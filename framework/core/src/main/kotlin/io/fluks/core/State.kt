package io.fluks.core

import io.fluks.common.Effect
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