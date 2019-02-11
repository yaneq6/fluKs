package io.fluks.base.android

import io.fluks.base.FluksLog
import io.fluks.base.innerName
import timber.log.Timber

object FluksLogTimber : FluksLog {

    override fun log(level: Int, message: String?, throwable: Throwable?, tag: Any?) {
        tag?.let { tag(it) }
        when {
            throwable != null && message != null -> Timber.log(level, throwable, message)
            throwable != null -> Timber.log(level, throwable)
            message != null -> Timber.log(level, message)
        }
    }

    override fun tag(tag: Any) {
        Timber.tag(tag::class.innerName)
    }

    override val debug: Boolean get() = BuildConfig.DEBUG
}