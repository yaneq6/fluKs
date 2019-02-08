package io.scheme.feature.session

import io.scheme.Core
import io.scheme.util.di.provide
import io.scheme.util.di.provider.singleton
import io.scheme.util.core.*
import io.scheme.feature.session.action.SignIn
import io.scheme.feature.session.action.SignOut

object Session {

    interface Effect : CoreEffect

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

    interface Component {
        val signIn: SignIn.Interactor
        val signOut: SignOut.Interactor
        val sessionStore: Store<Effect, State>
    }

    class Module(
        data: Core.Component
    ) :
        Component,
        Core.Component by data {

        override val signOut by provide {
            SignOut.Interactor(api)
        }

        override val signIn by provide {
            SignIn.Interactor(api)
        }

        override val sessionStore by provide(singleton()) { name ->
            Store(SimpleStateHolder(
                state = State(),
                repository = sharedPrefsRepository(
                    name = name,
                    context = context
                )
            ))
        }
    }
}