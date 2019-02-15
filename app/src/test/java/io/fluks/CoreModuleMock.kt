package io.fluks

import android.content.Context
import android.content.SharedPreferences
import io.fluks.base.Action
import io.fluks.base.Event
import io.fluks.base.weak
import io.fluks.core.GetTime
import io.fluks.core.SimpleStateHolder
import io.fluks.core.Store
import io.fluks.core.middleware.Navigator
import io.fluks.di.DependenciesAccumulator
import io.fluks.di.provide
import io.fluks.di.provider.singleton
import io.reactivex.schedulers.Schedulers

class CoreModuleMock(
    override val context: Context,
    dependencies: DependenciesAccumulator
) : Core.Component,
    DependenciesAccumulator by dependencies {

    private val testScheduler = Schedulers.from(Runnable::run)

    override val mainScheduler = testScheduler

    override val backgroundScheduler = testScheduler

    override val api: (Action) -> Event = Core.FAKE_API_CLIENT

    override val sharedPreferences: SharedPreferences by provide(singleton()) {
        SharedPreferencesMock()
    }

    override val contextStore by provide(singleton()) {
        Store(SimpleStateHolder(Navigator.State(context.weak())))
    }

    override val getTime = GetTimeMock()
}

class GetTimeMock(private var currentTime: Long = 0) : GetTime {
    override fun invoke(): Long = currentTime++
}