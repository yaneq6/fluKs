package io.fluks.util.di

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import io.fluks.util.core.measureCreate
import io.fluks.util.core.util.up
import kotlin.reflect.KClass

interface Depends<D> {
    val component: D
}

interface ProxyContainer<D : Any> : Depends<D> {
    fun createDependencies(): D
    override val component: D
        get() = (getDependenciesFragment() ?: createDependenciesFragment()).component
}


private fun <D : Any> ProxyContainer<D>.createDependenciesFragment() = DependenciesFragment<D>().apply {
    component = createDependencies()
    activity().supportFragmentManager.beginTransaction().add(this, DependenciesFragment.TAG).commit()
}


private fun <D : Any> ProxyContainer<D>.getDependenciesFragment() =
    activity().findFragmentByTag<DependenciesFragment<D>>(DependenciesFragment.TAG)


private fun <D : Any> ProxyContainer<D>.activity() = (this as FragmentActivity)

@Suppress("UNCHECKED_CAST")
private fun <F : Fragment> FragmentActivity.findFragmentByTag(tag: String): F? =
    supportFragmentManager.findFragmentByTag(tag) as? F

inline fun <D, T> Depends<D>.di(get: D.() -> T): T = component.get()

inline fun <D, T> Depends<D>.lazyDi(crossinline get: D.() -> T): Lazy<T> = lazy { component.get() }

inline fun <D: Any> Depends<D>.createDi(crossinline create: () -> D): Lazy<D> = lazy { this.measureCreate("dependencies") { create() } }

inline fun <reified D : Any> Any.dependencies(): D = dependencies(D::class)

@Suppress("UNCHECKED_CAST")
fun <D : Any> Any.dependencies(type: KClass<D>): D = (this as? Depends<D>)?.component
    ?: throw up("$this is not a type of ${Depends::class.qualifiedName}<${type.qualifiedName}>")

class DependenciesFragment<D : Any> : Fragment(), Depends<D> {
    override lateinit var component: D

    init {
        retainInstance = true
    }

    companion object {
        const val TAG = "DependenciesFragment"
    }
}