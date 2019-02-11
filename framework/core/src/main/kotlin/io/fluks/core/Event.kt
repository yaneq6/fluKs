package io.fluks.core

import io.fluks.common.Event

interface Effect : Event

interface Action : Event {

    interface Async : Action

    interface Navigate<Context>: Action {

        fun Context.navigate()

        val finishCurrent get() = true
    }
}

typealias CoreEffect = Effect