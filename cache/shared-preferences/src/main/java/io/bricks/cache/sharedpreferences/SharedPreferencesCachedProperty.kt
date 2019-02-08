package io.bricks.cache.sharedpreferences

import android.content.SharedPreferences
import io.bricks.cache.core.CachedProperty
import io.bricks.serial.core.JsonSource
import io.bricks.serial.core.StringJsonFactory
import io.bricks.serial.core.invoke
import kotlin.reflect.KClass

class SharedPreferencesCachedProperty<T: Any>(
    private val sharedPreferences: SharedPreferences,
    private val jsonFactory: StringJsonFactory,
    private val key: String,
    private val type: KClass<out T>,
    defaultValue: T? = null
): CachedProperty<Pair<T, Long>> {

    override var value: Pair<T, Long>? = deserialized ?: defaultValue?.let { it to 0L }
        get() {
            return if (field != null) field
            else deserialized.also { field = it }
        }
        set(value) {
            if (field != null) deserialized = value
            field = value

        }

    private var deserialized: Pair<T, Long>?
        get() = jsonFactory {
            serializedValue?.let { JsonSource(type, it).deserialize<T>() to serializedTimestamp }
        }
        set(value) = jsonFactory {
            serializedValue = value?.run { first.serialize().source }
            serializedTimestamp = value?.second ?: 0
        }

    private var serializedValue: String?
        get() = sharedPreferences.getString(key, null)
        set(value) = sharedPreferences.edit().putString(key, value).apply()

    private var serializedTimestamp: Long
        get() = sharedPreferences.getLong("${key}Timestamp", 0)
        set(value) = sharedPreferences.edit().putLong("${key}Timestamp", value).apply()

}