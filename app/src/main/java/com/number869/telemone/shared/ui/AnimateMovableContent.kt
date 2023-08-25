package com.number869.telemone.shared.ui


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.layout.intermediateLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.round
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.animateMovableContent(
	lookaheadScope: LookaheadScope,
	offsetAnimationSpec: AnimationSpec<IntOffset> = spring(),
	sizeAnimationSpec: AnimationSpec<IntSize> = spring(),
	animationIsRunning: Boolean = true
) = composed {
	var offsetAnimation: Animatable<IntOffset, AnimationVector2D>? by remember { mutableStateOf(null) }
	var targetOffset: IntOffset? by remember { mutableStateOf(null) }

	var sizeAnimation: Animatable<IntSize, AnimationVector2D>? by remember { mutableStateOf(null) }
	var targetSize: IntSize? by remember {
		mutableStateOf(null)
	}

	this.intermediateLayout { measurable, constraints ->
		val animatedConstraints = Constraints.fixed(
			width = ((sizeAnimation?.value?.width ?: 0)).coerceAtLeast(0),
			height = (sizeAnimation?.value?.height ?: 0).coerceAtLeast(0)
		)

		val placeable = measurable.measure(animatedConstraints)

		layout(placeable.width, placeable.height) {
			val coordinates = coordinates

			if (coordinates != null) {
				val calculatedOffsetTarget = with(lookaheadScope) {
					lookaheadScopeCoordinates
						.localLookaheadPositionOf(coordinates)
						.round()
						.also { targetOffset = it }
				}
				val calculatedSizeTarget = lookaheadSize.also { targetSize = it }

				// sadly this runs when when parent composable moves too
				if (calculatedOffsetTarget != offsetAnimation?.targetValue) {
					offsetAnimation?.run {
						launch {
							animateTo(
								calculatedOffsetTarget,
								offsetAnimationSpec
							)
						}
					} ?: Animatable(calculatedOffsetTarget, IntOffset.VectorConverter).let {
						offsetAnimation = it
					}
				}

				if (calculatedSizeTarget != sizeAnimation?.targetValue) {
					sizeAnimation?.run {
						launch {
							animateTo(
								calculatedSizeTarget,
								sizeAnimationSpec
							)
						}
					} ?: Animatable(calculatedSizeTarget, IntSize.VectorConverter).let {
						sizeAnimation = it
					}
				}

				val placementOffset = lookaheadScopeCoordinates.localPositionOf(
					coordinates,
					Offset.Zero
				).round()

				val (x, y) = requireNotNull(offsetAnimation).run { value - placementOffset }
				if (animationIsRunning) placeable.place(x, y) else placeable.place(0, 0)
			} else {
				placeable.place(0, 0)
			}
		}
	}
}

// this doesnt work that well

//@OptIn(ExperimentalComposeUiApi::class)
//fun Modifier.animateMovableContentNoNull(
//	lookaheadScope: LookaheadScope,
//	offsetAnimationSpec: AnimationSpec<IntOffset> = spring(),
//	sizeAnimationSpec: AnimationSpec<IntSize> = spring(),
//	animationIsRunning: Boolean = true
//) = composed {
//	val offsetAnimation = remember { Animatable(IntOffset.Zero, IntOffset.VectorConverter) }
//	val sizeAnimation = remember { Animatable(IntSize.Zero, IntSize.VectorConverter) }
//
//	var targetOffset: IntOffset by remember { mutableStateOf(IntOffset.Zero) }
//	var targetSize: IntSize by remember { mutableStateOf(IntSize.Zero) }
//
//	LaunchedEffect(targetOffset) {
//		offsetAnimation.animateTo(targetOffset, offsetAnimationSpec)
//	}
//
//	LaunchedEffect(targetSize) {
//		sizeAnimation.animateTo(targetSize, sizeAnimationSpec)
//	}
//
//	this.intermediateLayout { measurable, constraints ->
//		val animatedConstraints = Constraints.fixed(
//			width = sizeAnimation.value.width.coerceAtLeast(0),
//			height = sizeAnimation.value.height.coerceAtLeast(0)
//		)
//
//		val placeable = measurable.measure(animatedConstraints)
//
//		layout(placeable.width, placeable.height) {
//			val coordinates = coordinates
//
//			if (coordinates != null) {
//				with(lookaheadScope) {
//					lookaheadScopeCoordinates
//						.localLookaheadPositionOf(coordinates)
//						.round()
//						.also { targetOffset = it }
//				}
//
//				lookaheadSize.also { targetSize = it }
//
//				val placementOffset = lookaheadScopeCoordinates.localPositionOf(
//					coordinates,
//					Offset.Zero
//				).round()
//
//				val (x, y) = offsetAnimation.value - placementOffset
//				if (animationIsRunning) placeable.place(x, y) else placeable.place(0, 0)
//			} else {
//				placeable.place(0, 0)
//			}
//		}
//	}
//}