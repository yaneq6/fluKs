package io.fluks.feature.scheme.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import io.fluks.R
import io.fluks.base.android.BaseActivity
import io.fluks.base.weak
import io.fluks.databinding.SchemeBinding
import io.fluks.base.createDi
import io.fluks.base.dependencies
import io.fluks.base.lazyDi
import io.fluks.feature.raw.view.RawActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.scheme.*

class SchemeActivity : BaseActivity<SchemeBinding, SchemeUI.Component>() {

    override val component: SchemeUI.Component by createDi {
        SchemeUI.Module(
            app = application.dependencies(),
            getContext = weak()
        )
    }

    override val disposable by lazyDi {
        CompositeDisposable(
            super.disposable,
            gestureObservable.onScale.subscribe { detector ->
                plan.apply {
                    scaleX *= detector.scaleFactor
                    scaleY *= detector.scaleFactor
                }
            }
        )
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
            R.id.done -> dispatch(RawActivity.Start())
            else -> null
        } != null
    }

    override fun onDestroy() {
        disposable.dispose()
        binding.root.setOnTouchListener(null)
        super.onDestroy()
    }
}