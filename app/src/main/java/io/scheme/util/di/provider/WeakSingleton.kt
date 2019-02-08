package io.scheme.util.di.provider

import io.scheme.util.core.util.weak
import io.scheme.util.di.Provider
import java.lang.ref.WeakReference

class WeakSingleton<T : Any>(
    override val name: String
) : Provider.Repository<T> {

    private var ref = WeakReference<T>(null)

    override fun get(): T? = ref.get()

    override fun set(value: T?) = value?.let {
        ref = it.weak().also { ref.clear() }
    } ?: ref.clear()

    override fun clear() = ref.clear()
}

fun <T : Any> weakSingleton() = { name: String -> WeakSingleton<T>(name) }