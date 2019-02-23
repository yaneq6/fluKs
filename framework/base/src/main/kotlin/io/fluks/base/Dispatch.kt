package io.fluks.base

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