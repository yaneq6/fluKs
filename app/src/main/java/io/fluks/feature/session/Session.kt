package io.fluks.feature.session

import io.fluks.Core
import io.fluks.common.BaseEffect
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
        core: Core.Component
    ) :
        Component,
        Core.Component by core {

        override val signOut by provide {
            SignOut.Interactor(api)
        }

        override val signIn by provide {
            SignIn.Interactor(api)
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