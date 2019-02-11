package io.fluks.feature.login.view

import android.os.Bundle
import io.fluks.base.android.BaseActivity
import io.fluks.databinding.LoginBinding
import io.fluks.base.createDi
import io.fluks.base.dependencies

class LoginActivity : BaseActivity<LoginBinding, LoginUI.Component>() {

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