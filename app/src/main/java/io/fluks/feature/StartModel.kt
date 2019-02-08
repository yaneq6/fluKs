package io.fluks.feature

import io.fluks.util.core.Action
import io.fluks.util.core.Dispatch
import io.fluks.util.core.Store
import io.fluks.feature.login.view.NavigateLogin
import io.fluks.feature.scheme.view.NavigateScheme
import io.fluks.feature.session.Session

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