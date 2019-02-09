package io.fluks.core.middleware

import io.reactivex.disposables.Disposable
import io.fluks.core.AbstractMiddleware
import io.fluks.core.Event
import io.fluks.core.Middleware

class Splitter : AbstractMiddleware<Event.More>() {

    override val disposable: Disposable = input
        .flatMapIterable(this::execute)
        .subscribe(outputSubject::onNext)


    private fun execute(record: Middleware.Record<Event.More>) = record.event.events.map(record::copy)
}