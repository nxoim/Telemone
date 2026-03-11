package com.number869.telemone.ui.screens.editor

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushToFront
import com.number869.telemone.data.ThemeData
import com.number869.telemone.data.ThemeManager
import com.number869.telemone.ui.screens.common.ThemeExporter
import com.number869.telemone.ui.screens.common.ThemeFilePicker
import com.number869.telemone.utils.lifecycledCoroutineScope
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

class EditorComponent(
    context: ComponentContext,
    private val navigateToParent: () -> Unit,
    private val themeManager: ThemeManager,
    private val themeExporter: ThemeExporter,
    private val themePicker: ThemeFilePicker,
    private val displayTypeSettings: ThemeDisplaySettings
) {
    val backHandler = context.backHandler
    private val dialogsNavigation = EditorDialogsNavigationImpl()
    private val editorNavigation = EditorNavigationImpl(
        dialogsNavigation,
        navigateToParent
    )

    val model = EditorModel(
        themeManager,
        navigation = editorNavigation,
        coroutineScope = context.lifecycledCoroutineScope(),
        themeExporter,
        themePicker,
        displayTypeSettings
    )

    val stack = context.childStack(
        editorNavigation,
        serializer = serializer(),
        initialConfiguration = EditorDestinations.Editor,
        handleBackButton = true,
        childFactory = { destination, _ ->
            when (destination) {
                EditorDestinations.Editor -> EditorDestinationsInstance.Editor
                EditorDestinations.ThemeValues -> EditorDestinationsInstance.ThemeValues
            }
        }
    )

    val dialogsStack = context.childStack(
        dialogsNavigation,
        key = "EditorDialogs",
        serializer = serializer(),
        initialConfiguration = EditorDestinations.Dialogs.Empty,
        childFactory = { destination, _ -> destination },
        handleBackButton = true
    )
}

private class EditorDialogsNavigationImpl()
    : StackNavigation<EditorDestinations.Dialogs> by StackNavigation()

private class EditorNavigationImpl(
    private val dialogsNavigator: StackNavigation<EditorDestinations.Dialogs>,
    private val navigateToParent: () -> Unit
) : EditorNavigation, StackNavigation<EditorDestinations> by StackNavigation() {
    override fun navigateBack() = pop() { popped ->
        if (!popped) navigateToParent()
    }
    override fun navigateToThemeValues() =
        pushToFront(EditorDestinations.ThemeValues)

    override fun navigateToDialog(destination: EditorDestinations.Dialogs) =
        dialogsNavigator.pushToFront(destination)

    override fun navigateBackDialog() = dialogsNavigator.pop()
}

sealed interface EditorDestinationsInstance {
    data object Editor : EditorDestinationsInstance
    data object ThemeValues : EditorDestinationsInstance
}


@Serializable
sealed interface EditorDestinations {
    @Serializable
    data object Editor : EditorDestinations

    @Serializable
    data object ThemeValues : EditorDestinations

    @Serializable
    sealed interface Dialogs {
        @Serializable
        data object Empty : Dialogs

        @Serializable
        data object SavedThemeTypeSelection : Dialogs

        @Serializable
        data class LoadThemeWithOptions(val uuid: String) : Dialogs

        @Serializable
        data class OverwriteDefaultThemeChoice(val withTheme: ThemeData) : Dialogs

        @Serializable
        data class OverwriteDefaultThemeConfirmation(
            val overwriteDark: Boolean,
            val withTheme: ThemeData
        ) : Dialogs

        @Serializable
        data class DeleteOneTheme(val theme: ThemeData) : Dialogs

        @Serializable
        data class DeleteSelectedThemes(val selectedThemeCount: Int) : Dialogs

        @Serializable
        data object ClearThemeBeforeLoadingFromFile : Dialogs
    }
}