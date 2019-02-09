package io.fluks.feature.scheme

import io.fluks.di.DependenciesAccumulator
import io.fluks.di.provide
import io.fluks.di.provider.weakSingleton
import io.fluks.core.Reduce
import io.fluks.core.CoreEffect
import io.fluks.core.SimpleStateHolder
import io.fluks.core.Store

object Scheme {

    class State : Reduce<Effect, State>

    interface Effect : CoreEffect

    interface Component {
        val schemeStore: Store<Effect, State>
    }

    class Module(
        mainStore: DependenciesAccumulator
    ) : Component,
        DependenciesAccumulator by mainStore {

        override val schemeStore: Store<Effect, State> by provide(weakSingleton()) {
            Store(SimpleStateHolder(State()))
        }
    }
}