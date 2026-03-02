package com.number869.telemone.ui.shared

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.positionChangeIgnoreConsumed
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.backhandler.BackEvent
import com.arkivanov.essenty.backhandler.BackEvent.SwipeEdge


// based on PredictiveBackGestureOverlay from the decompose extensions
/**
 * Handles back gestures on both edges of the screen and drives the provided [BackDispatcher] accordingly.
 *
 * @param defaultComponentContext is used for getting the back dispatcher conveniently.
 * @param modifier a [Modifier] to applied to the overlay.
 * @param startEdgeEnabled controls whether the start edge is enabled or not,
 * left in LTR mode and right in RTL mode.
 * @param endEdgeEnabled controls whether the end edge is enabled or not,
 * right in LTR mode and left in RTL mode.
 * @param edgeWidth the width in [Dp] from the screen edge where the gesture first down touch is recognized. When null - use the entire width of the screen.
 * @param activationOffsetThreshold a distance threshold in [Dp] from the initial touch point in the direction
 * of gesture. The gesture is initiated once this threshold is surpassed.
 * @param progressConfirmationThreshold a threshold of progress that needs to be reached for the gesture
 * to be confirmed once the touch is completed. The gesture is cancelled if the touch is completed without
 * reaching the threshold.
 * @param content a content to be shown under the overlay.
 */
@Composable
@Stable
fun BackGestureProviderContainer(
    defaultComponentContext: DefaultComponentContext,
    modifier: Modifier = Modifier,
    startEdgeEnabled: Boolean = true,
    endEdgeEnabled: Boolean = false,
    edgeWidth: Dp? = 16.dp,
    activationOffsetThreshold: Dp = 4.dp,
    progressConfirmationThreshold: Float = 0.2F,
    velocityConfirmationThreshold: Dp = 8.dp,
    blockChildDragInputs: Boolean = true,
    content: @Composable () -> Unit,
) {
    val backDispatcher = defaultComponentContext.backHandler as BackDispatcher
    val layoutDirection = LocalLayoutDirection.current

    Box(
        modifier = modifier.backGestureProvider(
            backDispatcher = backDispatcher,
            leftEdgeEnabled = when (layoutDirection) {
                LayoutDirection.Ltr -> startEdgeEnabled
                LayoutDirection.Rtl -> endEdgeEnabled
            },
            rightEdgeEnabled = when (layoutDirection) {
                LayoutDirection.Ltr -> endEdgeEnabled
                LayoutDirection.Rtl -> startEdgeEnabled
            },
            edgeWidth = edgeWidth,
            activationOffsetThreshold = activationOffsetThreshold,
            progressConfirmationThreshold = progressConfirmationThreshold,
            velocityConfirmationThreshold = velocityConfirmationThreshold,
            blockChildDragInputs = blockChildDragInputs
        ),
    ) {
        content()
    }
}

/**
 * Detects drag gestures on a composable and dispatches back gestures to a [BackDispatcher].
 *
 * @param backDispatcher The [BackDispatcher] instance that will receive the back gestures.
 * @param leftEdgeEnabled Whether the left edge of the composable is enabled for back gestures.
 * @param rightEdgeEnabled Whether the right edge of the composable is enabled for back gestures.
 * @param edgeWidth The width of the edge area that will trigger a back gesture. Defaults to null.
 * When null - uses the full composable's width
 * @param activationOffsetThreshold The minimum distance the user must drag to activate a back gesture. Defaults to 4.dp.
 * @param progressConfirmationThreshold The minimum progress required to confirm a back gesture. Defaults to 0.2F.
 * @param velocityConfirmationThreshold The minimum velocity required to confirm a back gesture. Defaults to 8.dp.
 * @param blockChildDragInputs Whether to block child drag inputs. Defaults to false.
 */
fun Modifier.backGestureProvider(
    backDispatcher: BackDispatcher,
    leftEdgeEnabled: Boolean = true,
    rightEdgeEnabled: Boolean = false,
    edgeWidth: Dp? = null,
    activationOffsetThreshold: Dp = 4.dp,
    progressConfirmationThreshold: Float = 0.2F,
    velocityConfirmationThreshold: Dp = 8.dp,
    blockChildDragInputs: Boolean = false
) = this.backGestureDetector(
    leftEdgeEnabled = leftEdgeEnabled,
    rightEdgeEnabled = rightEdgeEnabled,
    edgeWidth = edgeWidth,
    activationOffsetThreshold = activationOffsetThreshold,
    progressConfirmationThreshold = progressConfirmationThreshold,
    velocityConfirmationThreshold = velocityConfirmationThreshold,
    blockChildDragInputs = blockChildDragInputs,
    onStart = backDispatcher::startPredictiveBack,
    onProgress = backDispatcher::progressPredictiveBack,
    onCancel = backDispatcher::cancelPredictiveBack,
    onConfirm = backDispatcher::back
)

