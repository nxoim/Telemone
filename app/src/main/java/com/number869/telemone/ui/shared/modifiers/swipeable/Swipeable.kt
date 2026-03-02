package com.nxoim.blean.ui.composeUiCommons.modifiers.swipeable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.positionChangeIgnoreConsumed
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.input.pointer.util.addPointerInputChange
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirstOrNull
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

fun Modifier.swipeable(
    detectionConstraint: SwipeConstraint,
    confirmationConstraint: SwipeConstraint = detectionConstraint,
    onStart: GestureScope.(direction: SwipeDirection) -> Unit,
    onProgress: GestureScope.(
        delta: Offset,
        uptimeMillis: Long,
        activationDirection: SwipeDirection
    ) -> Unit,
    onConfirm: GestureScope.(
        velocity: Velocity,
        direction: SwipeDirection
    ) -> Unit,
    onCancel: GestureScope.(velocity: Velocity) -> Unit,
    thresholds: SwipeThresholds = SwipeThresholds.Default,
    isEnabled: (PointerType) -> Boolean = defaultEnabled,
    interactionSource: MutableInteractionSource? = null,
    key: Any = Unit,
    activationRequireUnconsumed: Boolean = true,
    activationDetectionPass: PointerEventPass = PointerEventPass.Main
): Modifier = swipeActionInternal(
    key = key,
    thresholds = thresholds,
    detectionConstraint = detectionConstraint,
    confirmationConstraint = confirmationConstraint,
    onStart = onStart,
    onProgress = onProgress,
    onConfirm = onConfirm,
    isEnabled = isEnabled,
    onCancel = onCancel,
    interactionSource = interactionSource,
    activationRequireUnconsumed = activationRequireUnconsumed,
    activationDetectionPass = activationDetectionPass
)

////////////////////////////////////////////////////////////////////////////////////////////

private fun Modifier.swipeActionInternal(
    key: Any?,
    thresholds: SwipeThresholds,
    detectionConstraint: SwipeConstraint,
    confirmationConstraint: SwipeConstraint,
    onStart: GestureScope.(direction: SwipeDirection) -> Unit,
    onProgress: GestureScope.(
        delta: Offset,
        uptimeMillis: Long,
        activationDirection: SwipeDirection
    ) -> Unit,
    onConfirm: GestureScope.(
        velocity: Velocity,
        direction: SwipeDirection
    ) -> Unit,
    isEnabled: (PointerType) -> Boolean,
    onCancel: GestureScope.(velocity: Velocity) -> Unit,
    interactionSource: MutableInteractionSource?,
    activationRequireUnconsumed: Boolean,
    activationDetectionPass: PointerEventPass,
): Modifier = pointerInput(
    key,
    thresholds,
    detectionConstraint,
    confirmationConstraint,
    activationRequireUnconsumed,
    activationDetectionPass,
    onStart,
    onProgress,
    onConfirm,
    onCancel,
    interactionSource,
    isEnabled
) {
    val velocityTracker = VelocityTracker()
    var totalSwipeDeltaPx = Offset.Zero
    var startedSwipingInDirection: SwipeDirection? = null
    var dragInteraction: DragInteraction.Start? = null

    fun resetSwipeStates() {
        totalSwipeDeltaPx = Offset.Zero
        startedSwipingInDirection = null
        velocityTracker.resetTracking()
        dragInteraction = null
    }

    with(GestureScope(this)) {
        detectDragGestures(
            activationRequireUnconsumed = activationRequireUnconsumed,
            activationDetectionPass = activationDetectionPass,
            constraint = detectionConstraint,
            isEnabled = isEnabled,
            offAxisCancellationDistancePx = thresholds.offAxisCancellation.toPx(),
            touchSlop = thresholds.activation?.toPx() ?: viewConfiguration.touchSlop,
            onDragStart = { change, direction ->
                resetSwipeStates()
                startedSwipingInDirection = direction
                velocityTracker.addPointerInputChange(change)

                interactionSource?.let {
                    val interaction = DragInteraction.Start()
                    dragInteraction = interaction
                    it.tryEmit(interaction)
                }

                onStart(direction)
                onProgress(Offset.Zero, change.uptimeMillis, direction)
                change.consume()
            },
            onDrag = { pointerChange, dragDelta ->
                totalSwipeDeltaPx += dragDelta
                velocityTracker.addPointerInputChange(pointerChange)
                onProgress(
                    pointerChange.positionChange(),
                    pointerChange.uptimeMillis,
                    startedSwipingInDirection!!
                )

                pointerChange.consume()
            },
            onDragCancel = {
                startedSwipingInDirection?.let {
                    dragInteraction?.let {
                        interactionSource?.tryEmit(DragInteraction.Cancel(it))
                    }
                    dragInteraction = null

                    onCancel(velocityTracker.calculateVelocity())
                }
            },
            onDragEnd = {
                startedSwipingInDirection?.let {
                    handleDragEnd(
                        totalSwipeDeltaPx = totalSwipeDeltaPx,
                        velocityTracker = velocityTracker,
                        constraint = confirmationConstraint,
                        confirmationVelocityPxPerMs = thresholds.confirmationVelocity.toPx(),
                        confirmationMinDistancePx = thresholds.confirmationMinDistance.toPx(),
                        onConfirm = { velocity, direction ->
                            dragInteraction?.let {
                                interactionSource?.tryEmit(DragInteraction.Stop(it))
                            }
                            dragInteraction = null

                            onConfirm(velocity, direction)
                        },
                        onCancel = {
                            dragInteraction?.let {
                                interactionSource?.tryEmit(DragInteraction.Cancel(it))
                            }
                            dragInteraction = null

                            onCancel(it)
                        }
                    )
                }
            }
        )
    }
}

