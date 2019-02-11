package io.fluks

import io.fluks.feature.session.Session
import io.fluks.core.Platform
import io.fluks.di.provide
import io.fluks.di.provider.weakSingleton

object Start {

    interface Component :
        Session.Component,
        Platform.Component {
        val model: StartModel
    }

    class Module(
        app: App.Component
    ) :
        Component,
        App.Component by app {

        override val model: StartModel by provide(weakSingleton()) {
            StartModel(
                sessionStore = sessionStore,
                dispatch = dispatcher
            )
        }
    }
}