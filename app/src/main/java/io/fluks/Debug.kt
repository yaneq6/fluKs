package io.fluks

import android.app.Activity
import android.support.annotation.StyleRes
import io.fluks.core.Dispatcher
import io.fluks.core.Platform
import io.fluks.core.SimpleStateHolder
import io.fluks.core.Store
import io.palaima.debugdrawer.DebugDrawer
import io.palaima.debugdrawer.commons.BuildModule
import io.palaima.debugdrawer.commons.DeviceModule
import io.palaima.debugdrawer.commons.SettingsModule
import io.palaima.debugdrawer.timber.TimberModule
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.fluks.util.debug.dispatch.DebugEvents
import io.fluks.util.debug.dispatch.DispatchDebug
import io.fluks.util.debug.dependency.DependenciesDebug
import io.fluks.di.create
import io.fluks.di.factory.multiton
import io.fluks.di.provide
import io.fluks.di.provider.singleton

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
            Store(
                SimpleStateHolder(
                    state = DebugEvents(),
                    repository = DebugEvents.Repository(
                        name = name,
                        context = context
                    )
                )
            )
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

