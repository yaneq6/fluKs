package io.fluks.core

import io.fluks.base.Event
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import java.util.*

class EventFilters(
    val records: Observable<Middleware.Record<Event>>
) {
    val loading: ObservableSource<Pair<Event, Boolean>> = LoadingManager(records)

    private val normalized = records
        .filter { it.event !is Event.More }
        .distinctUntilChanged()!!

    val events = normalized.map { it.event }!!
}


private class LoadingManager(
    vararg inputs: Observable<Middleware.Record<Event>>
) : ObservableSource<Pair<Event, Boolean>> {

    private val events = Vector<Event>()

    private val output = Observable
        .merge(inputs.toList())
        .map { it.handle() }
        .filter { it.first != None }

    override fun subscribe(observer: Observer<in Pair<Event, Boolean>>) {
        output.subscribe(observer)
    }

    private fun Middleware.Record<Event>.handle(): Pair<Event, Boolean> = when {
        isStarting && onStart(rootEvent) -> true
        isFinished && onFinish(rootEvent) -> false
        else -> null
    }?.let { status ->
        rootEvent to status
    } ?: None to false

    private fun onStart(event: Event) = events.add(event)

    private fun onFinish(event: Event) = events.remove(event) && events.isEmpty()

    private object None : Event
}