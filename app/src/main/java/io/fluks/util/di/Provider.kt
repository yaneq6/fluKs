@file:Suppress("ReplaceSingleLineLet")

package io.fluks.util.di

import io.fluks.util.core.measureCreate
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Provider<in R : Any, D : Any> internal constructor(
    private var getRepository: ((String) -> Repository<D>)? = null,
    private val create: R.(name: String) -> D
) :
    ReadOnlyProperty<R, D> {

    private var repository: Repository<D>? = null

    override fun getValue(thisRef: R, property: KProperty<*>): D = thisRef.run {
        getRepository(property)?.run {
            get() ?: thisRef.measureCreate(property.name) {
                create(property.name).also(this::set)
            }
        } ?: create(property.name)
    }

    private fun R.getRepository(property: KProperty<*>): Repository<D>? =
        repository ?: getRepository?.invoke(property.name)?.also {
            repository = it
            getRepository = null
            addStore(it)
        }

    interface Repository<T : Any> : Dependency {
        fun get(): T?
        fun set(value: T?)
        override fun snapshot(): T? = get()
    }
}

fun <R : Any, T : Any> R.provide(
    getRepository: ((String) -> Provider.Repository<T>)? = null,
    create: R.(name: String) -> T
): ReadOnlyProperty<R, T> = Provider(
    getRepository = getRepository,
    create = create
)