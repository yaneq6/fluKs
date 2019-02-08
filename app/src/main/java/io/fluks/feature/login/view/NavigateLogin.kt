package io.fluks.feature.login.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import io.fluks.util.core.Action

data class NavigateLogin(
    override val finishCurrent: Boolean = false
) : Action.Navigate<Context> {

    override fun Context.navigate() {

        startActivity(Intent(this, LoginActivity::class.java))

        if (finishCurrent && this is Activity) finish()
    }
}