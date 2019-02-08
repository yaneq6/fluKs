package io.fluks.feature

import io.fluks.App
import io.fluks.util.core.Platform
import io.fluks.util.di.provide
import io.fluks.util.di.provider.singleton
import io.fluks.feature.session.Session

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