package io.bricks.serial.core

import kotlin.reflect.KClass

interface JsonFactory<S> {
    fun <T: Any> T.serialize(): JsonSource<S>
    fun <T: Any> JsonSource<S>.deserialize(): T
}

inline fun <reified T: Any, reified S> JsonFactory<S>.serialize(arg: T): S = arg.serialize().source
inline fun <reified T: Any, reified S> JsonFactory<S>.deserialize(source: S): T = JsonSource(T::class, source).deserialize()

operator fun <D, R> JsonFactory<D>.invoke(block: JsonFactory<D>.() -> R) = run(block)

data class JsonSource<S>(
    val dataType: KClass<*>,
    val source: S
)

inline fun <reified T, S> json(source: S) = JsonSource(T::class, source)

typealias StringJsonFactory = JsonFactory<String>