package io.fluks.feature.session.action

import io.fluks.feature.session.Session

class SignOut: Session.Async {
    class Success: Session.Effect
}