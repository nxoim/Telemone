package com.number869.telemone.shared.utils

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.OverlayClip
import androidx.compose.animation.SharedTransitionScope.PlaceHolderSize
import androidx.compose.animation.SharedTransitionScope.PlaceHolderSize.Companion.contentSize
import androidx.compose.animation.SharedTransitionScope.ResizeMode
import androidx.compose.animation.SharedTransitionScope.ResizeMode.Companion.ScaleToBounds
import androidx.compose.animation.SharedTransitionScope.SharedContentState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

@OptIn(ExperimentalSharedTransitionApi::class)
val customBoundsTransform = BoundsTransform { _, _ -> spring(0.9f, 600f) }

@OptIn(ExperimentalSharedTransitionApi::class)
class CombinedSharedTransitionScope(
    private val sharedTransition: SharedTransitionScope,
    private val visibility: AnimatedVisibilityScope
) : AnimatedVisibilityScope by visibility, SharedTransitionScope by sharedTransition {
    fun Modifier.sharedBounds(
        sharedContentState: SharedContentState,
        enter: EnterTransition = fadeIn(),
        exit: ExitTransition = fadeOut(),
        boundsTransform: BoundsTransform = customBoundsTransform,
        resizeMode: ResizeMode = ScaleToBounds(ContentScale.FillWidth, Center),
        placeHolderSize: PlaceHolderSize = contentSize,
        renderInOverlayDuringTransition: Boolean = true,
        zIndexInOverlay: Float = 0f,
        clipInOverlayDuringTransition: OverlayClip = object : OverlayClip {
            override fun getClipPath(
                state: SharedContentState,
                bounds: Rect,
                layoutDirection: LayoutDirection,
                density: Density
            ): Path? {
                return state.parentSharedContentState?.clipPathInOverlay
            }
        }
    ) = this.sharedBounds(
        sharedContentState,
        visibility,
        enter,
        exit,
        boundsTransform,
        resizeMode,
        placeHolderSize,
        renderInOverlayDuringTransition,
        zIndexInOverlay,
        clipInOverlayDuringTransition
    )

    fun Modifier.sharedElement(
        state: SharedContentState,
        boundsTransform: BoundsTransform = customBoundsTransform,
        placeHolderSize: PlaceHolderSize = contentSize,
        renderInOverlayDuringTransition: Boolean = true,
        zIndexInOverlay: Float = 0f,
        clipInOverlayDuringTransition: OverlayClip =  object : OverlayClip {
            override fun getClipPath(
                state: SharedContentState,
                bounds: Rect,
                layoutDirection: LayoutDirection,
                density: Density
            ): Path? {
                return state.parentSharedContentState?.clipPathInOverlay
            }
        }
    ) = this.sharedElement(
        state,
        visibility,
        boundsTransform,
        placeHolderSize,
        renderInOverlayDuringTransition,
        zIndexInOverlay,
        clipInOverlayDuringTransition
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CombineSharedTransitionAndAnimatedVisibility(animatedVisibilityScope: AnimatedVisibilityScope, content: @Composable CombinedSharedTransitionScope.() -> Unit) {
    val scope = CombinedSharedTransitionScope(this@CombineSharedTransitionAndAnimatedVisibility, animatedVisibilityScope)
    CompositionLocalProvider(
        LocalCombinedSharedTransitionScope provides scope,
        content = { scope.content() }
    )
}

val LocalCombinedSharedTransitionScope = staticCompositionLocalOf<CombinedSharedTransitionScope?> {
    null
}