package io.fluks.base

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

interface Platform {

    interface Effect : BaseEffect

    data class OnTop<Context>(
        val context: WeakReference<Context>
    ) : Effect
}

interface UI {
    interface Component<DataBinding> : Dispatch.Component {
        val layoutId: Int
        val disposable: Disposable
        val eventsLifecycle: Observable<Pair<Event, Boolean>>
        fun DataBinding.bind()
    }
    interface Finishable {
        fun finish()
    }
}