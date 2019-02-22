package io.fluks.core

import io.fluks.base.Action
import io.fluks.base.Event
import io.fluks.base.WeakProvider
import io.reactivex.subjects.PublishSubject
import org.junit.Test

class EventFiltersTest {

    private val publisher = PublishSubject.create<Middleware.Record<Event>>()
    private val eventFilters = EventFilters(publisher)

    @Test
    fun getLoading() {
        val observable1 = eventFilters.lifecycle.observable().test()
        val observable2 = eventFilters.lifecycle.observable().test()

        publisher.onNext(
            Middleware.Record(
                event = TestAsyncAction,
                context = this,
                timestamp = 0
            )
        )
        publisher.onNext(
            Middleware.Record(
                event = Event.Success,
                predecessors = listOf(TestAsyncAction to 0L),
                context = WeakProvider(this),
                timestamp = 0
            )
        )

        observable1.assertValues(
            Event.Lifecycle(TestAsyncAction, 0, true),
            Event.Lifecycle(TestAsyncAction, 0, false)
        )
        observable2.assertValues(
            Event.Lifecycle(TestAsyncAction, 0, true),
            Event.Lifecycle(TestAsyncAction, 0, false)
        )
    }
}

object TestAsyncAction : Action.Async