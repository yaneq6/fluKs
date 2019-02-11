package io.fluks.feature.login.view

import io.fluks.App
import io.fluks.R
import io.fluks.android.BaseActivity
import io.fluks.core.UI
import io.fluks.databinding.LoginBinding
import io.fluks.di.provide
import io.fluks.di.provider.weakSingleton

object LoginUI {

    interface Component :
        UI.Component<LoginBinding>,
        BaseActivity.Component

    class Module(
        app: App.Component
    ) : Component,
        App.Component by app {


        override val layoutId = R.layout.login

        override val disposable by provide(weakSingleton()) {
            LoginViewModel(
                dispatch = dispatch,
                store = sessionStore
            )
        }

        override fun LoginBinding.bind() {
            model = disposable
        }
    }
}
