package io.scheme.util.android.input

import io.reactivex.disposables.CompositeDisposable
import io.scheme.util.android.value

class InputValidator(
    private vararg val inputs: InputObservable
) {

    private val disposables = CompositeDisposable()

    init {
        inputs.forEach { it.bind() }
    }

    private fun InputObservable.bind() = disposables.addAll(
        focus.observable().filter(Boolean::not).subscribe {
            validate(this)
        }
    )

    val allValid get() = inputs.all(InputObservable::isValid)

    fun validate(input: InputObservable? = null) = inputs
        .takeWhile { it !== input }
        .plus(listOfNotNull(input))
        .mapNotNull { it.apply { validate() }.error.value }
        .all(String::isBlank)

    fun dispose() = disposables.dispose()
}