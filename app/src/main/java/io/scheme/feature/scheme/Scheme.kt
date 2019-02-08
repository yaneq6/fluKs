package io.scheme.feature.scheme

import io.scheme.util.di.DependenciesAccumulator
import io.scheme.util.di.provide
import io.scheme.util.di.provider.weakSingleton
import io.scheme.util.core.Reduce
import io.scheme.util.core.CoreEffect
import io.scheme.util.core.SimpleStateHolder
import io.scheme.util.core.Store

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