package io.fluks.core.android.middleware

import io.fluks.common.dynamicCast
import io.fluks.common.unsafe
import io.fluks.core.*

interface Finishable {
    fun finish()
}

class Navigator<Context : Any>(
    private val store: Store<Platform.Effect, Platform.State<Context>>
) :
    AbstractMiddleware<Action.Navigate<Context>>() {

    override val disposable = input
        .map(this::execute)
        .subscribe(outputSubject::onNext)!!

    private fun execute(record: Middleware.Record<Action.Navigate<Context>>) = record + let {
        store.state.topContext?.let { context ->
            record.event.run { unsafe<Action.Navigate<Any>>() }.run {
                context.run {
                    navigate()
                    if (finishCurrent) dynamicCast<Finishable>().finish()
                    Event.Success
                }
            }
        } ?: Event.Unhandled
    }
}