fun Modifier.backGestureDetector(
    leftEdgeEnabled: Boolean = true,
    rightEdgeEnabled: Boolean = false,
    edgeWidth: Dp? = null,
    activationOffsetThreshold: Dp = 4.dp,
    progressConfirmationThreshold: Float = 0.2F,
    velocityConfirmationThreshold: Dp = 8.dp,
    blockChildDragInputs: Boolean = false,
    onStart: (BackEvent) -> Unit,
    onProgress: (BackEvent) -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) = if (blockChildDragInputs) pointerInput(leftEdgeEnabled, rightEdgeEnabled) {
    val triggerWidth = edgeWidth?.toPx() ?: size.width.toFloat()

    awaitEachGesture {
        val firstDown = awaitFirstDown(pass = PointerEventPass.Initial)

        val startPosition = firstDown.position
        val isLeftEdge = leftEdgeEnabled && (startPosition.x < triggerWidth)
        val isRightEdge = rightEdgeEnabled && (startPosition.x > size.width - triggerWidth)
        val edge = when {
            isLeftEdge && isRightEdge -> {
                if (startPosition.x < size.width / 2F)
                    BackGestureHandler.Edge.LEFT
                else
                    BackGestureHandler.Edge.RIGHT
            }

            isLeftEdge -> BackGestureHandler.Edge.LEFT
            isRightEdge -> BackGestureHandler.Edge.RIGHT
            else -> return@awaitEachGesture
        }

        val handler = BackGestureHandler(
            pointerId = firstDown.id,
            startPosition = startPosition,
            edge = edge,
            ignoreOffsetThreshold = triggerWidth,
            activationOffsetThreshold = activationOffsetThreshold.toPx(),
            progressConfirmationThreshold = progressConfirmationThreshold,
            velocityConfirmationThreshold = velocityConfirmationThreshold.toPx(),
            onStart = onStart,
            onProgress = onProgress,
            onCancel = onCancel,
            onConfirm = onConfirm
        )
        with(handler) { handleGesture() }
    }
} else pointerInput(leftEdgeEnabled, rightEdgeEnabled) {
    val triggerWidth = edgeWidth?.let { it.value * density } ?: size.width.toFloat()
    var edge: SwipeEdge = SwipeEdge.UNKNOWN
    var progress = 0f
    var velocityPx = 0f
    var totalDistanceSwipedPx = 0f

    var dispatchingGestures: Boolean? = null

    detectHorizontalDragGestures(
        onDragStart = { offset ->
            edge = when {
                !rightEdgeEnabled -> SwipeEdge.LEFT
                !leftEdgeEnabled -> SwipeEdge.RIGHT
                else -> if (offset.x < (size.width / 2)) SwipeEdge.LEFT else SwipeEdge.RIGHT
            }

            // reset swipe state variables.
            totalDistanceSwipedPx = 0f
            progress = 0f
            velocityPx = 0f
            dispatchingGestures = null

            // check if we should dispatch gestures based on the starting position and enabled edges.
            dispatchingGestures = if (edge == SwipeEdge.LEFT) {
                if (leftEdgeEnabled)
                    offset.x <= triggerWidth
                else
                    return@detectHorizontalDragGestures
            } else {
                if (rightEdgeEnabled)
                    size.width - offset.x <= triggerWidth
                else
                    return@detectHorizontalDragGestures
            }

            if (dispatchingGestures == true) {
                onStart(
                    BackEvent(
                        progress = progress.coerceIn(0f, 1f),
                        swipeEdge = edge,
                        touchX = offset.x,
                        touchY = offset.y
                    )
                )
            }
        },
        onHorizontalDrag = { change, _ ->
            velocityPx = if (edge == SwipeEdge.LEFT) {
                change.positionChange().x
            } else {
                -change.positionChange().x
            }

            // stop dispatching gestures if the threshold is exceeded in the wrong direction.
            if (totalDistanceSwipedPx < 0) dispatchingGestures = false

            if (dispatchingGestures == true) {
                if (totalDistanceSwipedPx < activationOffsetThreshold.value * density) {
                    totalDistanceSwipedPx += velocityPx
                } else {
                    progress += (velocityPx / size.width)

                    onProgress(
                        BackEvent(
                            progress = progress.coerceIn(0f, 1f),
                            swipeEdge = edge,
                            touchX = change.position.x,
                            touchY = change.position.y
                        )
                    )
                }
            }
        },
        onDragEnd = {
            if (dispatchingGestures == true) {
                val velocityThresholdMet =
                    velocityPx / density >= velocityConfirmationThreshold.value
                val progressThresholdMet = progress >= progressConfirmationThreshold

                if (velocityThresholdMet || progressThresholdMet)
                    onConfirm()
                else
                    onCancel()
            }
        },
        onDragCancel = {
            if (dispatchingGestures == true) onCancel()
        }
    )
}

