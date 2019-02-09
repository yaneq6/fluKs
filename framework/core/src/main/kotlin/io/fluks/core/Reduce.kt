package io.fluks.core

fun <E: Effect, State : Reduce<E, State>>  State.reduce(effects: List<E>) =
    effects.fold(this) { state, effect -> state(effect) ?: state }

fun <E: Effect, State : Reduce<E, State>> State.reduce(vararg effects: E) =
    reduce(effects.toList())


interface Reduce<in E: Effect, State> {
    operator fun invoke(effect: E): State? = null
}