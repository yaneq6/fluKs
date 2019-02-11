package io.fluks

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.fluks.base.*
import io.fluks.base.Platform

class StartActivity :
    AppCompatActivity(),
    DispatchDelegate<Event>,
    Depends<Start.Component> {

    override val component: Start.Component by lazy {
        Start.Module(
            app = application.dependencies()
        )
    }

    override val dispatch: Dispatch<Event> by lazyDi { dispatch }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dispatch(Platform.OnTop(weak()))
        di { model.start() }
    }
}