/**
 * Handles a back gesture event.
 *
 * @param pointerId The ID of the pointer that triggered the gesture.
 * @param startPosition The starting position of the gesture.
 * @param edge The edge of the composable that triggered the gesture.
 * @param ignoreOffsetThreshold The minimum distance to ignore when detecting the gesture.
 * @param activationOffsetThreshold The minimum distance the user must drag to activate a back gesture.
 * @param progressConfirmationThreshold The minimum progress required to confirm a back gesture.
 * @param velocityConfirmationThreshold The minimum velocity required to confirm a back gesture.
 */
private class BackGestureHandler(
    private val pointerId: PointerId,
    private val startPosition: Offset,
    private val edge: Edge,
    private val ignoreOffsetThreshold: Float,
    private val activationOffsetThreshold: Float,
    private val progressConfirmationThreshold: Float,
    private val velocityConfirmationThreshold: Float,
    private val onStart: (BackEvent) -> Unit,
    private val onProgress: (BackEvent) -> Unit,
    private val onCancel: () -> Unit,
    private val onConfirm: () -> Unit
) {
    private var changesIterator: Iterator<PointerInputChange>? = null
    private var progress = 0f

    private suspend fun AwaitPointerEventScope.awaitStart(): Boolean {
        val change = awaitStartChange().also { it?.consume() } ?: return false
        val position = change.position

        onStart(
            BackEvent(
                progress = progress,
                swipeEdge = edge.toSwipeEdge(),
                touchX = position.x,
                touchY = position.y,
            )
        )

        return true
    }

    suspend fun AwaitPointerEventScope.handleGesture() {
        if (awaitStart()) processGesture()
    }

    private suspend fun AwaitPointerEventScope.awaitChange(): PointerInputChange {
        while (true) {
            var iterator = changesIterator

            while ((iterator == null) || !iterator.hasNext()) {
                val changes = awaitPointerEvent(pass = PointerEventPass.Initial).changes

                iterator = changes.iterator()
                changesIterator = iterator
            }

            iterator.next().takeIf { it.id == pointerId }?.also {
                return it
            }
        }
    }

    private suspend fun AwaitPointerEventScope.awaitStartChange(): PointerInputChange? {
        while (true) {
            val change = awaitChange()
            val position = change.position

            if (!change.pressed ||
                (position.y < startPosition.y - ignoreOffsetThreshold) ||
                (position.y > startPosition.y + ignoreOffsetThreshold)
            ) {
                return null
            }

            when (edge) {
                Edge.LEFT ->
                    when {
                        position.x < startPosition.x - ignoreOffsetThreshold -> return null
                        position.x > startPosition.x + activationOffsetThreshold -> return change
                    }

                Edge.RIGHT ->
                    when {
                        position.x > startPosition.x + ignoreOffsetThreshold -> return null
                        position.x < startPosition.x - activationOffsetThreshold -> return change
                    }
            }
        }
    }

    private suspend fun AwaitPointerEventScope.processGesture() {
        while (true) {
            val change = awaitChange().also { it.consume() }
            val velocity = change.positionChangeIgnoreConsumed().x

            progress = when (edge) {
                Edge.LEFT -> progress + (velocity / size.width)
                Edge.RIGHT -> progress + (velocity / -size.width)
            }.coerceIn(0f, 1f)

            onProgress(
                BackEvent(
                    progress = progress,
                    swipeEdge = edge.toSwipeEdge(),
                    touchX = change.position.x,
                    touchY = change.position.y,
                )
            )

            val reachedProgressThreshold = progress > progressConfirmationThreshold
            val reachedVelocityThreshold = velocity >= velocityConfirmationThreshold

            if (!change.pressed) {
                if (reachedProgressThreshold || reachedVelocityThreshold)
                    onConfirm()
                else
                    onCancel()

                return
            }
        }
    }

    private fun Edge.toSwipeEdge() = when (this) {
        Edge.LEFT -> SwipeEdge.LEFT
        Edge.RIGHT -> SwipeEdge.RIGHT
    }

    enum class Edge { LEFT, RIGHT }
}


