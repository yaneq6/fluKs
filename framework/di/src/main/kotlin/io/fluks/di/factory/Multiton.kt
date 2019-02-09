package io.fluks.di.factory

import io.fluks.di.Factory

class Multiton<A, T: Any>(
    override val name: String,
    private val map: MutableMap<A, T> = mutableMapOf()
) : Factory.Repository<A, T> {

    override fun snapshot(): Map<A, T> = map

    override fun get(arg: A): T? = map[arg]

    override fun set(arg: A, value: T?) {
        value?.let { map[arg] = it } ?: map.remove(arg)
    }

    override fun clear() = map.clear()
}

fun <A, T: Any> multiton() = { name: String -> Multiton<A, T>(name) }