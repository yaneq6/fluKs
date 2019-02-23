package io.fluks.core

import io.fluks.base.Event
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import java.util.*

class EventsManager(
    val records: Observable<Middleware.Record<Event>>
) {
    val status: Observable<Event.Status> = records.compose(::EventStatusSource)

    private val normalized = records
        .filter { it.event !is Event.More }
        .distinctUntilChanged()!!

    val events = normalized.map { it.event }!!

    interface Component {
        val eventsManager: EventsManager
    }
}

private class EventStatusSource(
    input: Observable<Middleware.Record<Event>>
) : ObservableSource<Event.Status> {

    private val events = Vector<Pair<Event, Long>>()

    private val output = input
        .map { it.handle() }
        .filter { it != Event.Status.Empty }
        .share()!!

    override fun subscribe(observer: Observer<in Event.Status>) {
        output.subscribe(observer)
    }

    private fun Middleware.Record<Event>.handle(): Event.Status = when {
        isStarting && onStart(rootEvent to startedAt) -> true
        isFinished && onFinish(rootEvent to startedAt) -> false
        else -> null
    }?.let { status ->
        Event.Status(
            event = rootEvent,
            isRunning = status,
            startedAt = startedAt
        )
    } ?: Event.Status.Empty

    private fun onStart(event: Pair<Event, Long>) = events.add(event)

    private fun onFinish(event: Pair<Event, Long>) = events.remove(event) && events.isEmpty()
}