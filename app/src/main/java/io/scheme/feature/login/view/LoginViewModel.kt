package io.scheme.feature.login.view

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.scheme.util.android.input.InputObservable
import io.scheme.util.android.input.InputValidator
import io.scheme.util.android.value
import io.scheme.util.core.*
import io.scheme.feature.session.Session
import io.scheme.feature.session.action.SignIn
import io.scheme.feature.scheme.view.SchemeUI as SchemeView

class LoginViewModel(
    private val store: Store<Session.Effect, Session.State>,
    dispatch: Dispatch<Event>
) :
    Dispatch<Event> by dispatch,
    Model()
{
    val login = InputObservable(
        hasFocus = true,
        validate = { if (it.length < 6) "login must have at last 6 character" else null }
    )

    val password = InputObservable(
        validate = { if (it.length < 6) "password must have at last 6 character" else null }
    )

    fun signIn() = validator.validate().let { isValid ->
        if (isValid) dispatch(SignIn(
            login = login.text.value,
            password = password.text.value
        ))
    }

    private val validator = InputValidator(
        login,
        password
    )

    override fun subscribe(): Disposable = CompositeDisposable(
        store.subscribe { effect ->
            when (effect) {
                is SignIn.Failure -> {
                    login.error.set(effect.login)
                    password.error.set(effect.password)
                }
            }
        }
    )
}
