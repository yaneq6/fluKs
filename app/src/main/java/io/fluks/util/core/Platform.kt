package io.fluks.util.core

import io.fluks.util.core.util.unsafe
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

        interface Component : Dispatcher.Component
    }

    data class OnTop<Context>(
        val context: WeakReference<Context>
    ) : Effect

    interface Component<
        DataBinding,
        ViewModel,
        State : Reduce<*, State>> :

        Platform.State.Component {

        val store: Store<*, State>
        val layoutId: Int
        val viewModel: ViewModel
        val bind: DataBinding.(ViewModel) -> Unit
    }
}