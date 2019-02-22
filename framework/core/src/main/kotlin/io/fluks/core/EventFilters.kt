package io.fluks.core

import io.fluks.base.Event
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import java.util.*

class EventFilters(
    val records: Observable<Middleware.Record<Event>>
) {
    val lifecycle: Observable<Event.Lifecycle> = records.compose(::EventLifecycleSource)

    private val normalized = records
        .filter { it.event !is Event.More }
        .distinctUntilChanged()!!

    val events = normalized.map { it.event }!!
}

private class EventLifecycleSource(
    input: Observable<Middleware.Record<Event>>
) : ObservableSource<Event.Lifecycle> {

    private val events = Vector<Pair<Event, Long>>()

    private val output = input
        .map { it.handle() }
        .filter { it != Event.Lifecycle.Empty }
        .share()!!

    override fun subscribe(observer: Observer<in Event.Lifecycle>) {
        output.subscribe(observer)
    }

    private fun Middleware.Record<Event>.handle(): Event.Lifecycle = when {
        isStarting && onStart(rootEvent to startedAt) -> true
        isFinished && onFinish(rootEvent to startedAt) -> false
        else -> null
    }?.let { status ->
        Event.Lifecycle(
            event = rootEvent,
            isRunning = status,
            startedAt = startedAt
        )
    } ?: Event.Lifecycle.Empty

    private fun onStart(event: Pair<Event, Long>) = events.add(event)

    private fun onFinish(event: Pair<Event, Long>) = events.remove(event) && events.isEmpty()
}