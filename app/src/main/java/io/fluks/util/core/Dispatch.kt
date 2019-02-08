package io.fluks.util.core

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.fluks.util.core.util.merge
import io.fluks.util.core.util.unsafe
import io.fluks.util.core.util.weak

interface Dispatch<in T : Any> {
    infix fun Any.dispatch(any: T?)

    object Void : Dispatch<Any> {
        override fun Any.dispatch(any: Any?) = Unit
    }
}

interface DispatchDelegate<in E : Any> : Dispatch<E> {
    val dispatch: Dispatch<E>

    override fun Any.dispatch(any: E?) = dispatch.run {
        this@dispatch.dispatch(any)
    }
}

class Dispatcher(
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

    val input = middlewares.merge { input }
    val output = middlewares.merge { output.filter { record -> !record.hasError } }
    val error = middlewares.merge { output.filter { record -> record.hasError } }

    private val disposable = CompositeDisposable(
        input.subscribe { record ->
            logDispatch(record)
        },
        output.subscribe { record ->
            dispatchRecord(record)
            logSuccess(record)
        }
    )

    override fun Any.dispatch(any: Event?) = any?.let { event ->
        dispatchRecord(Middleware.Record(
            context = weak(),
            event = event
        ))
    } ?: logNullEvent()




    private fun dispatchRecord(record: Middleware.Record<Event>) {
        when (record.event) {
            is Action.Async -> async
            is Action.Navigate<*> -> navigator
            is Effect -> stores
            is Event.More -> splitter ?: throw IllegalArgumentException("Event.More cannot by handled without Splitter")
            else -> null
        }?.invoke(record.unsafe())
    }

    override fun isDisposed() = disposable.isDisposed
    override fun dispose() = disposable.dispose()

    interface Component {
        val dispatcher: Dispatcher
    }
}

