package io.fluks.feature.scheme.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import io.fluks.R
import io.fluks.base.*
import io.fluks.base.android.BaseActivity
import io.fluks.base.android.notInvisible
import io.fluks.databinding.SchemeBinding
import io.fluks.feature.raw.view.RawActivity
import io.fluks.feature.session.Session
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.default_toolbar.*
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
            subscribeGestures(),
            subscribeProgressBar()
        )
    }

    private fun subscribeGestures() = di {
        gestureObservable.onScale.subscribe { detector ->
            plan.apply {
                scaleX *= detector.scaleFactor
                scaleY *= detector.scaleFactor
            }
        }
    }

    private fun subscribeProgressBar() = di {
        eventsManager.status
            .isRunning<Session.Async>()
            .subscribe(progress::notInvisible::set)
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