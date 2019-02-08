package io.bricks.serial.core

import org.junit.Assert.assertEquals

fun data(vararg pair: Pair<Any, StringJsonFactory.() -> Any>): Collection<Array<Any>> = pair.map { arrayOf(it.first, it.second) }

interface JsonFactoryTest {

    fun assert() = assertEquals(expected, actual(factory))

    val expected: Any
    val actual: StringJsonFactory.() -> Any

    val factory: StringJsonFactory

    companion object {

        fun data() = data(

            JsonSource(
                dataType = TestDataClass::class,
                source = """{"int":0}"""
            ) to factory {
                TestDataClass(0).serialize()
            },

            JsonSource(
                dataType = TestObject::class,
                source = "{}"
            ) to factory {
                TestObject.serialize()
            },

            TestDataClass(0) to factory {
                JsonSource(
                    dataType = TestDataClass::class,
                    source = """{"int":0}"""
                ).deserialize()
            },

            TestObject to factory {
                JsonSource(
                    dataType = TestObject::class,
                    source = "{}"
                ).deserialize()
            }
        )

        private fun factory(init: StringJsonFactory.() -> Any) = init

    }

    data class TestDataClass(val int: Int = 0)

    @Suppress("EqualsOrHashCode")
    object TestObject {
        override fun hashCode(): Int = javaClass.name.hashCode()
        override fun equals(other: Any?): Boolean = hashCode() == other?.hashCode()
    }
}
