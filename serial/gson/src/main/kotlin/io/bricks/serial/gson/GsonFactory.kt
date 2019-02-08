package io.bricks.serial.gson

import com.google.gson.GsonBuilder
import io.bricks.serial.core.JsonSource
import io.bricks.serial.core.StringJsonFactory

class GsonFactory : StringJsonFactory {

    private val gson = GsonBuilder().create()

    override fun <T : Any> T.serialize() = JsonSource(javaClass.kotlin, gson.toJson(this))

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> JsonSource<String>.deserialize(): T = gson.fromJson(source, dataType.java) as T
}