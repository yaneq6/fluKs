@file:Suppress("MemberVisibilityCanBePrivate")

package io.fluks.core

import io.fluks.common.Event
import io.fluks.common.WeakProvider
import io.fluks.common.timestamp
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

interface Middleware<A : Event> : Disposable {

    val input: Observable<out Record<A>>
    val output: Observable<out Record<Event>>

    operator fun invoke(event: Record<A>)

    /*
    TODO Add custom toString implementation for better logging
    TODO Add abstraction over [event to timestamp] pair
    */
    data class Record<out E : Event>(
        val event: E,
        val context: WeakProvider<Any>,
        val timestamp: Long = timestamp(),
        val predecessors: List<Pair<Event, Long>> = emptyList()
    ) {
        operator fun <E : Event> plus(event: E) = Record(
            context = context,
            event = event,
            timestamp = timestamp(),
            predecessors = listOf(this.event to timestamp) + predecessors
        )

        fun <E : Event> copy(event: E) = Record(
            context = context,
            event = event,
            timestamp = timestamp(),
            predecessors = predecessors
        )

        val error: Throwable? get() = (event as? Event.Error)?.throwable
        val hasError get() = error != null
        val isFinished: Boolean get() = event is Event.Error || event is Event.Success
    }
}

abstract class AbstractMiddleware<A : Event> : Middleware<A> {

    override val input: Observable<Middleware.Record<A>> get() = inputSubject

    override val output: Observable<Middleware.Record<Event>> get() = outputSubject

    override fun invoke(event: Middleware.Record<A>) = inputSubject.onNext(event)

    override fun dispose() = disposable.dispose()

    override fun isDisposed(): Boolean = disposable.isDisposed

    protected val outputSubject = PublishSubject.create<Middleware.Record<Event>>()!!

    protected val inputSubject = PublishSubject.create<Middleware.Record<A>>()!!

    protected abstract val disposable: Disposable
}
