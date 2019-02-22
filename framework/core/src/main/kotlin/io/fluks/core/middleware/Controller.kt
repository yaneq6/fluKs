package io.fluks.core.middleware

import io.fluks.base.Effect
import io.fluks.base.Event
import io.fluks.core.AbstractMiddleware
import io.fluks.core.GetTime
import io.fluks.core.Middleware
import io.fluks.core.StoreProvider
import io.reactivex.Scheduler

class Controller(
    private val storeProviders: List<StoreProvider>,
    private val getTime: GetTime,
    scheduler: Scheduler
) : AbstractMiddleware<Effect>() {

    override val disposable = input
        .observeOn(scheduler)
        .map(this::execute)
        .subscribe(outputSubject::onNext)!!


    private fun execute(record: Middleware.Record<Effect>) = record + (storeProviders
        .mapNotNull { provide -> provide.safe(record.event) }
        .map { store -> store.invoke(record.event) }
        .any().let { success ->
            if (success) Event.Success
            else Event.Unhandled
        } to getTime())
}