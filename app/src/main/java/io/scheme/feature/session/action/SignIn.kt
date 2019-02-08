package io.scheme.feature.session.action

import io.scheme.feature.scheme.view.NavigateScheme
import io.scheme.feature.session.Session
import io.scheme.util.core.*

data class SignIn(
    val login: String,
    val password: String
) : Action.Async {

    data class Success(
        val token: String
    ) : Session.Effect

    class Failure(
        val login: String = "",
        val password: String = ""
    ) : Session.Effect

    class Interactor(
        private val api: (Action) -> Event
    ) : AbstractInteractor<SignIn, Event> {
        override fun invoke(action: SignIn): Event = api(action).let { result ->
            when (result) {
                is Success -> Event.More(result, NavigateScheme(finishCurrent = true))
                else -> result
            }
        }
    }
}