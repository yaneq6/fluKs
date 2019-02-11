package io.fluks

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.fluks.common.Dispatch
import io.fluks.common.DispatchDelegate
import io.fluks.common.Event
import io.fluks.core.Platform
import io.fluks.common.weak
import io.fluks.common.Depends
import io.fluks.common.dependencies
import io.fluks.common.di
import io.fluks.common.lazyDi

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

