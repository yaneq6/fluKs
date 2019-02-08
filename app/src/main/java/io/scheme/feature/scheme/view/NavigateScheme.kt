package io.scheme.feature.scheme.view

import android.content.Context
import android.content.Intent
import io.scheme.util.android.event
import io.scheme.util.core.Action

data class NavigateScheme(
    override val finishCurrent: Boolean = false
) : Action.Navigate<Context> {
    override fun Context.navigate() {
        startActivity(
            Intent(this, SchemeActivity::class.java)
        )
    }
}