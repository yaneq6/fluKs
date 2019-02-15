package io.fluks

import android.app.Application
import io.fluks.base.FluksLog
import io.fluks.base.android.FluksLogTimber
import io.fluks.base.measure
import io.fluks.di.Dependencies
import io.fluks.base.Depends
import io.fluks.base.createDi

class App : Application(), Depends<App.Component> {

    override val component by createDi {
        Module(
            core = Core.Module(
                context = this,
                dependencies = Dependencies()
            ),
            init = Init.Module(
                app = this
            )
        )
    }

    override fun onCreate() {
        super.onCreate()
        FluksLog.init(FluksLogTimber)
    }

    interface Component :
        Domain.Component,
        Debug.Component

    class Module(
        core: Core.Component,
        init: Init.Component
    ) :
        Component,
        Domain.Component by Domain.Module(core),
        Debug.Component by Debug.Module(core),
        Init.Component by init {

        init {
            measure("init App.Module") {
                leakCanary()
                logging()
                dispatch()
                vectorDrawables()
            }
        }

        private fun dispatch() {
            debug(dispatch)
        }
    }
}
