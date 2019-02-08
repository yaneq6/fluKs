@file:Suppress("UNCHECKED_CAST")

package io.scheme.util.core

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.subjects.PublishSubject
import io.scheme.util.di.Dependency

fun <E : Effect, State : Any> Store<E, State>.subscribe(onNext: State.(Effect) -> Unit) =
    observable().subscribe { onNext(it.first, it.second) }!!

fun <E : Effect, State : Any> Store<E, State>.observable(): Observable<Pair<State, Effect>> =
    Observable.unsafeCreate(this)!!

fun <T> ObservableSource<T>.observable() = Observable.unsafeCreate(this)!!

interface Store<E : Effect, State : Any> :
    Dependency,
    ObservableSource<Pair<State, Effect>> {

    val state: State
    val error: Observable<Pair<State, Event.Error>>

    override fun snapshot() = state

    operator fun invoke(effect: E)

    companion object {
        operator fun <E : Effect, State : Reduce<E, State>> invoke(
            stateHolder: StateHolder<State>
        ): Store<E, State> = DefaultStore(
            stateHolder = stateHolder
        )
    }
}

private class DefaultStore<E : Effect, State : Reduce<E, State>>(
    private val stateHolder: StateHolder<State>
) : Store<E, State>,
    ObservableSource<Pair<State, Effect>> by stateHolder {

    override val name: String get() = stateHolder.name

    override val state by stateHolder

    override val error: Observable<Pair<State, Event.Error>> get() = errorSubject

    private val errorSubject = PublishSubject.create<Pair<State, Event.Error>>()

    override fun snapshot() = state

    override fun clear() {}

    override fun invoke(event: E) {
        state(event)?.let { newState ->
            stateHolder.put(newState, event)
        } ?: errorSubject.onNext(
            state to Event.Error(
                event = event,
                throwable = IllegalArgumentException(
                    "Cannot reduce effect: $event, of type: ${event::class.java}")
            )
        )
    }
}

inline fun <reified T : Effect> store(
    noinline provide: () -> Store<T, *>
): StoreProvider = DefaultStoreProvider(
    matches = { effect -> effect is T },
    provide = provide as () -> Store<Effect, *>
)

interface StoreProvider {
    fun get(): Store<Effect, *>
    fun safe(effect: Effect): Store<Effect, *>?
}

class DefaultStoreProvider(
    private val provide: () -> Store<Effect, *>,
    private val matches: (Effect) -> Boolean
) : StoreProvider {

    override fun get(): Store<Effect, *> = provide()

    override fun safe(effect: Effect): Store<Effect, *>? = if (matches(effect)) get() else null
}


