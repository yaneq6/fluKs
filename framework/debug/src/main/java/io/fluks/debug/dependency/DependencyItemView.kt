package io.fluks.debug.dependency

import android.view.ViewGroup
import io.fluks.base.android.inflate
import io.fluks.debug.R
import io.fluks.di.Snapshot
import kotlinx.android.synthetic.main.debug_dependency_item.view.*

fun ViewGroup.createDependencyItemView(): ViewGroup = inflate(R.layout.debug_dependency_item)

fun ViewGroup.setSnapshot(snapshot: Snapshot) = apply {
    itemTextView.text = snapshot.getItemText()
}

private fun Snapshot.getItemText() =
    "$name: ${dependency!!::class.simpleName} (${type.simpleName})"