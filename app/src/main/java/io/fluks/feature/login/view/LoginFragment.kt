package io.fluks.feature.login.view

import io.fluks.util.android.BaseFragment
import io.fluks.util.di.dependencies
import io.fluks.databinding.LoginBinding
import io.fluks.feature.session.Session

class LoginFragment : BaseFragment<LoginBinding, LoginViewModel, Session.State, LoginUI.Component>() {

    override val component: LoginUI.Component by lazy {
        LoginUI.Module(
            app = activity!!.dependencies()
        )
    }
}