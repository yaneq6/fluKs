package io.scheme.feature.login.view

import io.scheme.util.android.BaseFragment
import io.scheme.util.di.dependencies
import io.scheme.databinding.LoginBinding
import io.scheme.feature.session.Session

class LoginFragment : BaseFragment<LoginBinding, LoginViewModel, Session.State, LoginUI.Component>() {

    override val component: LoginUI.Component by lazy {
        LoginUI.Module(
            app = activity!!.dependencies()
        )
    }
}