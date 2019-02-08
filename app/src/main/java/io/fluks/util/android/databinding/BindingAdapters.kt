@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package io.fluks.util.android.databinding

import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.databinding.InverseBindingListener
import android.view.View
import android.widget.EditText

@BindingAdapter("app:error")
fun EditText.error(error: String?) {
    this.error = error
}

@BindingAdapter("app:onFocusChange")
fun View.onFocusChange(onFocusChange: InverseBindingListener?) {
    onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
        onFocusChange?.onChange()
    }
}

@BindingAdapter("app:focus")
fun EditText.setFocus(focus: Boolean) {
    if (focus) requestFocus()
}

@InverseBindingAdapter(attribute = "app:focus", event = "app:onFocusChange")
fun View.getFocus(): Boolean = hasFocus()

