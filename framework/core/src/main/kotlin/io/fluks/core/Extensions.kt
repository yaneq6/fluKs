package io.fluks.core

import io.reactivex.Observable
import io.reactivex.ObservableSource

fun <T, R> List<T>.merge(by: T.() -> ObservableSource<out R>) = Observable.merge(this.map(by))!!
