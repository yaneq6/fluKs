package io.scheme.feature

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.scheme.util.core.Dispatch
import io.scheme.util.core.DispatchDelegate
import io.scheme.util.core.Event
import io.scheme.util.core.Platform
import io.scheme.util.core.util.weak
import io.scheme.util.di.Depends
import io.scheme.util.di.dependencies
import io.scheme.util.di.di
import io.scheme.util.di.lazyDi

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

