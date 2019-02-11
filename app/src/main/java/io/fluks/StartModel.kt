package io.fluks

import io.fluks.core.Action
import io.fluks.common.Dispatch
import io.fluks.core.Store
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