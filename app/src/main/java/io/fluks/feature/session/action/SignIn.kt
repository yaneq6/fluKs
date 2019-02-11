package io.fluks.feature.session.action

import io.fluks.feature.session.Session

data class SignIn(
    val login: String,
    val password: String
) : Session.Async {

    data class Success(
        val token: String
    ) : Session.Effect

    class Failure(
        val login: String = "",
        val password: String = ""
    ) : Session.Effect
}