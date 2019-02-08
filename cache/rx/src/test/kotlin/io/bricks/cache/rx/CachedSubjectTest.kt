package io.bricks.cache.rx

import io.bricks.cache.core.CachedProperty
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CachedSubjectTest {

    private val property = TestCachedProperty()
    private val behaviorSubject get() = BehaviorSubject.create<String>()

    @Before
    fun setup() {
        property.value = null
    }

    @Test
    fun shouldInitWithPersistedValue() {
        val expected = "persisted".also(property::value::set)
        val test = behaviorSubject.cached(property).test()

        test.assertValue(expected)
    }

    @Test
    fun shouldInitWithDefaultValue() {
        val expected = "default"
        val test = behaviorSubject.cached(property) { expected }.test()

        test.assertValue(expected)
    }

    @Test
    fun shouldInitWithoutValue() {
        val test = behaviorSubject.cached(property).test()

        test.assertNoValues()
    }

    @Test
    fun shouldPersistNextValue() {
        val expected = "next"
        val subject = behaviorSubject.cached(property)
        val test = subject.test()

        subject.onNext(expected)

        assertEquals(expected, property.value)
        test.assertValue(expected)
    }

    private class TestCachedProperty : CachedProperty<String> {
        override var value: String? = null
    }
}