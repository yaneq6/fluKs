package io.fluks.common

import io.reactivex.disposables.Disposable

interface UI {
    interface Component<DataBinding> : Dispatch.Component {
        val layoutId: Int
        val disposable: Disposable
        fun DataBinding.bind()
    }
}