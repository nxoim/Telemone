package com.number869.telemone.ui.theme


/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.OverscrollFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutAwareModifierNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext
import kotlin.math.abs
import kotlin.math.sign

@Composable
fun rememberNotCupertinoOverscrollFactory(
    animationSpec: AnimationSpec<Float> = spring(0.9f, 180f)
): NotCupertinoOverscrollEffectFactory {
    val layoutDirection = LocalLayoutDirection.current

    return remember {
        NotCupertinoOverscrollEffectFactory(
            layoutDirection = layoutDirection,
            animationSpec = animationSpec
        )
    }
}

data class NotCupertinoOverscrollEffectFactory(
    private val layoutDirection: LayoutDirection,
    private val animationSpec: AnimationSpec<Float>
) : OverscrollFactory {
    @OptIn(ExperimentalFoundationApi::class)
    override fun createOverscrollEffect() =
        NotCupertinoOverscrollEffect(
            applyClip = false,
            animationSpec = animationSpec
        )
}

private enum class ScrollSource {
    DRAG, FLING
}

private enum class OverscrollDirection {
    UNKNOWN, VERTICAL, HORIZONTAL
}

// NOTE: FLING_FROM_OVERSCROLL should be reported each time finger releases when overscrolled
private enum class SpringAnimationReason {
    FLING_FROM_OVERSCROLL, POSSIBLE_SPRING_IN_THE_END
}

/*
 * Encapsulates internal calculation data representing per-dimension change after drag delta is consumed (or not)
 * by [CupertinoOverscrollEffect]
 */
private data class OverscrollAvailableDelta(
    // delta which will be used to perform actual content scroll
    val availableDelta: Float,

    // new overscroll value for dimension in context of which calculation returning
    // instance of this type was returned
    val newOverscrollValue: Float
)
/**
 * @param applyClip Some consumers of overscroll effect apply clip by themselves and some don't,
 * thus this flag is needed to update our modifier chain and make the clipping correct in every case while avoiding redundancy
 */
