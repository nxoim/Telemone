package com.number869.telemone.shared.utils

import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier

@OptIn(ExperimentalSharedTransitionApi::class)
val customBoundaTransform = BoundsTransform { _, _ -> spring(0.9f, 600f) }

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.sharedElement(
    key: Any,
    visible: Boolean,
    renderInOverlay: Boolean = true,
    boundsTransform: BoundsTransform = customBoundaTransform,
    zIndexInOverlay: Float = 0f
) = with(LocalSharedTransitionScope.current) {
    sharedElementWithCallerManagedVisibility(
        rememberSharedContentState(key),
        boundsTransform = boundsTransform,
        renderInOverlayDuringTransition = renderInOverlay,
        visible = visible,
        zIndexInOverlay = zIndexInOverlay
    )
}

@ExperimentalSharedTransitionApi
val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope> {
    error("No SharedTransitionScope provided")
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScopeProvider(content: @Composable SharedTransitionScope.() -> Unit) = SharedTransitionLayout {
    CompositionLocalProvider(
        LocalSharedTransitionScope provides this,
        content = { content() }
    )
}

val LocalBooleanProvider = staticCompositionLocalOf<Boolean> { true }
