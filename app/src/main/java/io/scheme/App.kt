package io.scheme

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.squareup.leakcanary.LeakCanary
import io.palaima.debugdrawer.timber.data.LumberYard
import io.scheme.util.core.Platform
import io.scheme.util.core.measure
import io.scheme.util.di.Dependencies
import io.scheme.util.di.Depends
import io.scheme.util.di.createDi
import io.scheme.util.di.provide
import io.scheme.util.di.provider.singleton
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
                timber()
                middleware()
                model()
                vectorDrawables()
            }
        }

        private fun leakCanary() {
            if (!LeakCanary.isInAnalyzerProcess(app)) LeakCanary.install(app)
        }

        private fun timber() = Timber.plant(
            LumberYard.getInstance(app).apply(LumberYard::cleanUp).tree(),
            Timber.DebugTree()
        )

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


