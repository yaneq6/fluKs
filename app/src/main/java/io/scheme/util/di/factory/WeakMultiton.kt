package io.scheme.util.di.factory

import io.scheme.util.di.Factory
import java.util.*

class WeakMultiton<A, T : Any>(name: String) :
    Factory.Repository<A, T> by Multiton<A, T>(name, WeakHashMap())

fun <A, T : Any> weakMultiton() = { name: String -> WeakMultiton<A, T>(name) }