package io.scheme.util.android

import io.scheme.util.android.databinding.ObservableProperty
import org.junit.Assert.assertEquals
import org.junit.Test


class ExtensionsKtTest {

    @Test
    fun testOnPropertyChangedCallback() {
        val message = "test"

        var result: String? = "invalid"

        val callback = onPropertyChangedCallback<Unit, String>(Unit) { result = it }

        ObservableProperty(message).apply {

            addOnPropertyChangedCallback(callback)
            set("")
            set(null)
        }


        assertEquals(message, result)
    }
}