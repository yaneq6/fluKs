package io.fluks.feature.scheme

import io.fluks.Core
import io.fluks.core.CoreEffect
import io.fluks.core.Reduce
import io.fluks.core.SimpleStateHolder
import io.fluks.core.Store
import io.fluks.di.provide
import io.fluks.di.provider.weakSingleton

object Scheme {

    class State : Reduce<Effect, State>

    interface Effect : CoreEffect

    interface Component {
        val schemeStore: Store<Effect, State>
    }

    class Module(
        core: Core.Component
    ) : Component,
        Core.Component by core {

        override val schemeStore: Store<Effect, State> by provide(weakSingleton()) {
            Store(SimpleStateHolder(State()))
        }
    }
}