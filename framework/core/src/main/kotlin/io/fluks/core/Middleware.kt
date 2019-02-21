@file:Suppress("MemberVisibilityCanBePrivate")

package io.fluks.core

import io.fluks.base.Event
import io.fluks.base.WeakProvider
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

interface Middleware<A : Event> : Disposable {

    val output: Observable<out Record<Event>>
    operator fun invoke(event: Record<A>)

    /*
    TODO Add custom toString implementation for better logging
    TODO Add abstraction over [event to timestamp] pair
    */
    data class Record<out E : Event>(
        val event: E,
        val predecessors: List<Pair<Event, Long>>,
        val context: WeakProvider<*>,
        val timestamp: Long
    ) {

        constructor(
            event: E,
            context: Any,
            timestamp: Long
        ) : this(
            event = event,
            predecessors = emptyList(),
            context = WeakProvider(context),
            timestamp = timestamp
        )

        init {
            if (isFinished && predecessors.isEmpty()) throw IllegalStateException("Cannot init empty record")
        }

        operator fun <E : Event> plus(eventTimestamp: Pair<E, Long>) = Record(
            context = context,
            event = eventTimestamp.first,
            timestamp = eventTimestamp.second,
            predecessors = listOf(this.event to timestamp) + predecessors
        )

        fun <E : Event> copy(event: E) = Record(
            context = context,
            event = event,
            timestamp = timestamp,
            predecessors = predecessors
        )

        val success get() = event is Event.Success
        val failure get() = event is Event.Error
        val isStarting get() = predecessors.isEmpty()
        val isFinished get() = (success or failure) and !isStarting
        val error get() = (event as? Event.Error)?.throwable
        val rootEvent get() = predecessors.lastOrNull()?.first ?: event
        val startedAt get() = predecessors.lastOrNull()?.second ?: timestamp
    }
}

abstract class AbstractMiddleware<A : Event> : Middleware<A> {

    protected val input: Observable<Middleware.Record<A>> get() = inputSubject

    override val output: Observable<Middleware.Record<Event>> get() = outputSubject

    override fun invoke(event: Middleware.Record<A>) = inputSubject.onNext(event)

    override fun dispose() = disposable.dispose()

    override fun isDisposed(): Boolean = disposable.isDisposed

    protected val outputSubject = PublishSubject.create<Middleware.Record<Event>>()!!

    protected val inputSubject = PublishSubject.create<Middleware.Record<A>>()!!

    protected abstract val disposable: Disposable
}
