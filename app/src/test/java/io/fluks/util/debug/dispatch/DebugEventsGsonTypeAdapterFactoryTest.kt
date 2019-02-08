package io.fluks.util.debug.dispatch

import com.google.gson.GsonBuilder
import io.fluks.feature.session.action.SignIn
import org.junit.Assert.assertEquals
import org.junit.Test

class DebugEventsGsonTypeAdapterFactoryTest {
    val gson = GsonBuilder()
        .registerTypeAdapterFactory(DebugEvents.GsonTypeAdapter.Factory)
        .create()

    @Test
    fun toJson() {
        val expected = STRING_JSON
        val actual = gson.toJson(OBJECT_JSON)
        assertEquals(expected, actual)
    }

    @Test
    fun fromJson() {
        val expected = OBJECT_JSON
        val actual = gson.fromJson(STRING_JSON, DebugEvents::class.java)
        assertEquals(expected, actual)
    }

    companion object {
        const val STRING_JSON =
            "{\"actions\":[],\"savedActions\":[{\"type\":\"io.fluks.feature.session.input.SignIn\",\"value\":\"{\\\"login\\\":\\\"login\\\",\\\"password\\\":\\\"password\\\"}\"}]}"
        val OBJECT_JSON = DebugEvents(
            actions = listOf(),
            savedActions = listOf(
                SignIn(
                    login = "login",
                    password = "password"
                )
            )
        )
    }
}