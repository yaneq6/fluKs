@file:Suppress("NOTHING_TO_INLINE")

package io.fluks.common

import java.util.concurrent.atomic.AtomicReference
import kotlin.system.measureTimeMillis

typealias FluksLog = Log

interface Log {

    fun log(
        level: Int,
        message: String? = null,
        throwable: Throwable? = null,
        tag: Any? = null
    )

    fun tag(tag: Any)

    val debug: Boolean

    companion object {
        const val VERBOSE = 2
        const val DEBUG = 3
        const val INFO = 4
        const val WARN = 5
        const val ERROR = 6
        const val ASSERT = 7

        fun init(logger: Log) {
            log = logger
        }
    }
}

private var log: Log? = null

fun log(
    level: Int,
    message: String,
    throwable: Throwable? = null,
    tag: Any? = null
) {
    log?.log(level, message, throwable, tag)
}

fun <T : Any, R> T.tag(block: T.() -> R) = let {
    log?.tag(this)
    block()
}

internal fun <T : Any> T.tag() = log?.tag(this)


fun <T : Any, R> T.measure(message: Any = "", execute: () -> R): R =

    if (log?.debug == true) {
        val ref = AtomicReference<R>()
        measureTimeMillis {
            ref.set(execute())
        }.also { time ->
            val logResult = (ref.get()?.takeIf { it !is Unit } ?: "")
            tag()
            log?.log(Log.DEBUG, "$message $logResult in ${time}ms")
        }
        ref.get()
    } else execute()


fun timestamp() = System.currentTimeMillis()
