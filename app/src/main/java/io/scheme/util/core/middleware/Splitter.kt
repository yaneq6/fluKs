package io.scheme.util.core.middleware

import io.reactivex.disposables.Disposable
import io.scheme.util.core.AbstractMiddleware
import io.scheme.util.core.Event
import io.scheme.util.core.Middleware

class Splitter() : AbstractMiddleware<Event.More>() {

    override val disposable: Disposable = input
        .flatMapIterable(this::execute)
        .subscribe(outputSubject::onNext)


    private fun execute(record: Middleware.Record<Event.More>) = record.event.events.map(record::copy)
}