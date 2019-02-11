package io.fluks.core

import io.fluks.common.unsafe
import io.reactivex.disposables.Disposable
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

    interface Component : Dispatcher.Component
}

interface UI {
    interface Component<DataBinding> : Platform.Component {
        val layoutId: Int
        val disposable: Disposable
        fun DataBinding.bind()
    }
}