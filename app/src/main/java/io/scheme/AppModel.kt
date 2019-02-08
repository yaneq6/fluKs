package io.scheme

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.scheme.util.core.*
import io.scheme.feature.login.view.NavigateLogin
import io.scheme.feature.scheme.view.NavigateScheme
import io.scheme.feature.session.Session
import io.scheme.feature.session.action.SignIn
import io.scheme.feature.session.action.SignOut

class AppModel(
    private val sessionStore: Store<Session.Effect, Session.State>,
    dispatch: Dispatch<Action>
) :
    Model(),
    Dispatch<Action> by dispatch {

    override fun subscribe(): Disposable = CompositeDisposable(
        sessionStore.subscribe { effect ->
            when (effect) {
//                is SignIn.Success -> dispatch(NavigateScheme(
//                    finishCurrent = true
//                ))
                is SignOut.Success -> dispatch(NavigateLogin(
                    finishCurrent = true
                ))
            }
        }
    )
}