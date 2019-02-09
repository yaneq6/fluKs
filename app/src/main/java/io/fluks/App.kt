package io.fluks

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.squareup.leakcanary.LeakCanary
import io.fluks.common.FluksLog
import io.fluks.common.android.FluksLogTimber
import io.fluks.common.measure
import io.fluks.core.Platform
import io.fluks.di.Dependencies
import io.fluks.di.Depends
import io.fluks.di.android.createDi
import io.fluks.di.provide
import io.fluks.di.provider.singleton
import io.palaima.debugdrawer.timber.data.LumberYard
import timber.log.Timber

class App : Application(), Depends<App.Component> {

    val dependencies by lazy {
        Dependencies()
    }

    private val data by lazy {
        Core.Module(
            context = this,
            dependencies = dependencies
        )
    }

    override val component by createDi {
        Module(
            app = this,
            data = data
        )
    }

    override fun onCreate() {
        super.onCreate()
        FluksLog.init(FluksLogTimber)
        Timber.d("onCreate")
    }

    interface Component :
        Domain.Component,
        Debug.Component,
        Platform.State.Component {

        val appModel: AppModel
    }


    private class Module(
        private val app: Application,
        data: Core.Component
    ) :
        Component,
        Domain.Component by Domain.Module(data),
        Debug.Component by Debug.Module(data) {

        override val appModel by provide(singleton()) {
            AppModel(
                sessionStore = sessionStore,
                dispatch = dispatcher
            )
        }

        init {
            measure("init App.Module") {
                leakCanary()
                logging()
                middleware()
                model()
                vectorDrawables()
            }
        }

        private fun leakCanary() {
            if (!LeakCanary.isInAnalyzerProcess(app)) LeakCanary.install(app)
        }

        private fun logging() {
            Timber.plant(
                LumberYard.getInstance(app).apply(LumberYard::cleanUp).tree(),
                Timber.DebugTree()
            )
        }

        private fun middleware() {
            debug(dispatcher)
        }

        private fun model() {
            appModel.init()
        }

        private fun vectorDrawables() {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}

