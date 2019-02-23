package io.fluks

import io.fluks.base.*
import io.fluks.core.Middleware
import io.fluks.di.Dependencies
import io.mockk.mockk
import io.reactivex.observers.BaseTestConsumer

abstract class BaseTest : Depends<App.Component> {

    protected val app: App = mockk(relaxed = true)

    override val component by createDi {
        App.Module(
            core = CoreModuleMock(
                context = app,
                dependencies = Dependencies()
            ),
            init = InitModuleMock
        )
    }
}

fun <T, U : BaseTestConsumer<T, U>> BaseTestConsumer<T, U>.assertLast(
    expected: T
): U = values().first().let { actual ->
    expected == actual || throw AssertionError(
        """
            Values are different.
            Expected: $expected
            Actual:   $actual
        """.trimIndent()
    )
    assertSubscribed()
}

fun expectedRecord(
    context: Any,
    vararg events: Event
) = events.reversed().let { reversed ->
    var timestamp = 0L
    Middleware.Record(
        event = reversed.first(),
        context = context.weak(),
        timestamp = timestamp++,
        predecessors = reversed.drop(1).map {
            it to timestamp++
        }
    )
}