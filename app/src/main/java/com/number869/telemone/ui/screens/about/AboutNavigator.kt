package com.number869.telemone.ui.screens.about

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.runtime.Composable
import com.number869.telemone.ui.screens.about.components.PrivacyPolicyDialog
import com.number869.telemone.ui.screens.about.components.TosDialog
import com.nxoim.decomposite.core.common.navigation.NavHost
import com.nxoim.decomposite.core.common.navigation.animations.cleanSlideAndFade
import com.nxoim.decomposite.core.common.navigation.navController
import kotlinx.serialization.Serializable

@Composable
fun AboutNavigator() {
    val aboutNavController = navController<AboutDestinations>(AboutDestinations.About)

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
            AboutDestinations.About -> AboutScreen()
            AboutDestinations.Dialogs.PrivacyPolicyDialog -> PrivacyPolicyDialog()
            AboutDestinations.Dialogs.TosDialog -> TosDialog()
        }
    }
}

@Serializable
sealed interface AboutDestinations {
    @Serializable
    data object About : AboutDestinations
    @Serializable
    sealed interface Dialogs : AboutDestinations {
        @Serializable
        data object PrivacyPolicyDialog : Dialogs

        @Serializable
        data object TosDialog : Dialogs
    }
}