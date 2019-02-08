package io.scheme.util.debug.dispatch

import android.view.ViewGroup
import io.scheme.R
import io.scheme.util.android.inflate
import io.scheme.util.core.Event
import io.scheme.util.core.util.innerName
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