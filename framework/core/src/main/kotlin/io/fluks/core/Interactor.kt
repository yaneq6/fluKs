package io.fluks.core

import io.fluks.common.Event
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
inline fun <reified A : Action.Async> interactor(
    noinline provide: () -> (A) -> Event
): Pair<KClass<*>, () -> (Action.Async) -> Event> =
    (A::class as KClass<*>) to provide as () -> (Action.Async) -> Event


interface Interactor<in A : Action, out E : Event> : (A) -> E

interface AbstractInteractor<in A : Action, out E : Event> : Interactor<A, E> {
    override operator fun invoke(action: A): E
}