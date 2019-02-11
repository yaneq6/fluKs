package io.fluks

import io.fluks.common.Dispatch
import io.fluks.di.provide
import io.fluks.di.provider.weakSingleton
import io.fluks.feature.session.Session

object Start {

    interface Component :
        Session.Component,
        Dispatch.Component {
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
                dispatch = dispatch
            )
        }
    }
}