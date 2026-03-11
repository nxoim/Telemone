package com.nxoim.blean.ui.composeUiCommons.modifiers.swipeable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.annotation.RememberInComposition
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Velocity
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

fun interface SwipeConstraint {
    fun classify(angleDegrees: Float): SwipeDirection?
    fun classify(delta: Offset): SwipeDirection? = classify(delta.x, delta.y)
    fun classify(delta: Velocity): SwipeDirection? = classify(delta.x, delta.y)
    fun classify(deltaX: Float, deltaY: Float): SwipeDirection? {
        if (deltaX == 0f && deltaY == 0f) return null

        return classify(angleFrom(deltaX, deltaY))
    }

    companion object {
        fun angleFrom(delta: Offset) = angleFrom(delta.x, delta.y)
        fun angleFrom(delta: Velocity) = angleFrom(delta.x, delta.y)
        fun angleFrom(deltaX: Float, deltaY: Float) =
            ((atan2(deltaY, deltaX) * 180f / pi) + 360f) % 360f
        private fun shortestDiff(a: Float, b: Float): Float {
            val raw = abs(a - b)
            return if (raw > 180f) 360f - raw else raw
        }

        private fun angle(
            angleDegrees: Float,
            direction: SwipeDirection,
            tolerance: Float
        ): SwipeConstraint = SwipeConstraint { angle ->
            if (shortestDiff(angle, angleDegrees) <= (tolerance / 2)) direction else null
        }

        private fun horizontal(layoutDirection: LayoutDirection, tolerance: Float): SwipeConstraint {
            val startAngle = if (layoutDirection == LayoutDirection.Ltr) 180f else 0f
            val endAngle = if (layoutDirection == LayoutDirection.Ltr) 0f else 180f

            return SwipeConstraint { angle ->
                val toStart = shortestDiff(angle, startAngle)
                if (toStart <= tolerance / 2) return@SwipeConstraint SwipeDirection.Companion.Start
                val toEnd = shortestDiff(angle, endAngle)
                if (toEnd <= tolerance / 2) return@SwipeConstraint SwipeDirection.Companion.End
                null
            }
        }

        private fun fourWay(
            layoutDirection: LayoutDirection,
            tolerance: Float
        ): SwipeConstraint {
            val startAngle = if (layoutDirection == LayoutDirection.Ltr) 180f else 0f
            val endAngle = if (layoutDirection == LayoutDirection.Ltr) 0f else 180f
            val topAngle = 270f
            val bottomAngle = 90f

            return SwipeConstraint { angle ->
                val toStart = shortestDiff(angle, startAngle)
                if (toStart <= tolerance / 2) return@SwipeConstraint SwipeDirection.Companion.Start
                val toEnd = shortestDiff(angle, endAngle)
                if (toEnd <= tolerance / 2) return@SwipeConstraint SwipeDirection.Companion.End
                val toTop = shortestDiff(angle, topAngle)
                if (toTop <= tolerance / 2) return@SwipeConstraint SwipeDirection.Companion.Top
                val toBottom = shortestDiff(angle, bottomAngle)
                if (toBottom <= tolerance / 2) return@SwipeConstraint SwipeDirection.Companion.Bottom
                null
            }
        }

        private fun all(layoutDirection: LayoutDirection): SwipeConstraint {
            val startAngle = if (layoutDirection == LayoutDirection.Ltr) 180f else 0f
            val endAngle = if (layoutDirection == LayoutDirection.Ltr) 0f else 180f
            val upStartAngle = if (layoutDirection == LayoutDirection.Ltr) 225f else 315f
            val upEndAngle = if (layoutDirection == LayoutDirection.Ltr) 315f else 225f
            val downStartAngle = if (layoutDirection == LayoutDirection.Ltr) 135f else 45f
            val downEndAngle = if (layoutDirection == LayoutDirection.Ltr) 45f else 135f

            val tolerance = 22.5f // need to divide 45 by two. 45 because 45 * 8 is 360

            return SwipeConstraint { angle ->
                val toEnd = shortestDiff(angle, endAngle)
                if (toEnd <= tolerance) return@SwipeConstraint SwipeDirection.Companion.End
                val toDownEnd = shortestDiff(angle, downEndAngle)
                if (toDownEnd <= tolerance) return@SwipeConstraint SwipeDirection.Companion.BottomEnd
                val toBottom = shortestDiff(angle, 90f)
                if (toBottom <= tolerance) return@SwipeConstraint SwipeDirection.Companion.Bottom
                val toDownStart = shortestDiff(angle, downStartAngle)
                if (toDownStart <= tolerance) return@SwipeConstraint SwipeDirection.Companion.BottomStart
                val toStart = shortestDiff(angle, startAngle)
                if (toStart <= tolerance) return@SwipeConstraint SwipeDirection.Companion.Start
                val toUpStart = shortestDiff(angle, upStartAngle)
                if (toUpStart <= tolerance) return@SwipeConstraint SwipeDirection.Companion.TopStart
                val toTop = shortestDiff(angle, 270f)
                if (toTop <= tolerance) return@SwipeConstraint SwipeDirection.Companion.Top
                val toUpEnd = shortestDiff(angle, upEndAngle)
                if (toUpEnd <= tolerance) return@SwipeConstraint SwipeDirection.Companion.TopEnd
                null
            }
        }

        @RememberInComposition
        fun anyOf(vararg constraints: SwipeConstraint): SwipeConstraint =
            SwipeConstraint { angle ->
                constraints.firstNotNullOfOrNull { it.classify(angle) }
            }

        private fun single(
            direction: SwipeDirection,
            layoutDirection: LayoutDirection,
            tolerance: Float
        ): SwipeConstraint {
            val angleDegrees = when (direction) {
                SwipeDirection.Cardinal.Bottom -> 90f
                SwipeDirection.Cardinal.End -> if (layoutDirection == LayoutDirection.Ltr) 0f else 180f
                SwipeDirection.Cardinal.Start -> if (layoutDirection == LayoutDirection.Ltr) 180f else 0f
                SwipeDirection.Cardinal.Top -> 270f
                SwipeDirection.Diagonal.BottomEnd -> if (layoutDirection == LayoutDirection.Ltr) 45f else 135f
                SwipeDirection.Diagonal.BottomStart -> if (layoutDirection == LayoutDirection.Ltr) 135f else 45f
                SwipeDirection.Diagonal.TopEnd -> if (layoutDirection == LayoutDirection.Ltr) 315f else 225f
                SwipeDirection.Diagonal.TopStart -> if (layoutDirection == LayoutDirection.Ltr) 225f else 315f
            }
            return angle(angleDegrees, direction, tolerance)
        }


        @Composable
        fun fourWay(tolerance: Float = multiDirectionDefaultTolerance): SwipeConstraint {
            val layoutDirection = LocalLayoutDirection.current

            return remember(layoutDirection, tolerance) {
                fourWay(layoutDirection, tolerance)
            }
        }


        @Composable
        private fun single(direction: SwipeDirection, tolerance: Float): SwipeConstraint {
            val layoutDirection = LocalLayoutDirection.current
            return remember(layoutDirection, direction, tolerance) {
                single(direction, layoutDirection, tolerance)
            }
        }

        @Composable
        fun all(): SwipeConstraint {
            val layoutDirection = LocalLayoutDirection.current

            return remember(layoutDirection) { all(layoutDirection) }
        }

        @Composable
        fun horizontal(tolerance: Float = multiDirectionDefaultTolerance): SwipeConstraint {
            val layoutDirection = LocalLayoutDirection.current

            return remember(layoutDirection, tolerance) {
                horizontal(layoutDirection, tolerance)
            }
        }

        @Composable
        fun vertical(tolerance: Float = multiDirectionDefaultTolerance) = remember(tolerance) {
            SwipeConstraint { angle ->
                val toTop = shortestDiff(angle, 270f)
                if (toTop <= tolerance / 2) return@SwipeConstraint SwipeDirection.Companion.Top
                val toBottom = shortestDiff(angle, 90f)
                if (toBottom <= tolerance / 2) return@SwipeConstraint SwipeDirection.Companion.Bottom
                null
            }
        }

        @Composable
        fun end(tolerance: Float = singleDirectionDefaultTolerance) =
            single(SwipeDirection.Companion.End, tolerance)

        @Composable
        fun start(tolerance: Float = singleDirectionDefaultTolerance) =
            single(SwipeDirection.Companion.Start, tolerance)

        @Composable
        fun top(tolerance: Float = singleDirectionDefaultTolerance) =
            single(SwipeDirection.Companion.Top, tolerance)

        @Composable
        fun bottom(tolerance: Float = singleDirectionDefaultTolerance) =
            single(SwipeDirection.Companion.Bottom, tolerance)

        @Composable
        fun topStart(tolerance: Float = singleDirectionDefaultTolerance) =
            single(SwipeDirection.Companion.TopStart, tolerance)

        @Composable
        fun topEnd(tolerance: Float = singleDirectionDefaultTolerance) =
            single(SwipeDirection.Companion.TopEnd, tolerance)

        @Composable
        fun bottomStart(tolerance: Float = singleDirectionDefaultTolerance) =
            single(SwipeDirection.Companion.BottomStart, tolerance)

        @Composable
        fun bottomEnd(tolerance: Float = singleDirectionDefaultTolerance) =
            single(SwipeDirection.Companion.BottomEnd, tolerance)
    }
}

private const val multiDirectionDefaultTolerance = 90f
private const val singleDirectionDefaultTolerance = 120f
private const val pi = PI.toFloat()