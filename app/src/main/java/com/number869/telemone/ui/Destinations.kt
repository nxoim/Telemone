package com.number869.telemone.ui

import kotlinx.serialization.Serializable
import javax.annotation.concurrent.Immutable

@Immutable
@Serializable
sealed interface Destinations {
	@Serializable
	data object WelcomeScreen : Destinations

	@Serializable
	data object MainScreen : Destinations

	@Serializable
	sealed interface EditorScreen : Destinations {
		@Serializable
		data object Editor : EditorScreen
		@Serializable
		data object ThemeValuesScreen : EditorScreen

		@Serializable
		sealed interface Dialogs : EditorScreen {
			@Serializable
			data object SavedThemeTypeSelection : Dialogs

			@Serializable
			data class LoadThemeWithOptions(val uuid: String) : Dialogs

			@Serializable
			data class OverwriteDefaultThemeChoice(val withThemeUuid: String) : Dialogs

			@Serializable
			data class OverwriteDefaultThemeConfirmation(
				val overwriteDark: Boolean,
				val withThemeUuid: String
			) : Dialogs

			@Serializable
			data class DeleteOneTheme(val uuid: String) : Dialogs

			@Serializable
			data object DeleteSelectedThemes : Dialogs

			@Serializable
			data object ClearThemeBeforeLoadingFromFile : Dialogs
		}
	}

	@Serializable
	sealed interface AboutScreen : Destinations {
		@Serializable
		data object About : AboutScreen
		@Serializable
		sealed interface Dialogs : AboutScreen {
			@Serializable
			data object PrivacyPolicyDialog : Dialogs

			@Serializable
			data object TosDialog : Dialogs
		}
	}
}