private inline fun GestureScope.handleDragEnd(
    totalSwipeDeltaPx: Offset,
    velocityTracker: VelocityTracker,
    constraint: SwipeConstraint,
    confirmationVelocityPxPerMs: Float,
    confirmationMinDistancePx: Float,
    onConfirm: GestureScope.(
        velocity: Velocity,
        direction: SwipeDirection
    ) -> Unit,
    onCancel: GestureScope.(velocity: Velocity) -> Unit
) {
    val velocityPxPerMs = velocityTracker.calculateVelocity()

    val velocitySatisfiesThreshold =
        abs(velocityPxPerMs.x) >= confirmationVelocityPxPerMs ||
                abs(velocityPxPerMs.y) >= confirmationVelocityPxPerMs

    val distanceSquared = totalSwipeDeltaPx.x *
            totalSwipeDeltaPx.x +
            totalSwipeDeltaPx.y *
            totalSwipeDeltaPx.y
    val distanceSatisfiesThreshold =
        distanceSquared >= (confirmationMinDistancePx * confirmationMinDistancePx)

    if (velocitySatisfiesThreshold || distanceSatisfiesThreshold) {
        val currentSwipingDirection = if (velocitySatisfiesThreshold) {
            constraint.classify(
                deltaX = velocityPxPerMs.x,
                deltaY = velocityPxPerMs.y,
            )
        } else {
            constraint.classify(
                deltaX = totalSwipeDeltaPx.x,
                deltaY = totalSwipeDeltaPx.y
            )
        }

        // not null equals allowed
        if (currentSwipingDirection != null) {
            onConfirm(velocityPxPerMs, currentSwipingDirection)
        } else {
            onCancel(velocityPxPerMs)
        }
    } else {
        onCancel(velocityPxPerMs)
    }
}

