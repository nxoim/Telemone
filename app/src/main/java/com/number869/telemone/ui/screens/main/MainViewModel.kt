package com.number869.telemone.ui.screens.main

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.number869.telemone.data.AppSettings
import com.number869.telemone.data.PredefinedTheme
import com.number869.telemone.data.ThemeManager
import com.number869.telemone.shared.utils.ThemeColorDataType
import com.number869.telemone.shared.utils.assetFoldersThemeHash
import com.number869.telemone.shared.utils.inject
import com.number869.telemone.shared.utils.showToast
import com.number869.telemone.shared.utils.stringify
import com.nxoim.decomposite.core.common.viewModel.ViewModel
import java.io.File

class MainViewModel(
    private val context: Context = inject(),
    private val themeManager: ThemeManager = inject()
) : ViewModel() {
    fun exportDefaultTheme(light: Boolean) {
        val themeName = if (light) "Telemone Light" else "Telemone Dark"
        val theme = stringify(
            themeManager.getThemeByUUID(PredefinedTheme.Default(light).uuid)!!.values,
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

    fun acceptThemeUpdate(light: Boolean) {
        themeManager.updateDefaultThemeFromStock(light)

        if (light) {
            AppSettings.lastAcceptedStockThemeHashLight.set(
                assetFoldersThemeHash(light = true)
            )
        } else {
            AppSettings.lastAcceptedStockThemeHashDark.set(
                assetFoldersThemeHash(light = false)
            )
        }

        showToast("Default theme updated")
    }
    fun declineThemeUpdate(light: Boolean) {
        if (light)
            AppSettings.lastDeclinedStockThemeHashLight
                .set(assetFoldersThemeHash(light = true))
        else
            AppSettings.lastDeclinedStockThemeHashDark
                .set(assetFoldersThemeHash(light = false))
    }
}