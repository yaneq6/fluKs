package io.bricks.cache.sharedpreferences

import android.content.SharedPreferences
import io.bricks.cache.core.CachedProperty
import io.bricks.cache.core.createCache
import io.bricks.serial.core.JsonSource
import io.bricks.serial.core.StringJsonFactory
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test

class SharedPreferencesCachedPropertyTest {

    private val sharedPreferences = mockk<SharedPreferences>(relaxed = true)

    private val jsonFactory = mockk<StringJsonFactory>()

    private val property = SharedPreferencesCachedProperty(
        jsonFactory = jsonFactory,
        sharedPreferences = sharedPreferences,
        key = KEY,
        type = String::class
    )

    init {
        every { sharedPreferences.edit().apply() } just Runs
        every { sharedPreferences.getString(KEY, DEFAULT_VALUE) } returns JSON_VALUE
        every { jsonFactory.run { VALUE.serialize() } } returns SOURCE
        every { jsonFactory.run { SOURCE.deserialize<String>() } } returns VALUE
    }

    @Test
    fun getValue() {
        assertEquals(VALUE, property.value)
        assertEquals(VALUE, property.value)

        verifySharedPrefsPut(exactly = 0)
        verifyJsonFactorySerialize(exactly = 0)
        verifySharedPrefsGet(exactly = 1)
        verifyJsonFactoryDeserialize(exactly = 1)
    }

    @Test
    fun setValue() {
        property.value = VALUE to 0
        assertEquals(VALUE, property.value)

        verifySharedPrefsPut(exactly = 1)
        verifyJsonFactorySerialize(exactly = 1)
        verifySharedPrefsGet(exactly = 0)
        verifyJsonFactoryDeserialize(exactly = 0)
    }

    private fun verifySharedPrefsPut(exactly: Int) = verify(exactly = exactly) { sharedPreferences.edit().putString(any(), any()) }
    private fun verifySharedPrefsGet(exactly: Int) = verify(exactly = exactly) { sharedPreferences.getString(KEY, DEFAULT_VALUE) }
    private fun verifyJsonFactorySerialize(exactly: Int) = verify(exactly = exactly) { jsonFactory.run { VALUE.serialize() } }
    private fun verifyJsonFactoryDeserialize(exactly: Int) = verify(exactly = exactly) { jsonFactory.run { SOURCE.deserialize<String>() } }

    private companion object {
        const val KEY = "test"
        const val VALUE = "test"
        const val JSON_VALUE = "\"$VALUE\""
        val SOURCE = JsonSource(String::class, JSON_VALUE)
        val DEFAULT_VALUE: String? = null
    }
}