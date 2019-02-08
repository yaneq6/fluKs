package io.scheme.util.debug.dependency

import android.view.ViewGroup
import io.scheme.R
import io.scheme.util.android.inflate
import io.scheme.util.di.Snapshot
import kotlinx.android.synthetic.main.debug_dependency_item.view.*

fun ViewGroup.createDependencyItemView(): ViewGroup = inflate(R.layout.debug_dependency_item)

fun ViewGroup.setSnapshot(snapshot: Snapshot) = apply {
    itemTextView.text = snapshot.getItemText()
}

private fun Snapshot.getItemText() =
    "$name: ${dependency!!::class.simpleName} (${type.simpleName})"