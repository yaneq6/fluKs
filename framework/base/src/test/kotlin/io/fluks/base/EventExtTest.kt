package io.fluks.base

import io.reactivex.Observable
import org.junit.Test

class EventExtTest {

    @Test
    fun isRunning() {
        Observable.fromArray(
            Event.Status.Empty,
            Event.Status.Empty.copy(
                event = Event.Success,
                isRunning = true
            )
        ).isRunning<Event.Success>()
            .test()
            .assertValues(true)
    }
}