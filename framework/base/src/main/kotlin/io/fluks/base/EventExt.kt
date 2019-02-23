package io.fluks.base

import io.reactivex.Observable
import kotlin.reflect.KClass

fun Observable<Event.Status>.isRunning(
    filterEvents: ((Event) -> Boolean)? = null
) = this
    .run { if (filterEvents != null) filter { filterEvents(it.event) } else this }
    .map { it.isRunning }!!

inline fun <
    reified T : Any
    > Observable<Event.Status>.isRunning(
    t1: KClass<T>? = null
) = isRunning { event ->
    event is T
}

inline fun <
    reified T1 : Any,
    reified T2 : Any
    > Observable<Event.Status>.isRunning(
    t1: KClass<T1>? = null,
    t2: KClass<T2>? = null
) = isRunning { event ->
    event is T1 || event is T2
}

inline fun <
    reified T1 : Any,
    reified T2 : Any,
    reified T3 : Any
    > Observable<Event.Status>.isRunning(
    t1: KClass<T1>? = null,
    t2: KClass<T2>? = null,
    t3: KClass<T3>? = null
) = isRunning { event ->
    event is T1 || event is T2 || event is T3
}

inline fun <
    reified T1 : Any,
    reified T2 : Any,
    reified T3 : Any,
    reified T4 : Any
    > Observable<Event.Status>.isRunning(
    t1: KClass<T1>? = null,
    t2: KClass<T2>? = null,
    t3: KClass<T3>? = null,
    t4: KClass<T4>? = null
) = isRunning { event ->
    event is T1 || event is T2 || event is T3 || event is T4
}

inline fun <
    reified T1 : Any,
    reified T2 : Any,
    reified T3 : Any,
    reified T4 : Any,
    reified T5 : Any
    > Observable<Event.Status>.isRunning(
    t1: KClass<T1>? = null,
    t2: KClass<T2>? = null,
    t3: KClass<T3>? = null,
    t4: KClass<T4>? = null,
    t5: KClass<T5>? = null
) = isRunning { event ->
    event is T1 || event is T2 || event is T3 || event is T4 || event is T5
}