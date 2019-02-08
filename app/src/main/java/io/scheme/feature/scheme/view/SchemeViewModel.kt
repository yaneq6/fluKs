package io.scheme.feature.scheme.view

import android.databinding.ObservableField
import io.scheme.util.core.Dispatch
import io.scheme.util.core.Event
import io.scheme.util.core.Model
import io.scheme.feature.session.action.SignOut

class SchemeViewModel(
    private val dispatch: Dispatch<Event>
) :
    Model(),
    Dispatch<Event> by dispatch {

    val field = ObservableField<String>()

    fun logout() = dispatch(SignOut())
}