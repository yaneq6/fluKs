package io.fluks.debug.dispatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.fluks.common.android.applicationContext
import io.fluks.core.Dispatch
import io.fluks.core.DispatchDelegate
import io.fluks.core.Dispatcher
import io.fluks.core.Event
import io.fluks.debug.R
import io.fluks.common.Depends
import io.fluks.common.dependencies
import io.fluks.common.di
import io.fluks.common.lazyDi
import io.palaima.debugdrawer.base.DebugModuleAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.debug_dispatch.*

class DispatchDebug :
    DebugModuleAdapter(),
    LayoutContainer,
    DispatchDelegate<Event>,
    Depends<DispatchDebug.Component> {

    override lateinit var containerView: View private set

    override val component by lazy {
        applicationContext!!.dependencies<DispatchDebug.Component>()
    }

    override val dispatch: Dispatch<Event> by lazyDi {
        applicationContext!!.dependencies<Dispatcher.Component>().dispatcher
    }

    private val debugEvents get() = di { debugEvents }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View = inflater
        .inflate(R.layout.debug_dispatch, parent, false)
        .also { containerView = it }

    override fun onOpened() = debugEvents.forEach { action ->
        itemsLayout
            .createEventItemView()
            .setEvent(action) {
                dispatch(it)
            }
            .let(itemsLayout::addView)
    }

    override fun onClosed() = itemsLayout.removeAllViews()

    interface Component {
        val debugEvents: List<Event>

    }
}