package io.fluks.feature.scheme.view

import android.view.GestureDetector
import android.view.View
import io.fluks.App
import io.fluks.R
import io.fluks.common.android.BaseActivity
import io.fluks.common.android.GetContext
import io.fluks.common.UI
import io.fluks.databinding.SchemeBinding
import io.fluks.di.provide
import io.fluks.di.provider.singleton
import io.fluks.di.provider.weakSingleton
import io.fluks.feature.scheme.view.gesture.CompoundGestureListener
import io.fluks.feature.scheme.view.gesture.GestureLogger
import io.fluks.feature.scheme.view.gesture.GestureObservable
import io.fluks.feature.scheme.view.gesture.GestureObservableListener

object SchemeUI {

    interface Component :
        UI.Component<SchemeBinding>,
        BaseActivity.Component {

        val gestureObservable: GestureObservable
        val touchListener: View.OnTouchListener
        val viewModel: SchemeViewModel
    }

    class Module(
        private val getContext: GetContext,
        private val app: App.Component
    ) :
        App.Component by app,
        Component {

        override val viewModel: SchemeViewModel get() = disposable

        override val disposable by provide(weakSingleton()) {
            SchemeViewModel(dispatch)
        }

        override fun SchemeBinding.bind() {
            model = disposable
        }

        override val layoutId = R.layout.scheme

        override val touchListener
            get() = View.OnTouchListener { _, event ->
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