private suspend fun PointerInputScope.detectDragGestures(
    constraint: SwipeConstraint,
    offAxisCancellationDistancePx: Float,
    touchSlop: Float,
    onDragStart: (down: PointerInputChange, direction: SwipeDirection) -> Unit,
    onDragEnd: (change: PointerInputChange) -> Unit,
    onDragCancel: () -> Unit = {},
    onDrag: (change: PointerInputChange, dragAmount: Offset) -> Unit,
    activationRequireUnconsumed: Boolean,
    activationDetectionPass: PointerEventPass,
    isEnabled: (PointerType) -> Boolean
) {
    val activationVelocityTracker = VelocityTracker()

    awaitEachGesture {
        val down = awaitFirstDown(activationRequireUnconsumed, activationDetectionPass)

        if (isEnabled(down.type)) {
            var overSlop = Offset.Zero
            var activationDirection: SwipeDirection? = null

            val drag = awaitTouchSlopOrCancellation(
                pointerId = down.id,
                constraint = constraint,
                offAxisCancellationDistancePx = offAxisCancellationDistancePx,
                touchSlop = touchSlop,
                velocityTracker = activationVelocityTracker,
                pass = activationDetectionPass
            ) { change, over, direction ->
                change.consume()
                overSlop = over
                activationDirection = direction
            }

            if (drag != null && activationDirection != null) {
                onDragStart(down, activationDirection)
                onDrag(drag, overSlop)

                val result = drag(
                    pointerId = drag.id,
                    onDrag = { change ->
                        onDrag(change, change.positionChange())
                        change.consume()
                    },
                    motionConsumed = { it.isConsumed }
                )

                if (result == null) onDragCancel() else onDragEnd(result)
            }
        }
    }
}

private suspend inline fun AwaitPointerEventScope.awaitTouchSlopOrCancellation(
    pointerId: PointerId,
    constraint: SwipeConstraint,
    offAxisCancellationDistancePx: Float,
    touchSlop: Float = viewConfiguration.touchSlop,
    pass: PointerEventPass = PointerEventPass.Initial,
    velocityTracker: VelocityTracker,
    onPointerSlopReached: (PointerInputChange, Offset, SwipeDirection) -> Unit,
): PointerInputChange? {
    var pointer = pointerId
    var totalPositionChange = Offset.Zero
    var startTime = 0L

    while (true) {
        val event = awaitPointerEvent(pass)
        val change = event.changes
            .firstOrNull { it.id == pointer }
            ?: return null

        if (change.isConsumed || !change.pressed) return null

        velocityTracker.addPointerInputChange(change)
        if (startTime == 0L) startTime = change.uptimeMillis

        val positionChange = change.positionChange()
        totalPositionChange += positionChange

        val distance = totalPositionChange.getDistance()

        if (distance >= touchSlop) {
            val velocity = velocityTracker.calculateVelocity()
            val timeDelta = change.uptimeMillis - startTime

            val directionFromPosition = constraint.classify(totalPositionChange)
            val directionFromVelocity = if (timeDelta > 0) {
                constraint.classify(velocity)
            } else null

            val direction = directionFromPosition ?: directionFromVelocity

            if (direction != null) {
                onPointerSlopReached(change, totalPositionChange, direction)
                return change
            } else {
                return null
            }
        }

        // Check off-axis cancellation
        if (
            abs(totalPositionChange.x) >= offAxisCancellationDistancePx ||
            abs(totalPositionChange.y) >= offAxisCancellationDistancePx
        ) {
            // Calculate velocity to check if it matches constraint
            val velocity = velocityTracker.calculateVelocity()
            val directionFromPosition = constraint.classify(totalPositionChange)
            val directionFromVelocity = constraint.classify(
                deltaX = velocity.x,
                deltaY = velocity.y
            )

            // Cancel only if BOTH position and velocity are outside the constraint
            if (directionFromPosition == null && directionFromVelocity == null) {
                return null
            }
        }
    }
}

internal suspend inline fun AwaitPointerEventScope.drag(
    pointerId: PointerId,
    onDrag: (PointerInputChange) -> Unit,
    motionConsumed: (PointerInputChange) -> Boolean,
): PointerInputChange? {
    if (currentEvent.changes.fastFirstOrNull { it.id == pointerId }?.pressed != true) {
        return null // The pointer has already been lifted, so the gesture is canceled
    }
    var pointer = pointerId
    while (true) {
        val change = awaitDragOrUp(pointer) {
            val positionChange = it.positionChangeIgnoreConsumed()
            positionChange != Offset.Zero
        }
            ?: return null

        if (motionConsumed(change)) return null

        if (change.changedToUpIgnoreConsumed()) return change

        onDrag(change)
        pointer = change.id
    }
}

