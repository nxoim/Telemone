package com.number869.telemone

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.number869.telemone.common.ThemeFileSource
import com.number869.telemone.data.NullableSetting
import com.number869.telemone.data.Setting
import com.number869.telemone.data.SettingsManager
import com.number869.telemone.data.StockThemeHashSource
import com.number869.telemone.data.StockThemeUpdateSettings
import com.number869.telemone.ui.screens.about.BuildInfo
import com.number869.telemone.ui.screens.common.LinkHandler
import com.number869.telemone.ui.screens.common.ThemeExporter
import com.number869.telemone.ui.screens.common.ThemeFilePicker
import com.number869.telemone.ui.screens.editor.ThemeDisplaySettings
import com.number869.telemone.utils.assetFoldersThemeHash
import com.number869.telemone.utils.toastChannel
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File

fun fileKitThemeFilePicker(): ThemeFilePicker = ThemeFilePicker {
    val file = FileKit.openFilePicker(
        type = FileKitType.File(extensions = setOf("attheme"))
    ) ?: return@ThemeFilePicker null

    ThemeFileSource {
        flow {
            file.readBytes().inputStream().bufferedReader().use { reader ->
                var line = reader.readLine()
                while (line != null) {
                    emit(line)
                    line = reader.readLine()
                }
            }
        }
    }
}

fun themeExporter(context: Context) = ThemeExporter { fileName, content ->
    withContext(Dispatchers.IO) {
        File(context.cacheDir, fileName).writeText(content)
    }
    val uri = FileProvider.getUriForFile(
        context.applicationContext,
        "${context.packageName}.provider",
        File(context.cacheDir, fileName)
    )
    val intent = Intent(Intent.ACTION_SEND).apply {
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        type = "*/attheme"
        putExtra(Intent.EXTRA_STREAM, uri)
    }
    context.startActivity(Intent.createChooser(intent, fileName))
}


fun stockThemeHashSource(context: Context) = StockThemeHashSource { light ->
    withContext(Dispatchers.IO) {
        assetFoldersThemeHash(light, context)
    }
}

class AppSettings(
    private val settingsManager: SettingsManager
) : OnboardingSettings, ThemeDisplaySettings, StockThemeUpdateSettings {

    private val agreedToConditions =
        Setting("userAgreedToV1OnWelcomeScreen", false, targetSettingsManager = settingsManager)

    private val savedThemeDisplayType =
        Setting("savedThemeItemDisplayType", 1, targetSettingsManager = settingsManager)

    private val lastAcceptedStockThemeHashDark = NullableSetting(
        "lastAcceptedStockThemeHashDark",
        String::class,
        targetSettingsManager = settingsManager
    )

    private val lastAcceptedStockThemeHashLight = NullableSetting(
        "lastAcceptedStockThemeHashLight",
        String::class,
        targetSettingsManager = settingsManager
    )

    private val lastDeclinedStockThemeHashDark =
        Setting("lastDeclinedStockThemeHashDark", "", targetSettingsManager = settingsManager)

    private val lastDeclinedStockThemeHashLight =
        Setting("lastDeclinedStockThemeHashLight", "", targetSettingsManager = settingsManager)

    override fun hasAgreedToConditions() = agreedToConditions.get()
    override suspend fun setAgreedToConditions(agreed: Boolean) = agreedToConditions.set(agreed)

    override fun displayType() = savedThemeDisplayType.get()
    override suspend fun setDisplayType(id: Int) = savedThemeDisplayType.set(id)

    override fun acceptedHashLight() = lastAcceptedStockThemeHashLight.get()
    override fun acceptedHashDark() = lastAcceptedStockThemeHashDark.get()
    override fun declinedHashLight() = lastDeclinedStockThemeHashLight.get()
    override fun declinedHashDark() = lastDeclinedStockThemeHashDark.get()
    override suspend fun setAcceptedHash(light: Boolean, hash: String) {
        if (light) lastAcceptedStockThemeHashLight.set(hash)
        else lastAcceptedStockThemeHashDark.set(hash)
    }

    override suspend fun setDeclinedHash(light: Boolean, hash: String) {
        if (light) lastDeclinedStockThemeHashLight.set(hash)
        else lastDeclinedStockThemeHashDark.set(hash)
    }
}

class BuildInfoImpl(private val context: Context) : BuildInfo {
    override val versionString = context.packageManager
        .getPackageInfo(context.packageName, 0)
        .versionName
        ?: context.getString(R.string.cant_get_version_name_for_some_reason)
}

fun MainActivity.customTabsLinkHandler(): LinkHandler = LinkHandler { uri ->
    CustomTabsIntent.Builder()
        .build()
        .launchUrl(this, uri.toUri())
}

suspend fun collectToasts(context: Context) {
    toastChannel.collect {
        Toast
            .makeText(
                context,
                context.getString(it.first, *it.second),
                Toast.LENGTH_LONG
            )
            .show()
    }
}
