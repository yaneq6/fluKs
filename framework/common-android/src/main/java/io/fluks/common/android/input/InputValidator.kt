package io.fluks.common.android.input

import io.reactivex.disposables.CompositeDisposable

class InputValidator(
    private vararg val inputs: InputObservable
) {

    private val disposable = CompositeDisposable(inputs.map { input ->
        input.focus.observable()
            .filter(Boolean::not)
            .subscribe { validate(input) }
    })

    fun validate(input: InputObservable? = null) = inputs
        .takeWhile { it !== input }
        .plus(listOfNotNull(input))
        .mapNotNull { it.apply { validate() }.error.value }
        .all(String::isBlank)

    val allValid get() = inputs.all(InputObservable::isValid)

    fun dispose() = disposable.dispose()
}