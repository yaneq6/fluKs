@file:Suppress("NOTHING_TO_INLINE")

package io.scheme.util.core

import io.scheme.BuildConfig
import io.scheme.util.core.util.innerName
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KClass
import kotlin.system.measureTimeMillis


inline fun <T : Any, R> T.tag(block: T.() -> R) = let {
    Timber.tag(this::class.innerName)
    block()
}

inline fun <T : Any> T.tag() = Timber.tag(this::class.innerName)

fun <T: Any, R> T.measureCreate(name: String = "", block: () -> R): R =
    measure("create $name", block)

fun <T: Any, R> T.measureDispatch(any: Any, block: () -> R): R =
    measure("dispatch $any", block)


fun <State : Any> StateHolder.Repository<State>.measureRead(): State? =
    measure("read $name") { state }


fun <State : Any> StateHolder.Repository<State>.measureWrite(state: State?) =
    measure("write $name") { this.state = state }

fun <T: Any, R> T.measure(message: Any = "", execute: () -> R): R =
    if (!BuildConfig.DEBUG) execute()
    else {
        val ref = AtomicReference<R>()
        measureTimeMillis {
            ref.set(execute())
        }.also { time ->
            val logResult = (ref.get()?.takeIf { it !is Unit } ?: "")
            tag()
            Timber.d("$message $logResult in ${time}ms")
        }
        ref.get()
    }


fun Any.logDispatch(record: Middleware.Record<Event>) = tag {
    record.takeIf { it.event !is Event.Success }?.run {
        Timber.d("dispatch: $event \n after: ${predecessors
            .takeUnless { it.isEmpty() }
            ?.map { it::class.simpleName }
            ?.reduce { acc, s -> "$acc -> $s" }
        }")
    }
}

fun Any.logSuccess(record: Middleware.Record<Event>) = tag {
    record.takeIf { it.event is Event.Success }?.run {
        Timber.d("successful in: ${timestamp - predecessors.last().second}\n ${predecessors
            .takeUnless { it.isEmpty() }
            ?.map { it::class.innerName }
            ?.reduce { acc, s -> "$acc -> $s" }
        }")
    }
}

fun Any.logNullEvent() = tag {
    Timber.d("trying dispatch null action")
}

fun timestamp() = System.currentTimeMillis()
