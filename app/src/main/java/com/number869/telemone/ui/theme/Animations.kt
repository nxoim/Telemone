package com.number869.telemone.ui.theme

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.TransformOrigin


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