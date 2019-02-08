package io.scheme.util.core

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.lang.ref.WeakReference

interface Middleware<A : Event> : Disposable {

    val input: Observable<out Record<A>>
    val output: Observable<out Record<Event>>

    operator fun invoke(event: Record<A>)

    data class Record<out E : Event>(
        val event: E,
        val context: WeakReference<Any>,
        val predecessors: List<Event> = emptyList()
    ) {
        operator fun <E : Event> plus(event: E) = Record(
            context = context,
            event = event,
            predecessors = listOf(this.event) + predecessors
        )

        fun <E: Event> copy(event: E) = Record(
            context = context,
            event = event,
            predecessors = listOf(this.event) + predecessors
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
