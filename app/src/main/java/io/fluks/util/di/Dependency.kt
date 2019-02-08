package io.fluks.util.di

import io.fluks.util.core.util.applyWhen
import java.util.*
import kotlin.reflect.KClass

interface Dependency {
    val name: String
    fun snapshot(): Any?
    fun clear()
}

interface DependenciesAccumulator : Dependency, (KClass<*>, Dependency) -> Unit {
    override fun snapshot(): Collection<Snapshot>
}

class Dependencies :
    DependenciesAccumulator,
    MutableMap<Dependency, StoreTag> by WeakHashMap() {
    override fun snapshot(): Collection<Snapshot> = map { (store, tag) ->
        Snapshot(
            name = tag.name,
            module = tag.parent,
            dependency = store.snapshot(),
            type = store::class
        )
    }

    override val name: String = this::class.qualifiedName!!

    override fun invoke(component: KClass<*>, store: Dependency) {
        put(
            value = StoreTag(store.name, component),
            key = store
        )
    }
}

internal fun Any.addStore(store: Dependency) = applyWhen<DependenciesAccumulator> {
    invoke(this::class, store)
}

data class StoreTag(
    val name: String,
    val parent: KClass<*>
)

data class Snapshot(
    val name: String,
    val type: KClass<*>,
    val module: KClass<*>,
    val dependency: Any?
)

fun Snapshot.hasDependency() = dependency != null

typealias Snapshots = Collection<Snapshot>

fun Snapshots.filterBy(
    name: String? = null,
    type: KClass<*>? = null,
    parent: KClass<*>? = null,
    getValue: ((Any?) -> Boolean)? = null
) = this
    .filterEqual(name) { it.name }
    .filterEqual(type) { it.type }
    .filterEqual(parent) { it.module }
    .run { getValue?.run { filter { invoke(it.dependency) } } ?: this  }

private inline fun Snapshots.filterEqual(value: Any?, get: (Snapshot) -> Any?) = value?.let {
    filter { snapshot -> get(snapshot) == value }
} ?: this