package com.number869.telemone.ui.screens.main

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import com.number869.telemone.data.ThemeColorDataType
import com.number869.telemone.data.ThemeManager
import com.number869.telemone.data.defaultDarkThemeUUID
import com.number869.telemone.data.defaultLightThemeUUID
import com.number869.telemone.inject
import com.nxoim.decomposite.core.common.viewModel.ViewModel
import com.tencent.mmkv.MMKV
import java.io.File

@Stable
// funny of you to actually expect some sort of documentation in the
// comments
class MainViewModel(
    private val context: Context = inject(),
    private val themeManager: ThemeManager = inject()
) : ViewModel() {
    private val storageForStockThemeComparison = MMKV.mmkvWithID("storageForStockThemeComparison")

    var lightThemeCanBeUpdated by mutableStateOf(
        assetFoldersThemeHash(true)	!= lastThemeUpdatesHash(true)
    )
    var darkThemeCanBeUpdated  by mutableStateOf(
        assetFoldersThemeHash(false) != lastThemeUpdatesHash(false)
    )

    var displayLightThemeUpdateChoiceDialog by mutableStateOf(false)
    var displayDarkThemeUpdateChoiceDialog by mutableStateOf(false)

    // move into theme manager
    init { checkForThemeHashUpdates() }


    fun exportDefaultTheme(light: Boolean) {
        val targetThemeId = if (light) defaultLightThemeUUID else defaultDarkThemeUUID
        val themeName = if (light) "Telemone Light" else "Telemone Dark"
        val theme = themeManager.stringify(
            themeManager.getThemeByUUID(targetThemeId)!!.values,
            ThemeColorDataType.ColorValuesFromDevicesColorScheme
        )

        with(context) {
            File(cacheDir, "$themeName.attheme").writeText(theme)

            val uri = FileProvider.getUriForFile(
                applicationContext,
                "${packageName}.provider",
                File(cacheDir, "$themeName.attheme")
            )

            val intent = Intent(Intent.ACTION_SEND)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.type = "*/attheme"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(intent, themeName))
        }
    }

    private fun dialogVisibilitySettingKey(ofLight: Boolean) = "display${if (ofLight) "Light" else "Dark"}ThemeUpdateChoiceDialogFor}"
    private fun lastRememberedStockThemeKey(ofLight: Boolean) = "lastRememberedStock${if (ofLight) "Light" else "Dark"}ThemeHash"
    private fun lastThemeUpdatesHash(ofLight: Boolean) = storageForStockThemeComparison
        .decodeString(lastRememberedStockThemeKey(ofLight))

    private fun assetFoldersThemeHash(ofLight: Boolean) = context.assets
        .open("default${if (ofLight) "Light" else "Dark"}File.attheme")
        .bufferedReader()
        .readText()
        .hashCode()
        .toString()

    private fun checkForThemeHashUpdates() {
        listOf(true, false).forEach { ofLight ->
            // on first launch, when no previous theme hashes are saved,
            // hashes need to be saved
            if (lastThemeUpdatesHash(ofLight) == null) {
                storageForStockThemeComparison.encode(
                    lastRememberedStockThemeKey(ofLight),
                    assetFoldersThemeHash(ofLight)
                )
            } else {
                if (ofLight) {
                    if (lightThemeCanBeUpdated) {
                        displayLightThemeUpdateChoiceDialog = storageForStockThemeComparison.decodeBool(
                            dialogVisibilitySettingKey(true),
                            true
                        )
                    }
                } else {
                    if (darkThemeCanBeUpdated) {
                        displayDarkThemeUpdateChoiceDialog = storageForStockThemeComparison.decodeBool(
                            dialogVisibilitySettingKey(false),
                            true
                        )
                    }
                }
            }
        }
    }

    fun acceptTheStockThemeUpdate(ofLight: Boolean) {
        // remove the previously saved hashes so they don't accumulate
        val nullString: String? = null
        storageForStockThemeComparison.encode(lastRememberedStockThemeKey(ofLight), nullString)

        themeManager.updateDefaultThemeFromStock(ofLight)
        storageForStockThemeComparison.encode(
            dialogVisibilitySettingKey(ofLight),
            false
        )
        if (ofLight) {
            displayLightThemeUpdateChoiceDialog = false
            lightThemeCanBeUpdated = true
        } else {
            displayDarkThemeUpdateChoiceDialog = false
            darkThemeCanBeUpdated = true
        }
    }

    fun declineDefaultThemeUpdate(ofLight: Boolean) {
        storageForStockThemeComparison.encode(
            dialogVisibilitySettingKey(ofLight),
            false
        )

        if (ofLight) {
            displayLightThemeUpdateChoiceDialog = false
        } else {
            displayDarkThemeUpdateChoiceDialog = false
        }
    }
}