@file:Suppress("NOTHING_TO_INLINE")

package io.fluks.common

import kotlin.reflect.KClass

val KClass<*>.innerName
    get() = qualifiedName?.run {
        split(".").filter {
            it.first().isUpperCase()
        }.reduce { acc, s -> "$acc.$s" }
    }

inline fun up(vomit: String) = Exception(vomit)

infix fun <T1, T2, T3> Pair<T1, T2>.to(third: T3) = Triple(first, second, third)

@Suppress("UNCHECKED_CAST")
inline fun <T> Any.unsafe() = this as T

inline fun <reified T> Any.applyWhen(block: T.() -> Unit) = (this as? T)?.apply(block)

inline fun <reified T, R> Any.runWhen(block: T.() -> R?): R? = (this as? T)?.run(block)