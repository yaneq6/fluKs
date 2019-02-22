package io.fluks.base

import java.io.Serializable

interface Event : Serializable {

    object Success: Event

    object Unhandled: Event

    data class Error(
        val event: Event,
        val throwable: Throwable
    ) : Event

    data class More(val events: List<Event>): Event {
        constructor(vararg events: Event?) : this(events.filterNotNull())
    }

    data class Lifecycle(
        val event: Event,
        val startedAt: Long,
        val isRunning: Boolean
    ) {
        companion object {
            val Empty = Lifecycle(
                event = Event.Unhandled,
                isRunning = false,
                startedAt = 0
            )
        }
    }
}


interface Effect : Event

interface Action : Event {

    interface Async : Action

    interface Navigate<Context>: Action {

        fun Context.navigate()

        val finishCurrent get() = false
    }
}

typealias BaseEffect = Effect

interface Dispatch<in T : Event> {
    infix fun Any.dispatch(any: T?)

    object Void : Dispatch<Event> {
        override fun Any.dispatch(any: Event?) = Unit
    }

    interface Component {
        val dispatch: Dispatch<Event>
    }
}

interface DispatchDelegate<in E : Event> : Dispatch<E> {
    val dispatch: Dispatch<E>

    override fun Any.dispatch(any: E?) = dispatch.run {
        this@dispatch.dispatch(any)
    }
}