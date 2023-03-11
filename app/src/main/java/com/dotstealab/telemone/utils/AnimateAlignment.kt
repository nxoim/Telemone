package com.dotstealab.telemone.utils

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment

@Composable
fun animateAlignmentAsState(
	targetAlignment: Alignment,
	animationSpec: AnimationSpec<Float> = spring<Float>()
): State<Alignment> {
	val biased = targetAlignment as BiasAlignment
	val horizontal by animateFloatAsState(biased.horizontalBias, animationSpec)
	val vertical by animateFloatAsState(biased.verticalBias, animationSpec)
	return derivedStateOf { BiasAlignment(horizontal, vertical) }
}