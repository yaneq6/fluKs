package io.fluks.core

import io.fluks.base.*
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

class Dispatcher(
    private val getTime: GetTime,
    private val navigator: Middleware<*>,
    private val async: Middleware<Action.Async>,
    private val stores: Middleware<Effect>,
    private val splitter: Middleware<Event.More>? = null
) :
    Dispatch<Event>,
    Disposable {

    private val middlewares = listOfNotNull(
        navigator,
        async,
        stores,
        splitter
    )

    private val inputSubject = PublishSubject.create<Middleware.Record<Event>>()

    val output = middlewares
        .merge { output }
        .mergeWith(inputSubject)
        .share()!!

    val disposable = output
        .subscribe { record -> dispatchRecord(record) }!!

    private fun dispatchRecord(record: Middleware.Record<Event>) {
        when (record.event) {
            is Action.Async -> async
            is Action.Navigate<*> -> navigator
            is Effect -> stores
            is Event.More -> splitter ?: throw IllegalArgumentException("Event.More cannot by handled without Splitter")
            else -> null
        }?.invoke(record.unsafe())
    }

    override fun Any.dispatch(any: Event?) {
        any?.let { event ->
            Middleware.Record(
                context = weak(),
                event = event,
                timestamp = getTime()
            ).also { record ->
                logDispatch(record)
                inputSubject.onNext(record)
            }
        } ?: logNullEvent()
    }

    override fun isDisposed() = disposable.isDisposed
    override fun dispose() = disposable.dispose()

    interface Component : Dispatch.Component, DispatchDelegate<Event> {
        override val dispatch: Dispatcher
    }
}