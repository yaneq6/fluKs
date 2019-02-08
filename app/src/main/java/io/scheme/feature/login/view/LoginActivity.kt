package io.scheme.feature.login.view

import android.os.Bundle
import io.scheme.util.android.BaseActivity
import io.scheme.util.di.createDi
import io.scheme.util.di.dependencies
import io.scheme.databinding.LoginBinding
import io.scheme.feature.session.Session

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