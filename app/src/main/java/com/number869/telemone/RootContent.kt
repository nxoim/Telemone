package com.number869.telemone

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatableV2
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.number869.telemone.ui.screens.about.AboutNavigator
import com.number869.telemone.ui.screens.editor.EditorNavigator
import com.number869.telemone.ui.screens.main.MainScreen
import com.number869.telemone.ui.screens.welcome.WelcomeScreen
import com.number869.telemone.ui.shared.theme.cleanSlideAndFadeAnimator

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootContent(root: RootComponent) {
    val stack by root.stack.subscribeAsState()

    Children(
        stack = stack,
        animation = predictiveBackAnimation(
            backHandler = root.backHandler,
            fallbackAnimation = stackAnimation(cleanSlideAndFadeAnimator()),
            selector = { initialEvent, _, _ ->
                androidPredictiveBackAnimatableV2(initialEvent)
            },
            onBack = { root.navigateBack() }
        )
    ) {
        when (val instance = it.instance) {
            is RootDestinationsInstance.Welcome -> WelcomeScreen(
                onAgree = instance.onAgree
            )
            is RootDestinationsInstance.Main -> MainScreen(
                vm = instance.model,
                onNavigateToEditor = instance.model.navigation::navigateToEditor,
                onNavigateToAbout = instance.model.navigation::navigateToAbout
            )
            is RootDestinationsInstance.Editor -> EditorNavigator(instance.component)
            is RootDestinationsInstance.About -> AboutNavigator(instance.component)
        }
    }
}