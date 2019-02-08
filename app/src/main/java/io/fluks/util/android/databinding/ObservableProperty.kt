package io.fluks.util.android.databinding

import android.databinding.Observable.OnPropertyChangedCallback
import io.reactivex.Observable
import io.reactivex.Observable.create
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import java.util.concurrent.atomic.AtomicReference

class ObservableProperty<T>(
    private val default: T
) : BaseObservableProperty<T>(),
    Disposable,
    ObservableSource<T> {

    init {
        super.set(default)
    }

    override fun set(value: T?) {
        super.set(value ?: default)
    }

    private val disposables = CompositeDisposable()

    override fun isDisposed() = disposables.isDisposed

    override fun dispose() = disposables.dispose()

    override fun subscribe(observer: Observer<in T>) {
        observable().subscribe(observer)
    }

    fun observable() = Observable.create<T> { emitter ->
        val callback = onChangeCallback {
            emitter.onNext(it)
        }

        val disposable = Disposables.fromAction {
            removeOnPropertyChangedCallback(callback)
        }

        emitter.setDisposable(disposable)
        addOnPropertyChangedCallback(callback)
        disposables.add(disposable)
    }!!

    fun single() = create<T> { emitter ->


        val callback = onChangeCallback {
            emitter.onNext(it)
            emitter.onComplete()
        }

        val disposable = LambdaDisposable {
            removeOnPropertyChangedCallback(callback)
        }

        disposables.add(disposable)
        addOnPropertyChangedCallback(callback)

    }!!
}

private fun <T> ObservableProperty<T>.onChangeCallback(
    call: OnPropertyChangedCallback.(T) -> Unit
) = object : OnPropertyChangedCallback() {

    override fun onPropertyChanged(
        sender: android.databinding.Observable,
        propertyId: Int
    ) = call(get())
}

private class LambdaDisposable(
    dispose: () -> Unit
) : AtomicReference<() -> Unit>(dispose), Disposable {

    override fun isDisposed() = get() != null

    override fun dispose() {
        getAndSet(null)?.invoke()
    }
}
