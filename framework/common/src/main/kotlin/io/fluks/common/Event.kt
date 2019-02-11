package io.fluks.common

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