package io.fluks.core

import io.reactivex.disposables.Disposable

abstract class Model : Initializable {

    private var disposable: Disposable? = null

    protected open fun subscribe(): Disposable = this

    final override fun init(): Disposable = subscribe().also {
        if (it != this) disposable = it
    }

    final override fun isDisposed(): Boolean = disposable?.isDisposed ?: true

    final override fun dispose() {
        disposable?.apply {
            if (!isDisposed) dispose()
            disposable = null
        }
    }
}

interface Initializable : Disposable {
    fun init(): Disposable
}