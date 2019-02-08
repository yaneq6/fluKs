package io.scheme.util.debug

import com.google.gson.GsonBuilder
import io.scheme.util.debug.dispatch.DebugEvents
import io.scheme.feature.session.action.SignIn
import org.junit.Assert
import org.junit.Test

class ActionAdapterFactoryTest {

    val gson = GsonBuilder()
        .registerTypeAdapterFactory(ActionTypAdapter.Factory)
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
            "{\"actions\":[],\"savedActions\":[{\"type\":\"io.scheme.feature.session.input.SignIn\",\"value\":\"{\\\"login\\\":\\\"login\\\",\\\"password\\\":\\\"password\\\"}\"}]}"
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


fun assertEquals(expected: Any?, actual: Any?) = Assert.assertEquals(expected, actual)
