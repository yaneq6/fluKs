package io.fluks.util.debug.dispatch

import android.view.ViewGroup
import io.fluks.R
import io.fluks.util.android.inflate
import io.fluks.util.core.Event
import io.fluks.util.core.util.innerName
import kotlinx.android.synthetic.main.debug_action_item.view.*

fun ViewGroup.createEventItemView() = inflate<ViewGroup>(R.layout.debug_action_item)

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