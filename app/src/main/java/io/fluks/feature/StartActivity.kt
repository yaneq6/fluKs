package io.fluks.feature

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.fluks.core.Dispatch
import io.fluks.core.DispatchDelegate
import io.fluks.core.Event
import io.fluks.core.Platform
import io.fluks.core.weak
import io.fluks.util.di.Depends
import io.fluks.util.di.dependencies
import io.fluks.util.di.di
import io.fluks.util.di.lazyDi

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

