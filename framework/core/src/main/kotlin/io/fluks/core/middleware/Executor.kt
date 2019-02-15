package io.fluks.core.middleware

import io.reactivex.Scheduler
import io.fluks.base.measure
import io.fluks.core.AbstractMiddleware
import io.fluks.base.Action
import io.fluks.base.Event
import io.fluks.core.GetTime
import io.fluks.core.Middleware
import kotlin.reflect.KClass

class Executor(
    mainScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    private val getTime: GetTime,
    private val interactors: Map<KClass<*>, () -> (Action.Async) -> Event>
) :
    AbstractMiddleware<Action.Async>() {

    override val disposable = input
        .subscribeOn(mainScheduler)
        .observeOn(backgroundScheduler)
        .map(this::execute)
        .subscribe(outputSubject::onNext)!!

    @Suppress("UNCHECKED_CAST")
    private fun execute(record: Middleware.Record<Action.Async>) = measure(record.event) {
        record + (try {
            interactors.entries
                .firstOrNull { it.key.java.isInstance(record.event) }
                ?.value
                ?.invoke()
                ?.invoke(record.event)
                ?: throw UnsupportedOperationException(
                    "Cannot dispatch input $record, of type: ${record::class.java}")
        } catch (e: Throwable) {
            Event.Error(record.event, e)
        } to getTime())
    }
}