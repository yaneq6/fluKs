package io.fluks.feature.scheme.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import io.fluks.R
import io.fluks.util.android.BaseActivity
import io.fluks.util.di.createDi
import io.fluks.util.di.dependencies
import io.fluks.util.di.lazyDi
import io.fluks.util.core.util.weak
import io.fluks.databinding.SchemeBinding
import io.fluks.feature.scheme.Scheme
import kotlinx.android.synthetic.main.scheme.*

class SchemeActivity :
    BaseActivity<
        SchemeBinding,
        SchemeViewModel,
        Scheme.State,
        SchemeUI.Component>() {

    override val component: SchemeUI.Component by createDi {
        SchemeUI.Module(
            app = application.dependencies(),
            getContext = weak()
        )
    }

    private val disposable by lazyDi {
        gestureObservable.onScale.subscribe { detector ->
            plan.apply {
                scaleX *= detector.scaleFactor
                scaleY *= detector.scaleFactor
            }
        }
    }

    override fun onCreateSafe(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        disposable
        binding.root.setOnTouchListener(component.touchListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean = true.also {
        MenuInflater(this).inflate(R.menu.scheme, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = component.viewModel.run {
        when (item.itemId) {
            R.id.logout -> logout()
            else -> null
        } != null
    }

    override fun onDestroy() {
        disposable.dispose()
        binding.root.setOnTouchListener(null)
        super.onDestroy()
    }
}