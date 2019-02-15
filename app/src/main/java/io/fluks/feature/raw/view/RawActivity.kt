package io.fluks.feature.raw.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.fluks.R
import io.fluks.base.Action

class RawActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.raw)
    }

    class Start : Action.Navigate<Context> {
        override fun Context.navigate() {
            startActivity(Intent(this, RawActivity::class.java))
        }
    }
}