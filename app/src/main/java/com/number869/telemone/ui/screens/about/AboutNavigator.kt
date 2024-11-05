package com.number869.telemone.ui.screens.about

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.runtime.Composable
import com.number869.telemone.ui.RootDestinations
import com.number869.telemone.ui.screens.about.components.PrivacyPolicyDialog
import com.number869.telemone.ui.screens.about.components.TosDialog
import com.nxoim.decomposite.core.common.navigation.NavController
import com.nxoim.decomposite.core.common.navigation.NavHost
import com.nxoim.decomposite.core.common.navigation.animations.cleanSlideAndFade
import kotlinx.serialization.Serializable

@Composable
fun AboutNavigator(
    rootNavController: NavController<RootDestinations>,
    aboutNavController: NavController<AboutDestinations>,
    dialogsNavController: NavController<AboutDestinations.Dialogs>
) {
    NavHost(
        aboutNavController,
        animations = {
            if (currentChild is AboutDestinations.Dialogs)
                cleanSlideAndFade(orientation = Orientation.Vertical)
            else
                cleanSlideAndFade()
        }
    ) {
        when (it) {
            AboutDestinations.About -> AboutScreen(
                rootNavController,
                dialogsNavController
            )
        }
    }

    DialogsHost(dialogsNavController)
}

@Composable
private fun DialogsHost(
    dialogsNavController: NavController<AboutDestinations.Dialogs>
) {
    NavHost(dialogsNavController) { destination ->
        when (destination) {
            AboutDestinations.Dialogs.Empty -> { }
            AboutDestinations.Dialogs.PrivacyPolicyDialog -> PrivacyPolicyDialog(
                onNavigateBackRequest = { dialogsNavController.navigateBack() }
            )
            AboutDestinations.Dialogs.TosDialog -> TosDialog(
                onNavigateBackRequest = { dialogsNavController.navigateBack() }
            )
        }
    }
}

@Serializable
sealed interface AboutDestinations {
    @Serializable
    data object About : AboutDestinations
    @Serializable
    sealed interface Dialogs {
        @Serializable
        data object Empty : Dialogs

        @Serializable
        data object PrivacyPolicyDialog : Dialogs

        @Serializable
        data object TosDialog : Dialogs
    }
}