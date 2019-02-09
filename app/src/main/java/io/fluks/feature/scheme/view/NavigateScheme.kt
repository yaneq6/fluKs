package io.fluks.feature.scheme.view

import android.content.Context
import android.content.Intent
import io.fluks.core.Action

data class NavigateScheme(
    override val finishCurrent: Boolean = false
) : Action.Navigate<Context> {
    override fun Context.navigate() {
        startActivity(
            Intent(this, SchemeActivity::class.java)
        )
    }
}