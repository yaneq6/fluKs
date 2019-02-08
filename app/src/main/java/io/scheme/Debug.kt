package io.scheme

import android.app.Activity
import android.support.annotation.StyleRes
import io.palaima.debugdrawer.DebugDrawer
import io.palaima.debugdrawer.commons.BuildModule
import io.palaima.debugdrawer.commons.DeviceModule
import io.palaima.debugdrawer.commons.SettingsModule
import io.palaima.debugdrawer.timber.TimberModule
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.scheme.feature.session.action.SignIn
import io.scheme.util.debug.dispatch.DebugEvents
import io.scheme.util.debug.dispatch.DispatchDebug
import io.scheme.util.debug.dependency.DependenciesDebug
import io.scheme.util.di.create
import io.scheme.util.di.factory.multiton
import io.scheme.util.di.provide
import io.scheme.util.di.provider.singleton
import io.scheme.util.core.*

object Debug {

    interface Component {
        val debugEventsStore: Store<DebugEvents.Effect, DebugEvents>
        val debug: (Dispatcher) -> Disposable
        fun Activity.initDebugDrawer(): DebugDrawer
    }

    class Module(
        data: Core.Component,
        @StyleRes private val themeId: Int = R.style.Theme_AppCompat_Light
    ) : Component,
        Core.Component by data {

        override val debugEventsStore: Store<DebugEvents.Effect, DebugEvents> by provide(singleton()) { name ->
            Store(SimpleStateHolder(
                state = DebugEvents(),
                repository = DebugEvents.Repository(
                    name = name,
                    context = context
                )
            ))
        }

        override fun Activity.initDebugDrawer() = DebugDrawer
            .Builder(this)
            .withTheme(themeId)
            .modules(*modules)
            .build()!!

        override val debug: Dispatcher.() -> Disposable by create(multiton()) { dispatcher ->
            dispatcher.input
                .observeOn(Schedulers.newThread())
                .filter { it.event !is Platform.OnTop<*> }
                .map { DebugEvents.Add(it.event) }
                .subscribe { debugEventsStore(it) }!!
        }


        private val modules by provide {
            arrayOf(
                TimberModule(),
                DispatchDebug(),
                DependenciesDebug(data),
                SettingsModule(),
                BuildModule(),
                DeviceModule()
            )
        }
    }
}

