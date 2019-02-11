package io.fluks.di

import io.fluks.common.measure

internal fun <T : Any, R> T.measureDispatch(any: Any, block: () -> R): R =
    measure("dispatch $any", block)