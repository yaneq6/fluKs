package io.fluks.util.core

import android.content.Context
import com.google.gson.GsonBuilder
import io.reactivex.ObservableSource
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

interface StateHolder<State : Any> :
    ReadOnlyProperty<Any, State>,
    ObservableSource<Pair<State, Effect>> {

    val name: String

    fun put(state: State, event: Effect)

    interface Repository<State : Any> {
        val name: String
        var state: State?
    }
}


class SimpleStateHolder<State : Any>(
    private var state: State,
    private val subject: Subject<Pair<State, Effect>> = BehaviorSubject.create(),
    private val repository: StateHolder.Repository<State>? = null
) :
    StateHolder<State>,
    ObservableSource<Pair<State, Effect>> by subject {

    override val name: String get() = repository?.name ?: ""

    init {
        state = repository?.measureRead() ?: state
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): State = state

    override fun put(state: State, event: Effect) {
        repository?.measureWrite(state)
        this.state = state
        subject.onNext(state to event)
    }
}


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

inline fun <reified State : Any> sharedPrefsRepository(
    name: String,
    context: Context
) = SharedPrefsRepository(
    name = name,
    context = context,
    type = State::class.java
)