package com.number869.telemone.ui.screens.about

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.number869.telemone.ui.screens.common.LinkHandler
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

class AboutComponent(
    context: ComponentContext,
    private val navigateToParent: () -> Unit,
    private val linkHandler: LinkHandler,
    private val buildInfo: BuildInfo
) {
    private val navigation = DialogsNavigationImpl(linkHandler, navigateToParent)
    val model = AboutModel(navigation, buildInfo)

    val dialogsStack = context.childStack(
        navigation,
        key = "AboutDialogs",
        serializer = serializer(),
        initialConfiguration = AboutDestinations.Dialogs.Empty,
        childFactory = { destination, _ -> destination },
        handleBackButton = true
    )
}

private class DialogsNavigationImpl(
    private val linkHandler: LinkHandler,
    private val navigateToParent: () -> Unit
)
    : AboutNavigation,
    StackNavigation<AboutDestinations.Dialogs> by StackNavigation() {
    override fun navigateBack() = pop() { popped ->
        if (!popped) navigateToParent()
    }
    override fun navigateToPrivacy() = pushNew(AboutDestinations.Dialogs.PrivacyPolicyDialog)
    override fun navigateToTOS() = pushNew(AboutDestinations.Dialogs.TosDialog)
    override fun openUri(uri: String) = linkHandler.handle(uri)
    }


interface AboutNavigation {
    fun navigateBack()
    fun navigateToPrivacy()
    fun navigateToTOS()
    fun openUri(uri: String)
}

interface BuildInfo {
    val versionString: String
}

class AboutModel(
    val navigation: AboutNavigation,
    val buildInfo: BuildInfo
)

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