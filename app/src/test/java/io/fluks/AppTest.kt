package io.fluks

import io.fluks.base.Event
import io.fluks.base.di
import io.fluks.feature.scheme.view.SchemeUI
import io.fluks.feature.session.action.SignIn
import org.junit.Test

class AppTest : BaseTest() {
    @Test
    fun `should login properly`(): Unit = di {
        val expected = arrayOf(
            SignIn("login", "password"),
            SignIn.Success("token"),
            SchemeUI.Navigate(true),
            Event.Success,
            Event.Success
        )

        val actual = eventsManager.events.test()

        dispatch(expected.first())

        actual.assertValues(*expected)
    }
}
