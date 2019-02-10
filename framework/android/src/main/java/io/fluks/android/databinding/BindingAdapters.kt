@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package io.fluks.android.databinding

import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.databinding.InverseBindingListener
import android.view.View
import android.widget.EditText

@BindingAdapter("android:error")
fun EditText.error(error: String?) {
    this.error = error
}

@BindingAdapter("android:onFocusChange")
fun View.onFocusChange(onFocusChange: InverseBindingListener?) {
    onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
        onFocusChange?.onChange()
    }
}

@BindingAdapter("android:focus")
fun EditText.setFocus(focus: Boolean) {
    if (focus) requestFocus()
}

@InverseBindingAdapter(attribute = "android:focus", event = "android:onFocusChange")
fun View.getFocus(): Boolean = hasFocus()

