package io.fluks.di

import io.fluks.common.measure


fun <T : Any, R> T.measureCreate(name: String = "", block: () -> R): R =
    measure("create $name", block)

internal fun <T : Any, R> T.measureDispatch(any: Any, block: () -> R): R =
    measure("dispatch $any", block)