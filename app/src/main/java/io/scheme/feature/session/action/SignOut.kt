package io.scheme.feature.session.action

import io.scheme.feature.session.Session
import io.scheme.util.core.AbstractInteractor
import io.scheme.util.core.Action
import io.scheme.util.core.Event

class SignOut: Action.Async {

    class Success: Session.Effect

    class Interactor(
        val api: (Action) -> Event
    ): AbstractInteractor<SignOut, Event> {
        override fun invoke(action: SignOut) = api(action)
    }
}