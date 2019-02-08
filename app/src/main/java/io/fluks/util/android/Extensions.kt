package io.fluks.util.android

import android.content.Context
import android.content.Intent
import android.databinding.Observable
import android.databinding.ObservableField
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.fluks.util.android.databinding.ObservableProperty
import io.fluks.util.core.Event
import kotlinx.android.extensions.LayoutContainer

val LayoutContainer.context get() = containerView?.context
val LayoutContainer.applicationContext get() = context?.applicationContext

var Intent.event: Event?
    get() = getSerializableExtra<Event>("event")
    set(value) {
        putExtra("event", value)
    }

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

var <T> ObservableField<T>.value: T?
    get() = get()
    set(value) {
        set(value)
    }

var <T> ObservableProperty<T>.value: T
    get() = get()
    set(value) {
        set(value)
    }

@Suppress("UNCHECKED_CAST")
fun <S, T> onPropertyChangedCallback(sender: S, onChanged: S.(T) -> Unit) =
    object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: Observable, propertyId: Int) {
            when (observable) {
                is ObservableField<*> -> observable.run { onChanged.invoke(sender, value as T)  }
            }
        }
    }

inline fun <reified T> parcelableCreator(
    crossinline create: () -> T
) = object : Parcelable.Creator<T> {

    override fun createFromParcel(source: Parcel?): T = create()

    override fun newArray(size: Int): Array<T> = emptyArray<T>()
}

typealias GetContext = () -> Context?
