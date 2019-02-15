package io.fluks

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.squareup.leakcanary.LeakCanary
import io.palaima.debugdrawer.timber.data.LumberYard
import timber.log.Timber

object Init {

    interface Component {
        fun leakCanary()
        fun vectorDrawables()
        fun logging()
    }

    class Module(
        private val app: Application
    ) : Component {

        override fun leakCanary() {
            if (!LeakCanary.isInAnalyzerProcess(app)) LeakCanary.install(app)
        }

        override fun logging() {
            Timber.plant(
                LumberYard.getInstance(app).apply(LumberYard::cleanUp).tree(),
                Timber.DebugTree()
            )
        }

        override fun vectorDrawables() {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}