package io.fluks

import android.content.SharedPreferences

class SharedPreferencesMock : SharedPreferences {

    private val map = mutableMapOf<String, Any>()

    private val editor = Editor()

    override fun contains(key: String): Boolean = map.contains(key)

    override fun getAll(): Map<String, *> = map

    override fun registerOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener?
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unregisterOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener?
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun edit(): SharedPreferences.Editor = editor

    override fun getBoolean(key: String, defValue: Boolean): Boolean = map[key] as? Boolean ?: defValue

    override fun getInt(key: String, defValue: Int): Int = map[key] as? Int ?: defValue

    override fun getLong(key: String, defValue: Long): Long = map[key] as? Long ?: defValue

    override fun getFloat(key: String, defValue: Float): Float = map[key] as? Float ?: defValue

    override fun getStringSet(key: String, defValues: MutableSet<String>?): MutableSet<String>? = map[key] as? MutableSet<String> ?: defValues

    override fun getString(key: String, defValue: String?): String? = map[key] as? String? ?: defValue

    private inner class Editor : SharedPreferences.Editor {

        override fun clear(): SharedPreferences.Editor = apply { map.clear() }

        override fun remove(key: String): SharedPreferences.Editor = apply {
            map.remove(key)
        }

        override fun putLong(key: String, value: Long): SharedPreferences.Editor = apply {
            map[key] = value
        }

        override fun putInt(key: String, value: Int): SharedPreferences.Editor = apply {
            map[key] = value
        }


        override fun putBoolean(key: String, value: Boolean): SharedPreferences.Editor = apply {
            map[key] = value
        }

        override fun putStringSet(key: String, values: MutableSet<String>): SharedPreferences.Editor = apply {
            map[key] = values
        }

        override fun putFloat(key: String, value: Float): SharedPreferences.Editor = apply {
            map[key] = value
        }

        override fun putString(key: String, value: String): SharedPreferences.Editor = apply {
            map[key] = value
        }

        override fun commit(): Boolean = true

        override fun apply() = Unit
    }
}