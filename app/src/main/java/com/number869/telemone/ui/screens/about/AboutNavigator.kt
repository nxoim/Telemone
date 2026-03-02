package com.number869.telemone.ui.screens.about

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.number869.telemone.ui.screens.about.components.PrivacyPolicyDialog
import com.number869.telemone.ui.screens.about.components.TosDialog

@Composable
fun AboutNavigator(component: AboutComponent) {
    AboutScreen(component.model)

    Children(component.dialogsStack) {
        when (it.instance) {
            AboutDestinations.Dialogs.Empty -> {}
            AboutDestinations.Dialogs.PrivacyPolicyDialog -> PrivacyPolicyDialog(
                onDIsmiss = component.model.navigation::navigateBack
            )
            AboutDestinations.Dialogs.TosDialog -> TosDialog(
                onDismiss = component.model.navigation::navigateBack
            )
        }
    }
}
