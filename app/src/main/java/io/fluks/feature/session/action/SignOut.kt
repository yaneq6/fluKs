package io.fluks.feature.session.action

import io.fluks.feature.session.Session
import io.fluks.core.AbstractInteractor
import io.fluks.common.Action
import io.fluks.common.Event

class SignOut: Action.Async {

    class Success: Session.Effect

    class Interactor(
        val api: (Action) -> Event
    ): AbstractInteractor<SignOut, Event> {
        override fun invoke(action: SignOut) = api(action)
    }
}