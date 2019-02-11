package io.fluks.base.android.input

import io.fluks.base.android.databinding.ObservableProperty

class InputObservable(
    private val validate: (String) -> String? = { null },
    hasFocus: Boolean = false
) {
    val text = ObservableProperty("")
    val error = ObservableProperty<String?>(null)
    val focus = ObservableProperty(hasFocus)

    val isValid get() = validate(text.value) == null
    val hasError get() = error.value != null
    val hasFocus get() = focus.get()

    fun validate() = error.run {
        value = null
        value = validate(text.value)
        value != null
    }
}

var <T> ObservableProperty<T>.value: T
    get() = get()
    set(value) {
        set(value)
    }