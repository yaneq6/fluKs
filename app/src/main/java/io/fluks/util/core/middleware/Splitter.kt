package io.fluks.util.core.middleware

import io.reactivex.disposables.Disposable
import io.fluks.util.core.AbstractMiddleware
import io.fluks.util.core.Event
import io.fluks.util.core.Middleware

class Splitter() : AbstractMiddleware<Event.More>() {

    override val disposable: Disposable = input
        .flatMapIterable(this::execute)
        .subscribe(outputSubject::onNext)


    private fun execute(record: Middleware.Record<Event.More>) = record.event.events.map(record::copy)
}