package io.fluks.feature.scheme.view

import io.fluks.base.Dispatch
import io.fluks.base.Event
import io.fluks.core.Model
import io.fluks.feature.session.action.SignOut

class SchemeViewModel(
    private val dispatch: Dispatch<Event>
) :
    Model(),
    Dispatch<Event> by dispatch {

    fun logout() = dispatch(SignOut())
}