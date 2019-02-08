package io.fluks.feature.scheme.view

import android.databinding.ObservableField
import io.fluks.util.core.Dispatch
import io.fluks.util.core.Event
import io.fluks.util.core.Model
import io.fluks.feature.session.action.SignOut

class SchemeViewModel(
    private val dispatch: Dispatch<Event>
) :
    Model(),
    Dispatch<Event> by dispatch {

    val field = ObservableField<String>()

    fun logout() = dispatch(SignOut())
}