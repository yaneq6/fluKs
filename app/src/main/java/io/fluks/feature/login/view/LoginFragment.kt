package io.fluks.feature.login.view

import io.fluks.android.BaseFragment
import io.fluks.databinding.LoginBinding
import io.fluks.common.dependencies

class LoginFragment : BaseFragment<LoginBinding, LoginUI.Component>() {

    override val component: LoginUI.Component by lazy {
        LoginUI.Module(
            app = activity!!.dependencies()
        )
    }
}