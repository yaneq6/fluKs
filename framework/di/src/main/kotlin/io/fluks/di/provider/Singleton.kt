package io.fluks.di.provider

import io.fluks.di.Provider

class Singleton<T : Any>(
    override val name: String
) : Provider.Repository<T> {
    private var value: T? = null

    override fun get(): T? = value
    override fun set(value: T?) {
        this.value = value
    }
    override fun clear() {
        value = null
    }
}

fun <T : Any> singleton() = { name: String -> Singleton<T>(name) }