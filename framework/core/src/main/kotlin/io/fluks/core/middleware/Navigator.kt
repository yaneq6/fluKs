package io.fluks.core.middleware

import io.fluks.base.*
import io.fluks.core.*
import io.reactivex.Scheduler
import java.lang.ref.WeakReference

class Navigator<Context : Any>(
    private val store: Store<Platform.Effect, State<Context>>,
    private val getTime: GetTime,
    scheduler: Scheduler
) :
    AbstractMiddleware<Action.Navigate<Context>>() {

    override val disposable = input
        .observeOn(scheduler)
        .map(this::execute)
        .subscribe(outputSubject::onNext)!!

    private fun execute(record: Middleware.Record<Action.Navigate<Context>>) = record + ((
        store.state.topContext?.let { context ->
            record.event.run { unsafe<Action.Navigate<Any>>() }.run {
                context.run {
                    navigate()
                    if (finishCurrent && this is UI.Finishable) finish()
                    Event.Success
                }
            }
        } ?: Event.Unhandled) to getTime())


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