class NotCupertinoOverscrollEffect(
    val applyClip: Boolean,
    private val animationSpec: AnimationSpec<Float>
) : OverscrollEffect {

    /*
     * Direction of scrolling for this overscroll effect, derived from arguments during
     * [applyToScroll] calls. Technically this effect supports both dimensions, but current API requires
     * that different stages of animations spawned by this effect for both dimensions
     * end at the same time, which is not the case:
     * Spring->Fling->Spring, Fling->Spring, Spring->Fling effects can have different timing per dimension
     * (see Notes of https://github.com/JetBrains/compose-multiplatform-core/pull/609),
     * which is not possible to express without changing API. Hence this effect will be fixed to latest
     * received delta.
     */
    private var direction: OverscrollDirection = OverscrollDirection.UNKNOWN

    /*
     * Size of container is taking into consideration when computing rubber banding
     */
    private var scrollSize: Size = Size.Zero

    /*
     * Current offset in overscroll area
     * Negative for bottom-right
     * Positive for top-left
     * Zero if within the scrollable range
     * It will be mapped to the actual visible offset using the rubber banding rule inside
     * [Modifier.offset] within [effectModifier]
     */
    private var overscrollOffsetState = mutableStateOf(Offset.Zero)

    /**
     * In pixels
     */
    var overscrollOffset: Offset
        get () = overscrollOffsetState.value
        private set(value) {
            overscrollOffsetState.value = value
            drawCallScheduledByOffsetChange = true
        }
    private var drawCallScheduledByOffsetChange = true

    private var lastFlingUnconsumedDelta: Offset = Offset.Zero
    private val visibleOverscrollOffset: IntOffset
        get() = overscrollOffsetState.value.rubberBanded().round()

    override val isInProgress: Boolean
        get() =
        // If visible overscroll offset has at least one pixel
            // this effect is considered to be in progress
            visibleOverscrollOffset.toOffset().getDistance() > 0.5f

    private val overscrollNode = NotCupertinoOverscrollNode(
        offset = { visibleOverscrollOffset },
        onNodeRemeasured = { scrollSize = it.toSize() },
        onDraw = ::onDraw,
        applyClip = applyClip
    )
    override val node: DelegatableNode get() = overscrollNode

    private fun onDraw() {
        // Fix an issue where scrolling was cancelled but the overscroll effect was not completed.
        // Reset the overscroll effect when no ongoing animation or interaction is applied.
        if (!drawCallScheduledByOffsetChange && isInProgress && overscrollNode.pointersDown == 0) {
            overscrollOffsetState.value = Offset.Zero
        }

        drawCallScheduledByOffsetChange = false
    }

    private fun NestedScrollSource.toCupertinoScrollSource(): ScrollSource? =
        when (this) {
            NestedScrollSource.UserInput -> ScrollSource.DRAG
            NestedScrollSource.SideEffect -> ScrollSource.FLING
            else -> null
        }

    /*
     * Takes input scroll delta, current overscroll value, and scroll source, return [CupertinoOverscrollAvailableDelta]
     */
    @Stable
    private fun availableDelta(
        delta: Float,
        overscroll: Float,
        source: ScrollSource
    ): OverscrollAvailableDelta {
        // if source is fling:
        // 1. no delta will be consumed
        // 2. overscroll will stay the same
        if (source == ScrollSource.FLING) {
            return OverscrollAvailableDelta(delta, overscroll)
        }

        val newOverscroll = overscroll + delta

        return if (delta >= 0f && overscroll <= 0f) {
            if (newOverscroll > 0f) {
                OverscrollAvailableDelta(newOverscroll, 0f)
            } else {
                OverscrollAvailableDelta(0f, newOverscroll)
            }
        } else if (delta <= 0f && overscroll >= 0f) {
            if (newOverscroll < 0f) {
                OverscrollAvailableDelta(newOverscroll, 0f)
            } else {
                OverscrollAvailableDelta(0f, newOverscroll)
            }
        } else {
            OverscrollAvailableDelta(0f, newOverscroll)
        }
    }

    /*
     * Returns the amount of scroll delta available after user performed scroll inside overscroll area
     * It will update [overscroll] resulting in visual change because of [Modifier.offset] depending on it
     */
    private fun availableDelta(delta: Offset, source: ScrollSource): Offset {
        val (x, overscrollX) = availableDelta(delta.x, overscrollOffset.x, source)
        val (y, overscrollY) = availableDelta(delta.y, overscrollOffset.y, source)

        overscrollOffset = Offset(overscrollX, overscrollY)

        return Offset(x, y)
    }

    /*
     * Semantics of this method match the [OverscrollEffect.applyToScroll] one,
     * The only difference is NestedScrollSource being remapped to NotCupertinoScrollSource to narrow
     * processed states invariant
     */
    private fun applyToScroll(
        delta: Offset,
        source: ScrollSource,
        performScroll: (Offset) -> Offset
    ): Offset {
        // Calculate how much delta is available after being consumed by scrolling inside overscroll area
        val deltaLeftForPerformScroll = availableDelta(delta, source)

        // Then pass remaining delta to scroll closure
        val deltaConsumedByPerformScroll = performScroll(deltaLeftForPerformScroll)

        // Delta which is left after `performScroll` was invoked with availableDelta
        val unconsumedDelta = deltaLeftForPerformScroll - deltaConsumedByPerformScroll

        return when (source) {
            ScrollSource.DRAG -> {
                // [unconsumedDelta] is going into overscroll again in case a user drags and hits the
                // overscroll->content->overscroll or content->overscroll scenario within single frame
                overscrollOffset += unconsumedDelta
                lastFlingUnconsumedDelta = Offset.Zero
                delta - unconsumedDelta
            }

            ScrollSource.FLING -> {
                // If unconsumedDelta is not Zero, [CupertinoOverscrollEffect] will cancel fling and
                // start spring animation instead
                lastFlingUnconsumedDelta = unconsumedDelta
                delta - unconsumedDelta
            }
        }
    }

    override fun applyToScroll(
        delta: Offset,
        source: NestedScrollSource,
        performScroll: (Offset) -> Offset
    ): Offset {
        springAnimationScope?.cancel()
        springAnimationScope = null

        direction = direction.combinedWith(delta.toCupertinoOverscrollDirection())

        return source.toCupertinoScrollSource()?.let {
            applyToScroll(delta, it, performScroll)
        } ?: performScroll(delta)
    }

    override suspend fun applyToFling(
        velocity: Velocity,
        performFling: suspend (Velocity) -> Velocity
    ) {
        val availableFlingVelocity = playInitialSpringAnimationIfNeeded(velocity)
        val velocityConsumedByFling = performFling(availableFlingVelocity)
        val postFlingVelocity = availableFlingVelocity - velocityConsumedByFling

        val unconsumedDelta = lastFlingUnconsumedDelta.toFloat()
        if (unconsumedDelta == 0f && overscrollOffset == Offset.Zero) {
            return
        }

        playSpringAnimation(
            unconsumedDelta,
            postFlingVelocity.toFloat(),
            SpringAnimationReason.POSSIBLE_SPRING_IN_THE_END
        )
    }

    private fun Offset.toCupertinoOverscrollDirection(): OverscrollDirection {
        val hasXPart = abs(x) > 0f
        val hasYPart = abs(y) > 0f

        return if (hasXPart xor hasYPart) {
            if (hasXPart) {
                OverscrollDirection.HORIZONTAL
            } else {
                // hasYPart != hasXPart and hasXPart is false
                OverscrollDirection.VERTICAL
            }
        } else {
            // hasXPart and hasYPart are equal
            OverscrollDirection.UNKNOWN
        }
    }

    private fun OverscrollDirection.combinedWith(other: OverscrollDirection): OverscrollDirection =
        when (this) {
            OverscrollDirection.UNKNOWN -> when (other) {
                OverscrollDirection.UNKNOWN -> OverscrollDirection.UNKNOWN
                OverscrollDirection.VERTICAL -> OverscrollDirection.VERTICAL
                OverscrollDirection.HORIZONTAL -> OverscrollDirection.HORIZONTAL
            }

            OverscrollDirection.VERTICAL -> when (other) {
                OverscrollDirection.UNKNOWN, OverscrollDirection.VERTICAL -> OverscrollDirection.VERTICAL
                OverscrollDirection.HORIZONTAL -> OverscrollDirection.HORIZONTAL
            }

            OverscrollDirection.HORIZONTAL -> when (other) {
                OverscrollDirection.UNKNOWN, OverscrollDirection.HORIZONTAL -> OverscrollDirection.HORIZONTAL
                OverscrollDirection.VERTICAL -> OverscrollDirection.VERTICAL
            }
        }

    private fun Velocity.toFloat(): Float =
        toOffset().toFloat()

    private fun Float.toVelocity(): Velocity =
        toOffset().toVelocity()

    private fun Offset.toFloat(): Float =
        when (direction) {
            OverscrollDirection.UNKNOWN -> 0f
            OverscrollDirection.VERTICAL -> y
            OverscrollDirection.HORIZONTAL -> x
        }

    private fun Float.toOffset(): Offset =
        when (direction) {
            OverscrollDirection.UNKNOWN -> Offset.Zero
            OverscrollDirection.VERTICAL -> Offset(0f, this)
            OverscrollDirection.HORIZONTAL -> Offset(this, 0f)
        }

    private suspend fun playInitialSpringAnimationIfNeeded(initialVelocity: Velocity): Velocity {
        val velocity = initialVelocity.toFloat()
        val overscroll = overscrollOffset.toFloat()

        return if (overscroll != 0f) {
            playSpringAnimation(
                unconsumedDelta = 0f,
                velocity,
                SpringAnimationReason.FLING_FROM_OVERSCROLL
            ).toVelocity()
        } else {
            initialVelocity
        }
    }

    private var springAnimationScope: CoroutineScope? = null

    private suspend fun playSpringAnimation(
        unconsumedDelta: Float,
        initialVelocity: Float,
        reason: SpringAnimationReason
    ): Float {
        val initialValue = overscrollOffset.toFloat() + unconsumedDelta
        val initialSign = sign(initialValue)
        var currentVelocity = initialVelocity

        springAnimationScope?.cancel()
        springAnimationScope = CoroutineScope(coroutineContext)
        springAnimationScope?.run {
            AnimationState(
                Float.VectorConverter,
                initialValue,
                when (reason) {
                    // boost the spring
                    SpringAnimationReason.FLING_FROM_OVERSCROLL -> initialVelocity - (initialValue * 10f)
                    SpringAnimationReason.POSSIBLE_SPRING_IN_THE_END -> initialVelocity
                }
            ).animateTo(
                targetValue = 0f,
                animationSpec = animationSpec.run {
                    if (this is SpringSpec<*>)
                    // create a new spec to force the 1 pixel threshold
                        androidx.compose.animation.core.spring(
                            dampingRatio = this.dampingRatio,
                            stiffness = this.stiffness,
                            visibilityThreshold = 1f // 1 pixel
                        )
                    else
                        this
                }
            ) {
                overscrollOffset = value.toOffset()
                currentVelocity = velocity

                // NOTE: THE BLOCK BELOW CAUSES ISSUES WHEN INITIAL
                // VELOCITY WAS 0 AND TARGET WAS REACHED EVEN THOUGH
                // ANIMATION WAS NOT FINISHED.
                if (
                    reason == SpringAnimationReason.FLING_FROM_OVERSCROLL &&
                    initialSign != 0f &&
                    sign(value) != initialSign
                ) {
                    this.cancelAnimation()
                }
            }
            springAnimationScope = null
        }

        if (coroutineContext.isActive) {
            // The spring is critically damped, so in case spring-fling-spring sequence is slightly
            // offset and velocity is of the opposite sign, it will end up with no animation
            overscrollOffset = Offset.Zero
        }

        if (reason == SpringAnimationReason.POSSIBLE_SPRING_IN_THE_END) {
            currentVelocity = 0f
        }

        return currentVelocity
    }

    private fun Offset.rubberBanded(): Offset {
        if (scrollSize.width == 0f || scrollSize.height == 0f) {
            return Offset.Zero
        }

        return Offset(
            rubberBandedValue(this.x, scrollSize.width, RUBBER_BAND_COEFFICIENT),
            rubberBandedValue(this.y, scrollSize.height, RUBBER_BAND_COEFFICIENT)
        )
    }

    /*
     * Maps raw delta offset [value] on an axis within scroll container with [dimension]
     * to actual visible offset
     */
    private fun rubberBandedValue(value: Float, dimension: Float, coefficient: Float) =
        sign(value) * (1f - (1f / (abs(value) * coefficient / dimension + 1f))) * dimension

    companion object Companion {
        private const val RUBBER_BAND_COEFFICIENT = 0.55f
    }
}

