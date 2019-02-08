package io.bricks.cache.core

import kotlin.reflect.KClass

interface CachedProperty<T> {
    var value: T?

    interface Factory {
        fun <T: Any> create(key: String, type: KClass<T>) : CachedProperty<T>
    }
}

inline fun <F: CachedProperty.Factory, reified T: Any> F.createCache(key: String) = create(key, T::class)