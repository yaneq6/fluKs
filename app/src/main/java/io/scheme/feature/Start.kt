package io.scheme.feature

import io.scheme.App
import io.scheme.util.core.Platform
import io.scheme.util.di.provide
import io.scheme.util.di.provider.singleton
import io.scheme.feature.session.Session

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

        override val model: StartModel by provide(singleton()) {
            StartModel(
                sessionStore = sessionStore,
                dispatch = dispatcher
            )
        }
    }
}