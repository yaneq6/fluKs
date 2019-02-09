package io.fluks.util.debug.dispatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.fluks.Debug
import io.fluks.R
import io.fluks.core.Dispatch
import io.fluks.core.DispatchDelegate
import io.fluks.core.Dispatcher
import io.fluks.core.Event
import io.fluks.util.android.applicationContext
import io.fluks.di.Depends
import io.fluks.di.android.dependencies
import io.fluks.di.android.di
import io.fluks.di.android.lazyDi
import io.palaima.debugdrawer.base.DebugModuleAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.debug_dispatch.*

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
        .inflate(R.layout.debug_dispatch, parent, false)
        .also { containerView = it }

    override fun onOpened() = actionsStore.state.actions.forEach { action ->
        itemsLayout
            .createEventItemView()
            .setEvent(action) {
                dispatch(it)
            }
            .let(itemsLayout::addView)
    }

    override fun onClosed() = itemsLayout.removeAllViews()
}