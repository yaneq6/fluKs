package io.fluks.debug.dispatch

import android.view.ViewGroup
import io.fluks.base.android.inflate
import io.fluks.base.innerName
import io.fluks.base.Event
import io.fluks.debug.R
import kotlinx.android.synthetic.main.debug_event_item.view.*

fun ViewGroup.createEventItemView() = inflate<ViewGroup>(R.layout.debug_event_item)

fun ViewGroup.setEvent(
    action: Event,
    onClick: (Event) -> Unit
) = apply {
    val stringAction = action.toString().takeUnless {
        it.startsWith(context.packageName)
    } ?: action::class.innerName

    itemTextView.text = stringAction
    itemTextView.setOnClickListener { onClick(action) }
}