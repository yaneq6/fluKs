package io.fluks.debug.dispatch

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.fluks.base.BaseEffect
import io.fluks.base.Event
import io.fluks.core.Reduce
import io.fluks.core.StateHolder
import timber.log.Timber

data class DebugEvents(
    val actions: List<Event> = emptyList(),
    val savedActions: List<Event> = emptyList()
) :
    Reduce<DebugEvents.Effect, DebugEvents> {

    fun safe() = DebugEvents(
        actions = actions.filterNotNull(),
        savedActions = savedActions.filterNotNull()
    )

    interface Effect : BaseEffect

    data class Add(val event: Event) : Effect

    data class Save(val event: Event) : Effect

    override fun invoke(effect: Effect) = when (effect) {
        is Add -> copy(
            actions = listOf(effect.event) + actions.take(15)
        )
        is Save -> copy(
            savedActions = listOf(effect.event) + savedActions
        )
        else -> null
    }

    class Repository(
        override val name: String,
        context: Context
    ) : StateHolder.Repository<DebugEvents> {

        private val actionsType = DebugEvents::class.java

        private val gson = GsonBuilder()
            .registerTypeAdapterFactory(GsonTypeAdapter.Factory)
            .create()

        private val sharedPreferences = context.getSharedPreferences(
            SHARED_PREFS_NAME,
            Context.MODE_PRIVATE
        )

        override var state: DebugEvents?
            get() = sharedPreferences
                .getString(KEY_NAME, null)
                ?.let { gson.fromJson(it, actionsType).safe() }
            set(value) {
                sharedPreferences.edit()
                    .putString(KEY_NAME, gson.toJson(value))
                    .apply()
            }

        private companion object {
            const val SHARED_PREFS_NAME = "DebugActionsRepository"
            const val KEY_NAME = "actions"
        }
    }

    class GsonTypeAdapter(
        private val gson: Gson
    ) : TypeAdapter<Event>() {

        override fun write(out: JsonWriter, value: Event?) {
            value ?: return
            try {
                val type = value::class.java.name
                val json = gson.toJson(value)
                out.run {
                    beginObject()
                    name("type").value(type)
                    name("value").value(json)
                    endObject()
                }
            } catch (throwable: Throwable) {
                Timber.e(throwable)
            }
        }

        override fun read(input: JsonReader): Event? {
            try {
                var action: Event?
                input.run {
                    beginObject()
                    nextName()
                    val type = nextString()
                    nextName()
                    val value = nextString()
                    action = gson.fromJson<Event>(value, Class.forName(type))
                }
                return action
            } catch (throwable: ClassNotFoundException) {
                Timber.e(throwable)
                return null
            } finally {
                input.endObject()
            }
        }


        object Factory : TypeAdapterFactory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? =
                if (!type.rawType.isAssignableFrom(Event::class.java)) null
                else GsonTypeAdapter(gson) as? TypeAdapter<T>
        }
    }
}