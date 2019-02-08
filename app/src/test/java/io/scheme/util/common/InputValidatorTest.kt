package io.scheme.util.common

import io.scheme.util.android.input.InputObservable
import io.scheme.util.android.input.InputValidator
import io.scheme.util.android.value
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class InputValidatorTest {

    companion object {
        const val ERROR = "error"
        val VALID: String? = null
    }

    private val validate = { input: String ->
        when {
            input.isEmpty() -> ERROR
            else -> VALID
        }
    }

    private lateinit var observable1: InputObservable
    private lateinit var observable2: InputObservable

    private lateinit var validator: InputValidator

    @Before
    fun setup() {
        observable1 = InputObservable(validate)
        observable2 = InputObservable(validate)

        validator = InputValidator(
            observable1,
            observable2
        )
    }

    @Test
    fun validate1() {
        observable1.focus.apply {
            value = true
            value = false
        }
        assertEquals(ERROR, observable1.error.value)
        assertEquals(VALID, observable2.error.value)
    }

    @Test
    fun validate2() {
        observable1.focus.apply {
            value = true
            value = false
        }
        assertEquals(ERROR, observable1.error.value)
        assertEquals(ERROR, observable2.error.value)
    }
}