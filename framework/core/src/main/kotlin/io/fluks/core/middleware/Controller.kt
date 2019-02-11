package io.fluks.core.middleware

import io.fluks.base.Effect
import io.fluks.base.Event
import io.fluks.core.*
import io.reactivex.disposables.Disposable

class Controller(
    private val storeProviders: List<StoreProvider>
) : AbstractMiddleware<Effect>() {

    override val disposable: Disposable = input
        .map(this::execute)
        .subscribe(outputSubject::onNext)


    private fun execute(record: Middleware.Record<Effect>) = record + let {
        storeProviders
            .mapNotNull { provide -> provide.safe(record.event) }
            .map { store -> store(record.event) }
            .any().let { success ->
                if (success) Event.Success
                else Event.Unhandled
            }
    }
}