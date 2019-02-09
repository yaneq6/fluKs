package io.fluks.feature

import io.fluks.App
import io.fluks.feature.session.Session
import io.fluks.core.Platform
import io.fluks.util.di.provide
import io.fluks.util.di.provider.weakSingleton

object Start {

    interface Component :
        Session.Component,
        Platform.State.Component {
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