package io.fluks.core.android

import android.content.Context
import com.google.gson.GsonBuilder
import io.fluks.core.StateHolder

inline fun <reified State : Any> sharedPrefsRepository(
    name: String,
    context: Context
) = SharedPrefsRepository(
    name = name,
    context = context,
    type = State::class.java
)

class SharedPrefsRepository<State : Any>(
    override val name: String,
    context: Context,
    private val type: Class<State>
) : StateHolder.Repository<State> {

    private val sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    private val gson = GsonBuilder().create()

    override var state: State?
        get() = sharedPreferences
            .getString(KEY, null)
            ?.let { gson.fromJson(it, type) }
        set(value) = sharedPreferences.edit()
            .putString(KEY, gson.toJson(value))
            .apply()


    private companion object {
        const val KEY = "state"
    }
}