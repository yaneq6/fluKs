package io.fluks.feature.session

import io.fluks.Core
import io.fluks.base.Action
import io.fluks.base.BaseEffect
import io.fluks.base.Event
import io.fluks.core.AbstractInteractor
import io.fluks.core.Reduce
import io.fluks.core.SimpleStateHolder
import io.fluks.core.Store
import io.fluks.core.android.sharedPrefsRepository
import io.fluks.di.provide
import io.fluks.di.provider.singleton
import io.fluks.feature.session.action.SignIn
import io.fluks.feature.session.action.SignOut

object Session {

    interface Effect : BaseEffect
    interface Async: Action.Async

    data class State(
        val token: String? = null
    ) :
        Reduce<Effect, State> {

        val isSignedIn get() = token != null

        override fun invoke(effect: Effect) = when (effect) {
            is SignIn.Success -> copy(token = effect.token)
            is SignOut.Success -> copy(token = null)
            else -> null
        }
    }

    class Interactor(
        val api: (Action) -> Event
    ): AbstractInteractor<Async, Event> {
        override fun invoke(action: Async) = api(action)
    }

    interface Component {
        val sessionInteractor: Session.Interactor
        val sessionStore: Store<Effect, State>
    }

    class Module(
        core: Core.Component
    ) :
        Component,
        Core.Component by core {

        override val sessionInteractor by provide {
            Session.Interactor(api)
        }

        override val sessionStore by provide(singleton()) { name ->
            Store(
                SimpleStateHolder(
                    state = State(),
                    repository = sharedPrefsRepository(
                        name = name,
                        context = context
                    )
                )
            )
        }
    }
}