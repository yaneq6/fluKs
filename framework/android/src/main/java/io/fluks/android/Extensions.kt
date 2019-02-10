package io.fluks.android

import android.content.Intent
import io.fluks.common.android.getSerializableExtra
import io.fluks.core.Event

var Intent.event: Event?
    get() = getSerializableExtra<Event>("event")
    set(value) {
        putExtra("event", value)
    }