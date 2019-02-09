package io.fluks

import io.fluks.core.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.fluks.feature.login.view.NavigateLogin
import io.fluks.feature.session.Session
import io.fluks.feature.session.action.SignOut

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