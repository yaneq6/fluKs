package io.fluks.util.core.util

import java.lang.ref.WeakReference

class WeakProvider<T>(
    reference: T
) : WeakReference<T>(reference), () -> T? {
    override fun invoke(): T? = get()
    override fun toString() = "Weak(${get()})"
}

fun <T> T.weak() = WeakProvider(this)