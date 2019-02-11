package io.fluks.core.middleware

import io.fluks.base.*
import io.fluks.core.AbstractMiddleware
import io.fluks.core.Middleware
import io.fluks.core.Reduce
import io.fluks.core.Store
import java.lang.ref.WeakReference

class Navigator<Context : Any>(
    private val store: Store<Platform.Effect, State<Context>>
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
                    if (finishCurrent && this is UI.Finishable) finish()
                    Event.Success
                }
            }
        } ?: Event.Unhandled
    }


    data class State<Context>(
        val topContextRef: WeakReference<Context>
    ) : Reduce<Platform.Effect, State<Context>> {

        val topContext get() = topContextRef.get()

        override fun invoke(effect: Platform.Effect) = when (effect) {
            is Platform.OnTop<*> -> copy(topContextRef = effect.unsafe<Platform.OnTop<Context>>().context)
            else -> null
        }
    }
}
