package io.fluks.feature.scheme

import io.fluks.util.di.DependenciesAccumulator
import io.fluks.util.di.provide
import io.fluks.util.di.provider.weakSingleton
import io.fluks.util.core.Reduce
import io.fluks.util.core.CoreEffect
import io.fluks.util.core.SimpleStateHolder
import io.fluks.util.core.Store

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