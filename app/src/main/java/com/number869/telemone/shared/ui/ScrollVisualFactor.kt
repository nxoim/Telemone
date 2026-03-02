package com.number869.telemone.shared.ui

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class ScrollVisualFactorNestedConnection(
    private val coroutineScope: CoroutineScope,
    private val thresholdPx: Float,
    private val animationDelay: Duration,
    private val animationSpec: AnimationSpec<Float>
) : NestedScrollConnection {
    private val _fraction = mutableFloatStateOf(0f)
    val fraction: State<Float> get() = _fraction
    private var job: Job? = null

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        val delta = consumed.y
        _fraction.value = (_fraction.value - delta / thresholdPx).coerceIn(0f, 1f)
        job?.cancel()

        return Offset.Zero
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        job?.cancel()
        job = coroutineScope.launch {
            val target = when {
                available.run { x != 0f || y != 0f } -> 0f // cant scroll anymore
                fraction.value > 0.7f -> 1f // scrolled enough
                else -> 0f
            }

            delay(animationDelay)
            animate(fraction.value, target, animationSpec = animationSpec) { value, _ ->
                _fraction.value = value
            }
        }

        return super.onPostFling(consumed, available)
    }

    fun reset() {
        coroutineScope.coroutineContext.cancelChildren()
        coroutineScope.launch {
            animate(fraction.value, 0f) { value, _ ->
                _fraction.value = value
            }
        }
    }
}

@JvmInline
value class ScrollVisualFactorRootImpl(
    private val connection: ScrollVisualFactorNestedConnection
) : ScrollVisualFactorRoot, NestedScrollConnection by connection {
    override val fraction get() = connection.fraction
    override fun reset() {
        connection.reset()
    }
}

interface ScrollVisualFactorRoot {
    val fraction: State<Float>
    fun reset()

    object Empty : ScrollVisualFactorRoot {
        override val fraction = mutableStateOf(0f)
        override fun reset() {}
    }
}

@Composable
fun rememberScrollVisualFactorRoot(
    threshold: Dp = 120.dp,
    animationDelay: Duration = defaultDelay,
    animationSpec: AnimationSpec<Float> = spring(visibilityThreshold = 0.0001f)
): ScrollVisualFactorRootImpl {
    val coroutineScope = rememberCoroutineScope()
    val thresholdPx = with(LocalDensity.current) { threshold.toPx() }

    return remember(coroutineScope, thresholdPx) {
        ScrollVisualFactorRootImpl(
            ScrollVisualFactorNestedConnection(
                coroutineScope,
                thresholdPx,
                animationDelay,
                animationSpec
            )
        )
    }
}

val LocalScrollVisualFactor = staticCompositionLocalOf<ScrollVisualFactorRoot> {
    ScrollVisualFactorRoot.Empty
}

fun Modifier.slideUpOnScroll(scrollVisualFactor: ScrollVisualFactorRoot) = graphicsLayer {
    val factor = scrollVisualFactor.fraction.value
    translationY = -size.height * factor.coerceIn(0f, 1f)

    this.transformOrigin = transformOriginTopBottom
    if (factor < 0f) {
        scaleY = abs(factor) + 1f
    }
}

fun Modifier.slideDownOnScroll(scrollVisualFactor: ScrollVisualFactorRoot) = graphicsLayer {
    val factor = scrollVisualFactor.fraction.value
    translationY = size.height * factor.coerceIn(0f, 1f)

    this.transformOrigin = transformOriginBottomTop
    if (factor < 0f) {
        scaleY = abs(factor) + 1f
    }
}

private val defaultDelay = 1.seconds

private val transformOriginBottomTop = TransformOrigin(0f, 1f)
private val transformOriginTopBottom = TransformOrigin(0f, 0f)