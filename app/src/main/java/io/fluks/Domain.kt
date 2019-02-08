package io.fluks

import io.fluks.feature.scheme.Scheme
import io.fluks.feature.session.Session
import io.fluks.util.core.*
import io.fluks.util.core.middleware.Controller
import io.fluks.util.core.middleware.Executor
import io.fluks.util.core.middleware.Navigator
import io.fluks.util.core.middleware.Splitter
import io.fluks.util.di.provide
import io.fluks.util.di.provider.singleton

object Domain {

    interface Component :
        Core.Component,
        Dispatcher.Component,
        Session.Component,
        Scheme.Component


    class Module(
        data: Core.Component
    ) :
        Component,
        Core.Component by data,
        Session.Component by Session.Module(data),
        Scheme.Component by Scheme.Module(data) {

        override val dispatcher by provide(singleton()) {

            Dispatcher(
                splitter = Splitter(),

                navigator = Navigator(
                    store = platformStore
                ),

                async = Executor(
                    mainScheduler = mainScheduler,
                    backgroundScheduler = backgroundScheduler,
                    interactors = mapOf(
                        interactor { signIn },
                        interactor { signOut }
                    )
                ),

                stores = Controller(
                    storeProviders = listOf(
                        store { sessionStore },
                        store { schemeStore },
                        store { platformStore }
                    )
                )
            )
        }
    }
}