package io.fluks

import io.fluks.core.*
import io.fluks.core.middleware.Controller
import io.fluks.core.middleware.Executor
import io.fluks.core.middleware.Navigator
import io.fluks.core.middleware.Splitter
import io.fluks.di.provide
import io.fluks.di.provider.singleton
import io.fluks.feature.scheme.Scheme
import io.fluks.feature.session.Session

object Domain {

    interface Component :
        Core.Component,
        Dispatcher.Component,
        EventsManager.Component,
        Session.Component,
        Scheme.Component

    class Module(
        core: Core.Component
    ) :
        Component,
        Core.Component by core,
        Session.Component by Session.Module(core),
        Scheme.Component by Scheme.Module(core) {

        override val eventsManager by provide(singleton()) {
            EventsManager(
                records = dispatch.output
            )
        }

        override val dispatch by provide(singleton()) {

            Dispatcher(
                getTime = getTime,

                splitter = Splitter(),

                navigator = Navigator(
                    scheduler = mainScheduler,
                    getTime = getTime,
                    store = contextStore
                ),

                async = Executor(
                    getTime = getTime,
                    scheduler = backgroundScheduler,
                    interactors = mapOf(
                        interactor { sessionInteractor }
                    )
                ),

                stores = Controller(
                    getTime = getTime,
                    scheduler = mainScheduler,
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