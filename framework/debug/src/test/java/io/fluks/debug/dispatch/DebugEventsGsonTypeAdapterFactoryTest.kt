package io.fluks.debug.dispatch

import com.google.gson.GsonBuilder
import io.fluks.base.Event
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
            "{\"actions\":[],\"savedActions\":[{\"type\":\"io.fluks.debug.dispatch.TestDataClass\",\"value\":\"{\\\"foo\\\":\\\"foo\\\",\\\"bar\\\":\\\"bar\\\"}\"}]}"
        val OBJECT_JSON = DebugEvents(
            actions = listOf(),
            savedActions = listOf(
                TestDataClass(
                    foo = "foo",
                    bar = "bar"
                )
            )
        )
    }
}

data class TestDataClass(
    val foo: String,
    val bar: String
) : Event