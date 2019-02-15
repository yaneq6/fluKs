package io.fluks.core.middleware

import io.fluks.base.Event
import io.fluks.core.AbstractMiddleware
import io.fluks.core.Middleware
import io.reactivex.disposables.Disposable

class Splitter : AbstractMiddleware<Event.More>() {

    override val disposable: Disposable = input
        .subscribe(this::execute)


    private fun execute(record: Middleware.Record<Event.More>): Unit = record.run {
        event.events.fold(copy<Event>(Event.Success)) { record, event ->
            record.copy(
                event = event
            ).also(outputSubject::onNext)
        }
    }
}