package io.fluks.base

import java.io.Serializable

interface Event : Serializable {

    object Empty: Event

    object Unhandled: Event

    object Success: Event

    data class Error(
        val event: Event,
        val throwable: Throwable
    ) : Event

    data class More(val events: List<Event>): Event {
        constructor(vararg events: Event?) : this(events.filterNotNull())
    }

    data class Status(
        val event: Event,
        val startedAt: Long,
        val isRunning: Boolean
    ) : Event {
        companion object {
            val Empty = Status(
                event = Event.Empty,
                isRunning = false,
                startedAt = 0
            )
        }
    }
}

typealias BaseEffect = Effect

interface Effect : Event

interface Action : Event {

    interface Async : Action

    interface Navigate<Context>: Action {

        fun Context.navigate()
        val finishCurrent get() = false
    }
}