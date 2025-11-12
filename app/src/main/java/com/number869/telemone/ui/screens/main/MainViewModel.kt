package com.number869.telemone.ui.screens.main

import android.content.Context
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.number869.telemone.App
import com.number869.telemone.data.AppSettings
import com.number869.telemone.data.PredefinedTheme
import com.number869.telemone.data.ThemeManager
import com.number869.telemone.shared.utils.ThemeColorDataType
import com.number869.telemone.shared.utils.assetFoldersThemeHash
import com.number869.telemone.shared.utils.showToast
import com.number869.telemone.shared.utils.stringify
import com.nxoim.decomposite.core.common.viewModel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import java.io.File

class MainViewModel(
    private val themeManager: ThemeManager,
    private val settings: AppSettings
) : ViewModel() {
    val canLightBeUpdated = settings.lastAcceptedStockThemeHashLight
        .getAsFlow()
        .map { lastAcceptedHash ->
            withContext(Dispatchers.IO) {
                lastAcceptedHash == "" || assetFoldersThemeHash(light = true, App.context) != lastAcceptedHash
            }
        }
        .stateIn(viewModelScope, WhileSubscribed(), false)
    val canDarkBeUpdated = settings.lastAcceptedStockThemeHashDark
        .getAsFlow()
        .map { lastAcceptedHash ->
            withContext(Dispatchers.IO) {
                lastAcceptedHash == "" || assetFoldersThemeHash(light = false, App.context) != lastAcceptedHash
            }
        }
        .stateIn(viewModelScope, WhileSubscribed(), false)

    val shouldDisplayLightUpdateDialog = settings.lastDeclinedStockThemeHashLight
        .getAsFlow()
        .map { lastDeclinedThemeHash ->
            withContext(Dispatchers.IO) {
                lastDeclinedThemeHash != assetFoldersThemeHash(light = true, context = App.context)
            }
        }
        .stateIn(viewModelScope, WhileSubscribed(), false)

    val shouldDisplayDarkUpdateDialog = settings.lastDeclinedStockThemeHashDark
        .getAsFlow()
        .map { lastDeclinedThemeHash ->
            withContext(Dispatchers.IO) {
                lastDeclinedThemeHash != assetFoldersThemeHash(light = false, context = App.context)
            }
        }
        .stateIn(viewModelScope, WhileSubscribed(), false)

    fun exportDefaultTheme(light: Boolean, context: Context) {
        val themeName = if (light) "Telemone Light" else "Telemone Dark"
        val theme = stringify(
            themeManager.getThemeByUUID(PredefinedTheme.Default(light).uuid)!!.values,
            ThemeColorDataType.ColorValuesFromDevicesColorScheme,
            themeManager.paletteState.entirePaletteAsMap
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

    fun acceptThemeUpdate(light: Boolean, context: Context) {
        themeManager.updateDefaultThemeFromStock(light)

        if (light) {
            AppSettings.lastAcceptedStockThemeHashLight.set(
                assetFoldersThemeHash(light = true, context)
            )
        } else {
            AppSettings.lastAcceptedStockThemeHashDark.set(
                assetFoldersThemeHash(light = false, context)
            )
        }

        showToast("Default theme updated")
    }
    fun declineThemeUpdate(light: Boolean, context: Context) {
        if (light)
            AppSettings.lastDeclinedStockThemeHashLight
                .set(assetFoldersThemeHash(light = true, context))
        else
            AppSettings.lastDeclinedStockThemeHashDark
                .set(assetFoldersThemeHash(light = false, context))
    }
}