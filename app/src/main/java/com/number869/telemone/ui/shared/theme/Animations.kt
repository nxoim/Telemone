package com.number869.telemone.ui.shared.theme

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimator

fun softSpring() = spring(1.8f, 2500f, 0.0005f)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun scaleInWithFade(
    transformOrigin: TransformOrigin = TransformOrigin.Center
) = fadeIn(MaterialTheme.motionScheme.defaultEffectsSpec()) +
        scaleIn(MaterialTheme.motionScheme.defaultSpatialSpec(), material3AnimScale, transformOrigin)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun scaleOutWithFade(
    transformOrigin: TransformOrigin = TransformOrigin.Center
) = fadeOut(MaterialTheme.motionScheme.defaultEffectsSpec()) +
        scaleOut(MaterialTheme.motionScheme.defaultSpatialSpec(), material3AnimScale, transformOrigin)

private const val material3AnimScale = 0.92f

fun cleanSlideAndFadeAnimator(
    targetOffset: Dp = 64.dp,
    minimumAlpha: Float = 0.000001f,
    animationSpec: FiniteAnimationSpec<Float> = softSpring()
): StackAnimator = stackAnimator(animationSpec) { factor, _, content ->
    content(
        Modifier.graphicsLayer {
            translationX = targetOffset.toPx() * factor

            alpha = (1f - (factor * 2f) * (factor * 2f)).coerceIn(minimumAlpha, 1f)
        }
    )
}