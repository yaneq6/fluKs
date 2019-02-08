package io.scheme.feature.scheme.view

import android.view.GestureDetector
import android.view.View
import io.scheme.App
import io.scheme.Debug
import io.scheme.R
import io.scheme.util.android.GetContext
import io.scheme.util.core.Platform
import io.scheme.util.di.provide
import io.scheme.util.di.provider.singleton
import io.scheme.util.di.provider.weakSingleton
import io.scheme.databinding.SchemeBinding
import io.scheme.feature.scheme.Scheme
import io.scheme.feature.scheme.view.gesture.CompoundGestureListener
import io.scheme.feature.scheme.view.gesture.GestureLogger
import io.scheme.feature.scheme.view.gesture.GestureObservable
import io.scheme.feature.scheme.view.gesture.GestureObservableListener

object SchemeUI {

    interface Component :
        Platform.Component<SchemeBinding, SchemeViewModel, Scheme.State>,
        Scheme.Component,
        Debug.Component {

        val gestureObservable: GestureObservable
        val touchListener: View.OnTouchListener
    }

    class Module(
        private val getContext: GetContext,
        private val app: App.Component
    ) :
        App.Component by app,
        Component {

        override val store get() = schemeStore

        override val layoutId = R.layout.scheme

        override val bind = SchemeBinding::setModel

        override val viewModel by provide(weakSingleton()) { SchemeViewModel(dispatcher) }

        override val touchListener get() = View.OnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }

        private val gestureDetector by provide(singleton()) {
            GestureDetector(
                getContext(),
                CompoundGestureListener(
                    GestureLogger(),
                    gestureObservable
                )
            )
        }

        override val gestureObservable by provide(singleton()) {
            GestureObservableListener()
        }
    }
}