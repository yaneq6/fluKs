package io.fluks.feature

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.fluks.core.Dispatch
import io.fluks.core.DispatchDelegate
import io.fluks.core.Event
import io.fluks.core.Platform
import io.fluks.common.weak
import io.fluks.di.Depends
import io.fluks.di.android.dependencies
import io.fluks.di.android.di
import io.fluks.di.android.lazyDi

class StartActivity :
    AppCompatActivity(),
    DispatchDelegate<Event>,
    Depends<Start.Component> {
    override val component: Start.Component by lazy {
        Start.Module(
            app = application.dependencies()
        )
    }

    override val dispatch: Dispatch<Event> by lazyDi { dispatcher }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dispatch(Platform.OnTop(weak()))
        di { model.start() }
    }
}

