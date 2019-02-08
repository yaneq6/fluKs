package io.scheme.util.core.middleware

import android.app.Activity
import io.scheme.util.core.util.unsafe
import io.scheme.util.core.Platform
import io.scheme.util.core.*


class Navigator<Context>(
    private val store: Store<Platform.Effect, Platform.State<Context>>
) :
    AbstractMiddleware<Action.Navigate<Context>>() {

    override val disposable = input
        .map(this::execute)
        .subscribe(outputSubject::onNext)!!

    private fun execute(record: Middleware.Record<Action.Navigate<Context>>) = record + let {
        store.state.topContext?.let { context ->
            record.event.run { unsafe<Action.Navigate<Any>>() }.run {
                context?.navigate().let {
                    if (finishCurrent && this is Activity) finish()
                    Event.Success
                }
            }
        } ?: Event.Unhandled
    }
}