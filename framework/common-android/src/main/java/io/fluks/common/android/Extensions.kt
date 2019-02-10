package io.fluks.common.android

import android.content.Context
import android.content.Intent
import android.databinding.Observable
import android.databinding.ObservableField
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer

val LayoutContainer.context get() = containerView?.context
val LayoutContainer.applicationContext get() = context?.applicationContext

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline fun <reified T> Bundle.getSerializable(key: String) : T? = getSerializable(key) as? T
inline fun <reified T> Intent.getSerializableExtra(name: String) : T? = getSerializableExtra(name) as? T


@Suppress("UNCHECKED_CAST")
fun <V : View> ViewGroup.inflate(
    @LayoutRes layoutRes: Int,
    attachToParent: Boolean = false
): V = inflater.inflate(layoutRes, this, false).also {
    if (attachToParent) addView(it)
} as V

val View.inflater get() = LayoutInflater.from(context)!!


@Suppress("UNCHECKED_CAST")
fun <S, T> onPropertyChangedCallback(sender: S, onChanged: S.(T) -> Unit) =
    object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: Observable, propertyId: Int) {
            when (observable) {
                is ObservableField<*> -> observable.run { onChanged.invoke(sender, value as T)  }
            }
        }
    }

var <T> ObservableField<T>.value: T?
    get() = get()
    set(value) {
        set(value)
    }

typealias GetContext = () -> Context?
