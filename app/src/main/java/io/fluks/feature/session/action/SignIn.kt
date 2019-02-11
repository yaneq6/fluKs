package io.fluks.feature.session.action

import io.fluks.core.AbstractInteractor
import io.fluks.core.Action
import io.fluks.common.Event
import io.fluks.feature.scheme.view.NavigateScheme
import io.fluks.feature.session.Session

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