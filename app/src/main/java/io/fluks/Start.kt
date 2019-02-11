package io.fluks

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.fluks.base.*
import io.fluks.feature.login.view.NavigateLogin
import io.fluks.feature.scheme.view.NavigateScheme
import io.fluks.feature.session.Session

object Start {

    class Activity :
        AppCompatActivity(),
        Depends<Component> {

        override val component: Start.Component by lazy {
            Start.Module(application.dependencies())
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            di { start() }
        }
    }

    interface Component :
        Session.Component,
        Dispatch.Component {
        fun Activity.start()
    }

    class Module(
        app: App.Component
    ) :
        Component,
        App.Component by app,
        DispatchDelegate<Event> {

        override fun Activity.start() {
            dispatch(Platform.OnTop(weak()))
            dispatch(navigate())
        }

        private fun navigate() = let {
            if (isSignedIn) ::NavigateScheme
            else ::NavigateLogin
        }.invoke(true)

        private val isSignedIn get() = sessionStore.state.isSignedIn
    }
}