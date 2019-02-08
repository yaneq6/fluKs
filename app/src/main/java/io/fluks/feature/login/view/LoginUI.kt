package io.fluks.feature.login.view

import io.fluks.App
import io.fluks.R
import io.fluks.util.core.Platform
import io.fluks.util.di.provide
import io.fluks.util.di.provider.weakSingleton
import io.fluks.util.core.Store
import io.fluks.databinding.LoginBinding
import io.fluks.feature.session.Session

object LoginUI {

    interface Component :
        App.Component,
        Platform.Component<LoginBinding, LoginViewModel, Session.State>

    class Module(
        app: App.Component
    ) : Component,
        App.Component by app {

        override val store: Store<Session.Effect, Session.State> get() = sessionStore

        override val layoutId = R.layout.login

        override val bind = LoginBinding::setModel

        override val viewModel by provide(weakSingleton()) {
            LoginViewModel(
                dispatch = dispatcher,
                store = sessionStore
            )
        }
    }
}
