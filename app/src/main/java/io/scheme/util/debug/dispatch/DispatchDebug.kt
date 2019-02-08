package io.scheme.util.debug.dispatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.palaima.debugdrawer.base.DebugModuleAdapter
import io.scheme.Debug
import io.scheme.R
import io.scheme.util.android.applicationContext
import io.scheme.util.di.Depends
import io.scheme.util.di.dependencies
import io.scheme.util.di.di
import io.scheme.util.di.lazyDi
import io.scheme.util.core.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.debug_middleware.*

class DispatchDebug :
    DebugModuleAdapter(),
    LayoutContainer,
    DispatchDelegate<Event>,
    Depends<Debug.Component> {

    override lateinit var containerView: View private set

    override val component by lazy {
        applicationContext!!.dependencies<Debug.Component>()
    }

    override val dispatch: Dispatch<Event> by lazyDi {
        applicationContext!!.dependencies<Dispatcher.Component>().dispatcher
    }

    private val actionsStore get() = di { debugEventsStore }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View = inflater
        .inflate(R.layout.debug_middleware, parent, false)
        .also { containerView = it }

    override fun onOpened() = actionsStore.state.actions.forEach { action ->
        itemsLayout
            .createEventItemView()
            .setEvent(action) { dispatch(it) }
            .let(itemsLayout::addView)
    }

    override fun onClosed() = itemsLayout.removeAllViews()
}