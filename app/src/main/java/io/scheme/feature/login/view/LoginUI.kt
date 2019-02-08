package io.scheme.feature.login.view

import io.scheme.App
import io.scheme.R
import io.scheme.util.core.Platform
import io.scheme.util.di.provide
import io.scheme.util.di.provider.weakSingleton
import io.scheme.util.core.Store
import io.scheme.databinding.LoginBinding
import io.scheme.feature.session.Session

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
