package com.number869.telemone.data

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.number869.telemone.shared.utils.inject
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import kotlin.reflect.KClass

fun settingsRealm(key: String, encryptionKey: ByteArray? = null) = Realm.open(
    RealmConfiguration.Builder(settingsRealmSchemas).run {
        name(key)
        if (encryptionKey != null) encryptionKey(encryptionKey)
        build()
    }
)

class SettingsManager(private val database: Realm = settingsRealm("default")) {
    private val scope = CoroutineScope(Dispatchers.IO)

    fun <T : Any> set(key: String, value: T?, type: KClass<out T>) = scope.launch {
        database.write {
            copyToRealm(asSettingType(key, value, type), UpdatePolicy.ALL)
        }
    }

    fun <T : Any> setBlocking(key: String, value: T?, type: KClass<out T>) {
        database.writeBlocking {
            copyToRealm(asSettingType(key, value, type), UpdatePolicy.ALL)
        }
    }

    fun <T : Any> get(key: String, defaultValue: T? = null, type: KClass<out T>): T? {
        val realmSettingContainer = database
            .query(getSettingType(type),"key = '$key'")
            .first()
            .find()

        return realmSettingContainer
            ?.let { getValueFromSettingContainer(type, it) as T? }
            ?: defaultValue
    }

    fun <T : Any> getAsStateFlow(key: String, defaultValue: T?, type: KClass<out T>) = database
        .query(getSettingType(type),"key = '$key'")
        .first()
        .asFlow()
        .flowOn(Dispatchers.Default)
        .map() { queried ->
            queried.obj
                ?.let { getValueFromSettingContainer(type, it) as T? }
                ?: defaultValue
        }
        .stateIn(
            scope = CoroutineScope(Dispatchers.Default),
            started = SharingStarted.WhileSubscribed(),
            initialValue = get(key, defaultValue, type)
        )
}

private fun <T : Any> getValueFromSettingContainer(type: KClass<T>, obj: RealmObject) = when(type) {
    Boolean::class -> (obj as BooleanSetting).value
    String::class -> (obj as StringSetting).value
    Int::class -> (obj as IntSetting).value
    Float::class -> (obj as FloatSetting).value
    Long::class -> (obj as LongSetting).value
    Double::class -> (obj as DoubleSetting).value
    else -> error("unsupported setting class")
}
private fun <T : Any> getSettingType(type: KClass<T>) = when(type) {
    Boolean::class -> BooleanSetting::class
    String::class -> StringSetting::class
    Int::class -> IntSetting::class
    Float::class -> FloatSetting::class
    Long::class -> LongSetting::class
    Double::class -> DoubleSetting::class
    else -> error("unsupported setting class")
}

private fun <T : Any> asSettingType(targetKey: String, newValue: T?, type: KClass<out T>) = when(type) {
    Boolean::class -> BooleanSetting().apply {
        key = targetKey
        value = newValue as Boolean?
    }
    String::class -> StringSetting().apply {
        key = targetKey
        value = newValue as String?
    }
    Int::class -> IntSetting().apply {
        key = targetKey
        value = newValue as Int?
    }
    Float::class -> FloatSetting().apply {
        key = targetKey
        value = newValue as Float?
    }
    Long::class -> LongSetting().apply {
        key = targetKey
        value = newValue as Long?
    }
    Double::class -> DoubleSetting().apply {
        key = targetKey
        value = newValue as Double?
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
data class Setting<T : Any>(
    private val key: String,
    private val defaultValue: T,
    private val targetSettingsManager: SettingsManager = inject()
) {
    fun set(newValue: T) = targetSettingsManager
        .set(key, newValue, defaultValue::class)
    fun setBlocking(newValue: T) = targetSettingsManager
        .setBlocking(key, newValue, defaultValue::class)
    fun get() = targetSettingsManager
        .get(key, defaultValue, defaultValue::class) ?: defaultValue
    fun getAsStateFlow() = targetSettingsManager
        .getAsStateFlow(key, defaultValue, defaultValue::class) as StateFlow<T>
    @Composable
    fun asState() = getAsStateFlow().collectAsState()
}

@androidx.compose.runtime.Immutable
data class NullableSetting<T : Any>(
    private val key: String,
    private val type: KClass<out T>,
    private val defaultValue: T? = null,
    private val targetSettingsManager: SettingsManager = inject(),
) {
    fun set(newValue: T?) = targetSettingsManager.set(key, newValue, type)
    fun setBlocking(newValue: T?) = targetSettingsManager
        .setBlocking(key, newValue, type)
    fun get() = targetSettingsManager.get(key, defaultValue, type)
    fun getAsStateFlow() = targetSettingsManager.getAsStateFlow(key, defaultValue, type)
    @Composable
    fun asState() = getAsStateFlow().collectAsState()
}

fun settingsManagerInitializer() = SettingsManager()