private class NotCupertinoOverscrollNode(
    val offset: Density.() -> IntOffset,
    val onNodeRemeasured: (IntSize) -> Unit,
    val onDraw: () -> Unit,
    val applyClip: Boolean
) : Modifier.Node(),
    LayoutModifierNode,
    LayoutAwareModifierNode,
    DrawModifierNode,
    PointerInputModifierNode {
    override fun onRemeasured(size: IntSize) = onNodeRemeasured(size)

    var pointersDown by mutableStateOf(0)

    override fun onPointerEvent(
        pointerEvent: PointerEvent,
        pass: PointerEventPass,
        bounds: IntSize
    ) {
        if (pass == PointerEventPass.Final) {
            if (pointerEvent.type == PointerEventType.Press) {
                pointersDown++
            } else if (pointerEvent.type == PointerEventType.Release) {
                pointersDown--
                require(pointersDown >= 0) { "pointersDown cannot be negative" }
            }
        }
    }

    override fun onCancelPointerInput() {
        pointersDown = 0
    }

    override fun ContentDrawScope.draw() {
        onDraw()
        if (applyClip) {
            val bounds = Rect(-offset().toOffset(), size)
            val rect = size.toRect().intersect(bounds)
            clipRect(
                left = rect.left,
                top = rect.top,
                right = rect.right,
                bottom = rect.bottom,
            ) { this@draw.drawContent() }
        } else {
            this@draw.drawContent()
        }
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            // report animated offset because shared elements
            // that will definitely be used in lists will support
            // motion frame of reference in the future.
            // motion frame of reference would be used
            // to prevent "input jelly" effect to make animations
            // feel more responsive
            withMotionFrameOfReferencePlacement {
                placeable.placeWithLayer(offset())
            }
        }
    }
}

private fun Velocity.toOffset(): Offset =
    Offset(x, y)

private fun Offset.toVelocity(): Velocity =
    Velocity(x, y)