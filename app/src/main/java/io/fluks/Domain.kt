package io.fluks

import io.fluks.core.Dispatcher
import io.fluks.core.interactor
import io.fluks.core.store
import io.fluks.feature.scheme.Scheme
import io.fluks.feature.session.Session
import io.fluks.core.middleware.Controller
import io.fluks.core.middleware.Executor
import io.fluks.core.middleware.Navigator
import io.fluks.core.middleware.Splitter
import io.fluks.di.provide
import io.fluks.di.provider.singleton

object Domain {

    interface Component :
        Core.Component,
        Dispatcher.Component,
        Session.Component,
        Scheme.Component


    class Module(
        core: Core.Component
    ) :
        Component,
        Core.Component by core,
        Session.Component by Session.Module(core),
        Scheme.Component by Scheme.Module(core) {

        override val dispatch by provide(singleton()) {

            Dispatcher(
                splitter = Splitter(),

                navigator = Navigator(
                    store = contextStore
                ),

                async = Executor(
                    mainScheduler = mainScheduler,
                    backgroundScheduler = backgroundScheduler,
                    interactors = mapOf(
                        interactor { sessionInteractor }
                    )
                ),

                stores = Controller(
                    storeProviders = listOf(
                        store { sessionStore },
                        store { schemeStore },
                        store { contextStore }
                    )
                )
            )
        }
    }
}