private suspend inline fun AwaitPointerEventScope.awaitDragOrUp(
    pointerId: PointerId,
    hasDragged: (PointerInputChange) -> Boolean,
): PointerInputChange? {
    var pointer = pointerId
    while (true) {
        val event = awaitPointerEvent()
        val dragEvent = event.changes
            .fastFirstOrNull { it.id == pointer }
            ?: return null

        if (dragEvent.changedToUpIgnoreConsumed()) {
            val otherDown = event.changes.fastFirstOrNull { it.pressed }
            if (otherDown == null) {
                // This is the last "up"
                return dragEvent
            } else {
                pointer = otherDown.id
            }
        } else if (hasDragged(dragEvent)) {
            return dragEvent
        }
    }
}


@PublishedApi
internal val defaultEnabled = fun(type: PointerType): Boolean = true

///////////////////////////////////////////////////////////////////////////////////////////

@Preview
@Composable
fun SwipeableModifierPreview() {
    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                SwipeableCardPreview()
            }
        }
    }
}


@Composable
private fun SwipeableCardPreview() {
    var swipeDelta by remember { mutableStateOf(Offset.Zero) }
    var confirmed by remember { mutableStateOf(false) }
    var wrongDirection by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isDragging by interactionSource.collectIsDraggedAsState()

    Box(
        modifier = Modifier
            .size(340.dp, 520.dp)
            .shadow(20.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(
                when {
                    confirmed -> MaterialTheme.colorScheme.tertiaryContainer
                    isDragging -> MaterialTheme.colorScheme.secondaryContainer
                    else -> MaterialTheme.colorScheme.primaryContainer
                }
            )
            .swipeable(
                detectionConstraint = SwipeConstraint.horizontal(),
                interactionSource = interactionSource,
                onStart = {
                    confirmed = false
                    wrongDirection = true
                },
                onProgress = { delta, _, _ ->
                },
                onConfirm = { _, _ ->
                    wrongDirection = false
                },
                onCancel = {
                    wrongDirection = false
                }
            )
            .swipeable(
                detectionConstraint = SwipeConstraint.vertical(),
                confirmationConstraint = SwipeConstraint.top(),
                interactionSource = interactionSource,
                onStart = {
                    swipeDelta = Offset.Zero
                    confirmed = false
                },
                onProgress = { delta, _, _ ->
                    swipeDelta = delta
                },
                onConfirm = { _, _ ->
                    confirmed = true
                    swipeDelta = Offset.Zero
                },
                onCancel = {
                    swipeDelta = Offset.Zero
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (wrongDirection) {
                Text("Wrong direction")
            } else {
                Text(
                    text = when {
                        confirmed -> "Confirmed"
                        isDragging -> "Dragging..."
                        else -> "Swipe Up"
                    },
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Spacer(Modifier.height(24.dp))

            SwipeArrowIndicator(delta = swipeDelta)
        }
    }
}

@Composable
private fun SwipeArrowIndicator(delta: Offset) {
    AnimatedVisibility(delta != Offset.Zero) {
        val angle = SwipeConstraint.angleFrom(delta)
        val strength = sqrt(delta.x * delta.x + delta.y * delta.y)
        val alpha = (strength / 20f).coerceIn(0f, 1f)
        val color = MaterialTheme.colorScheme.primary

        Canvas(
            modifier = Modifier
                .size(80.dp)
                .alpha(alpha)
        ) {
            val arrowLength = 60.dp.toPx()
            val headLength = 20.dp.toPx()
            val headAngle = 30f

            rotate(angle, pivot = center) {
                drawLine(
                    color = color,
                    start = Offset(center.x - arrowLength / 2, center.y),
                    end = Offset(center.x + arrowLength / 2, center.y),
                    strokeWidth = 8.dp.toPx(),
                    cap = StrokeCap.Round
                )

                val tip = Offset(center.x + arrowLength / 2, center.y)

                fun head(angleOffset: Float): Offset {
                    val rad = (180f + angleOffset) * (PI.toFloat() / 180f)
                    return Offset(
                        tip.x + headLength * cos(rad),
                        tip.y + headLength * sin(rad)
                    )
                }

                drawLine(color, tip, head(-headAngle), 8.dp.toPx(), StrokeCap.Round)
                drawLine(color, tip, head(+headAngle), 8.dp.toPx(), StrokeCap.Round)
            }
        }
    }
}