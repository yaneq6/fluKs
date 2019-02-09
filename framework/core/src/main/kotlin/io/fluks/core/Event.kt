package io.fluks.core

import java.io.Serializable

interface Event : Serializable {

    object Success: Event

    object Unhandled: Event

    data class Error(
        val event: Event,
        val throwable: Throwable
    ) : Event

    data class More(val events: List<Event>): Event {
        constructor(vararg events: Event) : this(events.toList())
    }
}

interface Effect : Event

interface Action : Event {

    interface Async : Action

    interface Navigate<Context>: Action {

        fun Context.navigate()

        val finishCurrent get() = true
    }
}

typealias CoreEffect = Effect