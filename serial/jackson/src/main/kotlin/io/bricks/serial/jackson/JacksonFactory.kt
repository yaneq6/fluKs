package io.bricks.serial.jackson

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.bricks.serial.core.JsonSource
import io.bricks.serial.core.StringJsonFactory

class JacksonFactory : StringJsonFactory {

    private val jackson = jacksonObjectMapper()

    override fun <T : Any> T.serialize() = JsonSource(javaClass.kotlin, jackson.writeValueAsString(this))

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> JsonSource<String>.deserialize(): T = jackson.readValue(source, dataType.java) as T

}
