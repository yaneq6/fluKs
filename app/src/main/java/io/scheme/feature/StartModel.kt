package io.scheme.feature

import io.scheme.util.core.Action
import io.scheme.util.core.Dispatch
import io.scheme.util.core.Store
import io.scheme.feature.login.view.NavigateLogin
import io.scheme.feature.scheme.view.NavigateScheme
import io.scheme.feature.session.Session

class StartModel(
    private val sessionStore: Store<Session.Effect, Session.State>,
    dispatch: Dispatch<Action>
) : Dispatch<Action> by dispatch {

    fun start() = dispatch(navigate())

    private fun navigate() = let {
        if (isSignedIn) ::NavigateScheme
        else ::NavigateLogin
    }.invoke(true)

    private val isSignedIn get() = sessionStore.state.isSignedIn
}