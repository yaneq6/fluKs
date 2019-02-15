package io.fluks

import io.fluks.base.Event
import io.fluks.core.*
import io.fluks.feature.scheme.Scheme
import io.fluks.feature.session.Session
import io.fluks.core.middleware.Controller
import io.fluks.core.middleware.Executor
import io.fluks.core.middleware.Navigator
import io.fluks.core.middleware.Splitter
import io.fluks.di.provide
import io.fluks.di.provider.singleton
import io.reactivex.Observable

object Domain {

    interface Component :
        Core.Component,
        Dispatcher.Component,
        Session.Component,
        Scheme.Component {

        val eventsManager: EventFilters
        val eventsLifecycle: Observable<Pair<Event, Boolean>>
    }


    class Module(
        core: Core.Component
    ) :
        Component,
        Core.Component by core,
        Session.Component by Session.Module(core),
        Scheme.Component by Scheme.Module(core) {

        override val eventsLifecycle get() = eventsManager.loading.observable()

        override val eventsManager by provide(singleton()) {
            EventFilters(
                records = dispatch.output
            )
        }

        override val dispatch by provide(singleton()) {

            Dispatcher(
                getTime = getTime,

                splitter = Splitter(),

                navigator = Navigator(
                    getTime = getTime,
                    store = contextStore
                ),

                async = Executor(
                    getTime = getTime,
                    mainScheduler = mainScheduler,
                    backgroundScheduler = backgroundScheduler,
                    interactors = mapOf(
                        interactor { sessionInteractor }
                    )
                ),

                stores = Controller(
                    getTime = getTime,
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