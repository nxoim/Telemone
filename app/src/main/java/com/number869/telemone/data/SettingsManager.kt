package com.number869.telemone.data

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.number869.telemone.shared.utils.inject
import com.tencent.mmkv.MMKV
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import javax.annotation.concurrent.Immutable

fun SettingsRealm(key: String, encryptionKey: ByteArray? = null) = Realm.open(
    RealmConfiguration.Builder(setOf(RealmSetting::class)).run {
        name(key)
        if (encryptionKey != null) encryptionKey(encryptionKey)
        build()
    }
)

class SettingsManager(
    private val database: Realm = SettingsRealm("default")
//        Realm.open(
//        RealmConfiguration.create(setOf(RealmSetting::class))
//    )
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    fun <T> set(key: String, value: T?) = scope.launch {
        database.write {
            runCatching { delete(this.query(RealmSetting::class, "key = '$key'")) }
            copyToRealm(
                RealmSetting().apply {
                    this.key = key
                    stringValue = value as? String
                    booleanValue = value as? Boolean
                    intValue = value as? Int
                    longValue = value as? Long
                    floatValue = value as? Float
                    doubleValue = value as? Double
                }
            )
        }
    }

    fun <T> setBlocking(key: String, value: T?) {
        database.writeBlocking {
            runCatching { delete(this.query(RealmSetting::class, "key = '$key'")) }
            copyToRealm(
                RealmSetting().apply {
                    this.key = key
                    stringValue = value as? String
                    booleanValue = value as? Boolean
                    intValue = value as? Int
                    longValue = value as? Long
                    floatValue = value as? Float
                    doubleValue = value as? Double
                }
            )
        }
    }

    fun <T> get(key: String, defaultValue: T? = null): T? {
        val realmSettingContainer = database
            .query<RealmSetting>("key = '$key'")
            .first()
            .find()

        val settingFromStorage = realmSettingContainer?.let { getSupportedSettingsValue(it, defaultValue) }

        return when {
            settingFromStorage != null -> settingFromStorage
            else -> defaultValue
        }
    }

    fun <T> getAsStateFlow(key: String, defaultValue: T) = database
        .query<RealmSetting>("key = '$key'")
        .first()
        .asFlow()
        .flowOn(Dispatchers.IO)
        .map() { queried ->
            queried.obj
                ?.let { getSupportedSettingsValue(it, defaultValue) }
                ?: defaultValue
        }
        .stateIn(
            scope = CoroutineScope(Dispatchers.Default),
            started = SharingStarted.WhileSubscribed(),
            initialValue = get<T>(key, defaultValue) ?: defaultValue
        )
}

// making this private causes crashes
open class RealmSetting : RealmObject {
    @PrimaryKey var _id = ObjectId()
    var key: String = ""
    var stringValue: String? = null
    var booleanValue: Boolean? = null
    var intValue: Int? = null
    var longValue: Long? = null
    var floatValue: Float? = null
    var doubleValue: Double? = null
}

@Immutable
data class Setting<T>(
    private val key: String,
    private val defaultValue: T,
    private val settingsManager: SettingsManager = inject()
) {
    fun set(newValue: T) = settingsManager.set(key, newValue)
    fun setBlocking(newValue: T) = settingsManager.setBlocking(key, newValue)
    fun get() = settingsManager.get(key, defaultValue) ?: defaultValue
    fun getAsStateFlow() = settingsManager.getAsStateFlow(key, defaultValue)
    @Composable
    fun asState() = getAsStateFlow().collectAsState()
}

@Immutable
data class NullableSetting<T>(
    private val key: String,
    private val defaultValue: T? = null,
    private val settingsManager: SettingsManager = inject(),
) {
    fun set(newValue: T?) = settingsManager.set(key, newValue)
    fun setBlocking(newValue: T?) = settingsManager.setBlocking(key, newValue)
    fun get() = settingsManager.get(key, defaultValue)
    fun getAsStateFlow() = settingsManager.getAsStateFlow(key, defaultValue)
    @Composable
    fun asState() = getAsStateFlow().collectAsState()
}

private fun <T> getSupportedSettingsValue(realmSettingContainer: RealmSetting, value: T): T = when (value) {
    is String? -> (realmSettingContainer.stringValue) as T
    is Boolean? -> (realmSettingContainer.booleanValue) as T
    is Int? -> (realmSettingContainer.intValue) as T
    is Long? -> (realmSettingContainer.longValue) as T
    is Float? -> (realmSettingContainer.floatValue) as T
    is Double? -> (realmSettingContainer.doubleValue) as T
    else -> throw IllegalArgumentException("Requested value for ${realmSettingContainer.key} is not supported")
}

fun InstanceDeclaratorScope.settingsManagerInitializer(context: Context) {
    single { SettingsManager() } // init before migration

    val preferences = context.getSharedPreferences(
        "AppPreferences.Settings",
        Context.MODE_PRIVATE
    )

    val mmkv by lazy { MMKV.mmkvWithID("storageForStockThemeComparison") }

    if (preferences.all.isNotEmpty()) {
        val agreedToConditions = preferences.getBoolean(
            "userAgreedToV1OnWelcomeScreen",
            false
        )
        val savedThemeDisplayType = preferences.getString(
            "savedThemeItemDisplayType",
            "1"
        )?.toInt()

        AppSettings.agreedToConditions.setBlocking(agreedToConditions)
        AppSettings.savedThemeDisplayType.setBlocking(savedThemeDisplayType ?: 1)

        preferences.edit().clear().apply()
    }

    if (mmkv.allKeys()?.isNotEmpty() == true) {
        AppSettings.lastDeclinedStockThemeHashLight.setBlocking(
            "well the theme definetely changed"
        )

        AppSettings.lastDeclinedStockThemeHashDark.setBlocking(
            "well the theme definetely changed"
        )

        AppSettings.lastAcceptedStockThemeHashLight.setBlocking(
            ""
        )

        AppSettings.lastAcceptedStockThemeHashDark.setBlocking(
            ""
        )

        mmkv.clear()
    }
}