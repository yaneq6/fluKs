package io.bricks.serial.jackson

import io.bricks.serial.core.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class JacksonFactoryTest(
    override val expected: Any,
    override val actual: StringJsonFactory.() -> Any
) : JsonFactoryTest {

    override val factory: StringJsonFactory = JacksonFactory()

    @Test
    fun test() = assert()

    companion object {

        @JvmStatic
        @Parameters
        fun data() = JsonFactoryTest.data()
    }

}
