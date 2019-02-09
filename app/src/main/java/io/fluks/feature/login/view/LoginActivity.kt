package io.fluks.feature.login.view

import android.os.Bundle
import io.fluks.util.android.BaseActivity
import io.fluks.di.android.createDi
import io.fluks.di.android.dependencies
import io.fluks.databinding.LoginBinding
import io.fluks.feature.session.Session

class LoginActivity : BaseActivity<
    LoginBinding,
    LoginViewModel,
    Session.State,
    LoginUI.Component>() {

    override val component by createDi {
        LoginUI.Module(
            app = application.dependencies()
        )
    }

    override fun onCreateSafe(savedInstanceState: Bundle?) {
        super.onCreateSafe(savedInstanceState)
        setSupportActionBar(toolbar)
    }
}