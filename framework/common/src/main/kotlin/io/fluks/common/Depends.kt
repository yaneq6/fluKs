package io.fluks.common

import kotlin.reflect.KClass

interface Depends<D> {
    val component: D
}

inline fun <D, T> Depends<D>.di(get: D.() -> T): T = component.get()

inline fun <D, T> Depends<D>.lazyDi(crossinline get: D.() -> T): Lazy<T> = lazy { component.get() }

inline fun <D: Any> Depends<D>.createDi(crossinline create: () -> D): Lazy<D> = lazy { this.measureCreate("dependencies") { create() } }

inline fun <reified D : Any> Any.dependencies(): D = dependencies(D::class)

@Suppress("UNCHECKED_CAST")
fun <D : Any> Any.dependencies(type: KClass<D>): D = (this as? Depends<D>)?.component
    ?: throw up("$this is not a type of ${Depends::class.qualifiedName}<${type.qualifiedName}>")