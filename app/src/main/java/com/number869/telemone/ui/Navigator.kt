package com.number869.telemone.ui

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
sealed interface RootDestinations {
	@Serializable
	data object Welcome : RootDestinations

	@Serializable
	data object Main : RootDestinations

	@Serializable
	data object Editor : RootDestinations

	@Serializable
	data object About : RootDestinations
}
