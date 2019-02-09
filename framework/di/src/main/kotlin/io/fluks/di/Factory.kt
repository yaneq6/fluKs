package io.fluks.di

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Factory<in R : Any, A, T> internal constructor(
    private val getRepository: ((String) -> Repository<A, T>)? = null,
    private val create: R.(A) -> T
) :
    ReadOnlyProperty<R, (A) -> T> {

    private var repository: Repository<A, T>? = null

    override fun getValue(thisRef: R, property: KProperty<*>): (A) -> T = { arg ->
        thisRef.getRepository(property)
            ?.run { get(arg) ?: set(arg, thisRef.create(arg)); get(arg) }
            ?: thisRef.create(arg)
    }

    private fun R.getRepository(property: KProperty<*>) = repository ?: getRepository?.let { get ->
        get(property.name).also {
            repository = it
            addStore(it)
        }
    }

    interface Repository<A, T> : Dependency {
        operator fun get(arg: A): T?
        operator fun set(arg: A, value: T?)
        override fun snapshot(): Map<A, T>
    }
}

fun <R : Any, A, T> create(
    getRepository: ((String) -> Factory.Repository<A, T>)? = null,
    create: R.(A) -> T
): ReadOnlyProperty<R, (A) -> T> = Factory(
    getRepository = getRepository,
    create = create
)