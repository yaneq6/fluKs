package io.fluks.di

import io.fluks.base.measure

internal fun <T : Any, R> T.measureDispatch(any: Any, block: () -> R): R =
    measure("dispatch $any", block)