package com.number869.telemone.data

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.number869.telemone.shared.utils.inject
import com.tencent.mmkv.MMKV
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
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

fun settingsRealm(key: String, encryptionKey: ByteArray? = null) = Realm.open(
    RealmConfiguration.Builder(settingsRealmSchemas).run {
        name(key)
        if (encryptionKey != null) encryptionKey(encryptionKey)
        build()
    }
)

class SettingsManager(private val database: Realm = settingsRealm("default")) {
    private val scope = CoroutineScope(Dispatchers.IO)

    fun <T> set(key: String, value: T?) = scope.launch {
        database.write {
            runCatching { delete(this.query(getSettingType(value), "key = '$key'")) }
            copyToRealm(asSettingType(key, value))
        }
    }

    fun <T> setBlocking(key: String, value: T?) {
        database.writeBlocking {
            runCatching { delete(this.query(getSettingType(value), "key = '$key'")) }
            copyToRealm(asSettingType(key, value))
        }
    }

    fun <T> get(key: String, defaultValue: T? = null): T? {
        val realmSettingContainer = database
            .query(getSettingType(defaultValue),"key = '$key'")
            .first()
            .find()

        return realmSettingContainer
            ?.let { getValueFromSettingContainer(defaultValue, it) as T }
            ?: defaultValue
    }

    fun <T> getAsStateFlow(key: String, defaultValue: T) = database
        .query(getSettingType(defaultValue),"key = '$key'")
        .first()
        .asFlow()
        .flowOn(Dispatchers.IO)
        .map() { queried ->
            queried.obj
                ?.let { getValueFromSettingContainer(defaultValue, it) as T }
                ?: defaultValue
        }
        .stateIn(
            scope = CoroutineScope(Dispatchers.Default),
            started = SharingStarted.WhileSubscribed(),
            initialValue = get<T>(key, defaultValue) ?: defaultValue
        )
}

private fun <T> getValueFromSettingContainer(type: T, obj: RealmObject) = when(type) {
    is Boolean? -> (obj as BooleanSetting).value
    is String? -> (obj as StringSetting).value
    is Int? -> (obj as IntSetting).value
    is Float? -> (obj as FloatSetting).value
    is Long? -> (obj as LongSetting).value
    is Double? -> (obj as DoubleSetting).value
    else -> error("unsupported setting class")
}
private fun <T> getSettingType(type: T) = when(type) {
    is Boolean? -> BooleanSetting::class
    is String? -> StringSetting::class
    is Int? -> IntSetting::class
    is Float? -> FloatSetting::class
    is Long? -> LongSetting::class
    is Double? -> DoubleSetting::class
    else -> error("unsupported setting class")
}

private fun <T> asSettingType(targetKey: String, newValue: T) = when(newValue) {
    is Boolean? -> BooleanSetting().apply {
        key = targetKey
        value = newValue
    }
    is String? -> StringSetting().apply {
        key = targetKey
        value = newValue
    }
    is Int? -> IntSetting().apply {
        key = targetKey
        value = newValue
    }
    is Float? -> FloatSetting().apply {
        key = targetKey
        value = newValue
    }
    is Long? -> LongSetting().apply {
        key = targetKey
        value = newValue
    }
    is Double? -> DoubleSetting().apply {
        key = targetKey
        value = newValue
    }
    else -> error("unsupported setting class")
}

class BooleanSetting : RealmObject {
    var _id = ObjectId()
    @PrimaryKey var key: String = ""
    var value: Boolean? = null
}

class StringSetting : RealmObject {
    var _id = ObjectId()
    @PrimaryKey var key: String = ""
    var value: String? = null
}

class IntSetting : RealmObject {
    var _id = ObjectId()
    @PrimaryKey var key: String = ""
    var value: Int? = null
}

class FloatSetting : RealmObject {
    var _id = ObjectId()
    @PrimaryKey var key: String = ""
    var value: Float? = null
}

class LongSetting : RealmObject {
    var _id = ObjectId()
    @PrimaryKey var key: String = ""
    var value: Long? = null
}

class DoubleSetting : RealmObject {
    var _id = ObjectId()
    @PrimaryKey var key: String = ""
    var value: Double? = null
}

val settingsRealmSchemas = setOf(
    BooleanSetting::class,
    StringSetting::class,
    IntSetting::class,
    FloatSetting::class,
    LongSetting::class,
    DoubleSetting::class
)

@androidx.compose.runtime.Immutable
data class Setting<T>(
    private val key: String,
    private val defaultValue: T,
    private val targetSettingsManager: SettingsManager = inject()
) {
    fun set(newValue: T) = targetSettingsManager.set(key, newValue)
    fun setBlocking(newValue: T) = targetSettingsManager.setBlocking(key, newValue)
    fun get() = targetSettingsManager.get(key, defaultValue) ?: defaultValue
    fun getAsStateFlow() = targetSettingsManager.getAsStateFlow(key, defaultValue)
    @Composable
    fun asState() = getAsStateFlow().collectAsState()
}

@androidx.compose.runtime.Immutable
data class NullableSetting<T>(
    private val key: String,
    private val defaultValue: T? = null,
    private val targetSettingsManager: SettingsManager = inject(),
) {
    fun set(newValue: T?) = targetSettingsManager.set(key, newValue)
    fun setBlocking(newValue: T?) = targetSettingsManager.setBlocking(key, newValue)
    fun get() = targetSettingsManager.get(key, defaultValue)
    fun getAsStateFlow() = targetSettingsManager.getAsStateFlow(key, defaultValue)
    @Composable
    fun asState() = getAsStateFlow().collectAsState()
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