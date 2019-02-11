package io.fluks.core

import io.fluks.common.unsafe
import java.lang.ref.WeakReference

interface Platform {

    interface Effect : CoreEffect

    data class State<Context>(
        val topContextRef: WeakReference<Context>
    ) : Reduce<Effect, State<Context>> {

        val topContext get() = topContextRef.get()

        override fun invoke(effect: Effect) = when (effect) {
            is OnTop<*> -> copy(topContextRef = effect.unsafe<OnTop<Context>>().context)
            else -> null
        }
    }

    data class OnTop<Context>(
        val context: WeakReference<Context>
    ) : Effect
}

