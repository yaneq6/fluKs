package io.bricks.cache.rx

import io.bricks.cache.core.CachedProperty
import io.reactivex.subjects.BehaviorSubject

fun <T> BehaviorSubject<T>.cached(cachedProperty: CachedProperty<T>, default: (() -> T)? = null) = apply {
    (cachedProperty.value ?: default?.invoke())?.let { onNext(it) }
    subscribe { cachedProperty.value = it }
}