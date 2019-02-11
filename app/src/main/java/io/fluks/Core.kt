package io.fluks

import android.content.Context
import android.content.SharedPreferences
import io.fluks.base.Action
import io.fluks.base.Event
import io.fluks.base.Platform
import io.fluks.base.weak
import io.fluks.core.SimpleStateHolder
import io.fluks.core.Store
import io.fluks.core.middleware.Navigator
import io.fluks.di.DependenciesAccumulator
import io.fluks.di.provide
import io.fluks.di.provider.singleton
import io.fluks.feature.session.action.SignIn
import io.fluks.feature.session.action.SignOut
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object Core {

    interface Component :
        DependenciesAccumulator {

        val context: Context

        val api: (Action) -> Event

        val sharedPreferences: SharedPreferences

        val contextStore: Store<Platform.Effect, Navigator.State<Context>>

        val mainScheduler: Scheduler

        val backgroundScheduler: Scheduler
    }

    class Module(
        override val context: Context,
        dependencies: DependenciesAccumulator
    ) :
        Component,
        DependenciesAccumulator by dependencies {

        override val mainScheduler = AndroidSchedulers.mainThread()!!

        override val backgroundScheduler = Schedulers.io()

        override val api: (Action) -> Event = { action ->
            when (action) {
                is SignIn -> SignIn.Success("token")
                is SignOut -> SignOut.Success()
                else -> throw IllegalArgumentException()
            }
        }

        override val sharedPreferences by provide(singleton()) {
            context.getSharedPreferences(Core::class.qualifiedName, Context.MODE_PRIVATE)!!
        }

        override val contextStore by provide(singleton()) {
            Store(SimpleStateHolder(Navigator.State(context.weak())))
        }
    }
}