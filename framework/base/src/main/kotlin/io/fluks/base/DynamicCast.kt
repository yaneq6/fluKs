package io.fluks.base

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.*

inline fun <reified T> Any.dynamicCast(): T =
    if (this is T) this
    else Proxy.newProxyInstance(
        javaClass.classLoader,
        arrayOf(T::class.java),
        InvokeHandler(this)
    ) as T

class InvokeHandler(private val underlying: Any) : InvocationHandler {
    override fun invoke(proxy: Any, method: Method, args: Array<out Any?>?): Any? {
        for (fn in underlying.javaClass.methods) {
            if (fn.name == method.name && Arrays.equals(fn.parameterTypes, method.parameterTypes)) {
                return if (args == null)
                    fn.invoke(underlying)
                else
                    fn.invoke(underlying, *args)
            }
        }
        throw UnsupportedOperationException("$method with $args")
    }
}
