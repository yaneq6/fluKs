package io.fluks.feature.login.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import io.fluks.App
import io.fluks.R
import io.fluks.base.Action
import io.fluks.base.UI
import io.fluks.base.android.BaseActivity
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

        override fun LoginBinding.init() {
            model = disposable
        }
    }


    data class Navigate(
        override val finishCurrent: Boolean = false
    ) : Action.Navigate<Context> {

        override fun Context.navigate() {

            startActivity(Intent(this, LoginActivity::class.java))

            if (finishCurrent && this is Activity) finish()
        }
    }
}