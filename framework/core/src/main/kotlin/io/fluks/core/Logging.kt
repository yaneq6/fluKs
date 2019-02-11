package io.fluks.core

import io.fluks.base.*

internal fun <State : Any> StateHolder.Repository<State>.measureRead(): State? =
    measure("read $name") { state }


internal fun <State : Any> StateHolder.Repository<State>.measureWrite(state: State?) =
    measure("write $name") { this.state = state }

fun Any.logDispatch(record: Middleware.Record<Event>) = tag {
    record.takeIf { it.event !is Event.Success }?.run {
        log(
            level = Log.DEBUG,
            message = "dispatch: $event \n after: ${predecessors
                .takeUnless { it.isEmpty() }
                ?.map { it::class.simpleName }
                ?.reduce { acc, s -> "$acc -> $s" }
            }",
            tag = this@logDispatch
        )
    }
}

fun Any.logSuccess(record: Middleware.Record<Event>) =
    record.takeIf { it.event is Event.Success }?.run {
        log(
            level = Log.DEBUG,
            message = "successful in: ${timestamp - predecessors.last().second}ms\n ${predecessors
                .takeUnless { it.isEmpty() }
                ?.map { it.first::class.innerName }
                ?.reduce { acc, s -> "$acc -> $s" }
            }",
            tag = this@logSuccess
        )
    }

fun Any.logNullEvent() = log(
    level = Log.DEBUG,
    message = "trying dispatch null action",
    tag = this
)