package io.scheme.util.core.util

import java.lang.ref.WeakReference

class WeakProvider<T>(
    reference: T
) : () -> T? {
    override fun invoke(): T? = weakReference.get()
    private val weakReference = WeakReference(reference)
}

fun <T : Any> T.weakProvider() = WeakProvider(this)
