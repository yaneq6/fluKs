package io.fluks.feature.login.view

import io.fluks.android.BaseFragment
import io.fluks.di.android.dependencies
import io.fluks.databinding.LoginBinding
import io.fluks.feature.session.Session

class LoginFragment : BaseFragment<LoginBinding, LoginViewModel, Session.State, LoginUI.Component>() {

    override val component: LoginUI.Component by lazy {
        LoginUI.Module(
            app = activity!!.dependencies()
        )
    }
}