package io.fluks.util.android

import android.content.Intent
import io.fluks.common.android.getSerializableExtra
import io.fluks.core.Event
import io.fluks.util.android.databinding.ObservableProperty

var Intent.event: Event?
    get() = getSerializableExtra<Event>("event")
    set(value) {
        putExtra("event", value)
    }

var <T> ObservableProperty<T>.value: T
    get() = get()
    set(value) {
        set(value)
    }