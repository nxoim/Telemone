package com.nxoim.blean.ui.composeUiCommons.modifiers.swipeable

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * @property activation Threshold for activating the swipe gesture detection.
 *                        If null, uses the default touch slop value from view configuration.
 * @property confirmationMinDistance Minimum distance the swipe must travel to be confirmed.
 * @property confirmationVelocity Velocity threshold required for swipe confirmation.
 * @property offAxisCancellation Distance that triggers cancellation when swiping
 *                                away from the permitted swipe direction.
 */
data class SwipeThresholds(
    val activation: Dp?,
    val confirmationMinDistance: Dp,
    val confirmationVelocity: Dp,
    val offAxisCancellation: Dp,
) {
    companion object {
        /**
         * Default swipe thresholds with standard value.
         * Activation uses system touch slop
         */
        val Default = SwipeThresholds(
            activation = null,
            confirmationMinDistance = 32.dp,
            confirmationVelocity = 4.dp,
            offAxisCancellation = 1.dp,
        )
    }
}