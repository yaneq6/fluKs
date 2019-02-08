package io.bricks.serial.gson

import io.bricks.serial.core.JsonFactoryTest
import io.bricks.serial.core.StringJsonFactory
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized


@RunWith(Parameterized::class)
class GsonFactoryTest(
    override val expected: Any,
    override val actual: StringJsonFactory.() -> Any
) : JsonFactoryTest {

    @Test
    fun test() = assert()

    override val factory: StringJsonFactory = GsonFactory()

    companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> = JsonFactoryTest.data()
    }

}
