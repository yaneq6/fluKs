package io.fluks.feature.login.view

import android.os.Bundle
import io.fluks.base.*
import io.fluks.base.android.BaseActivity
import io.fluks.base.android.notInvisible
import io.fluks.databinding.LoginBinding
import io.fluks.feature.session.Session
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.default_toolbar.*

class LoginActivity : BaseActivity<LoginBinding, LoginUI.Component>() {

    override val component by createDi {
        LoginUI.Module(
            app = application.dependencies()
        )
    }

    override val disposable by lazy {
        CompositeDisposable(
            subscribeProgressBar()
        )
    }

    private fun subscribeProgressBar() = di {
        eventsManager.status
            .isRunning<Session.Async>()
            .subscribe(progress::notInvisible::set)
    }

    override fun onCreateSafe(savedInstanceState: Bundle?) {
        super.onCreateSafe(savedInstanceState)
        setSupportActionBar(toolbar)
    }
}