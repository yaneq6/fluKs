package io.fluks.core

import io.fluks.base.Action
import io.fluks.base.Event
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
inline fun <reified A : Action.Async> interactor(
    noinline provide: () -> (A) -> Event
): Pair<KClass<*>, () -> (Action.Async) -> Event> =
    (A::class as KClass<*>) to provide as () -> (Action.Async) -> Event


interface Interactor<in A : Action, out E : Event> : (A) -> E

typealias BaseInteractor<A, E> = Interactor<A, E>