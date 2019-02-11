package io.fluks.di.android

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import io.fluks.common.Depends

interface ProxyContainer<D : Any> : Depends<D> {
    fun createDependencies(): D
    override val component: D
        get() = (getDependenciesFragment() ?: createDependenciesFragment()).component
}


private fun <D : Any> ProxyContainer<D>.createDependenciesFragment() = DependenciesFragment<D>().apply {
    component = createDependencies()
    activity().supportFragmentManager
        .beginTransaction()
        .add(this, DependenciesFragment.TAG)
        .commit()
}


private fun <D : Any> ProxyContainer<D>.getDependenciesFragment() =
    activity().findFragmentByTag<DependenciesFragment<D>>(DependenciesFragment.TAG)


private fun <D : Any> ProxyContainer<D>.activity() = (this as FragmentActivity)

@Suppress("UNCHECKED_CAST")
private fun <F : Fragment> FragmentActivity.findFragmentByTag(tag: String): F? =
    supportFragmentManager.findFragmentByTag(tag) as? F

class DependenciesFragment<D : Any> : Fragment(), Depends<D> {
    override lateinit var component: D

    init {
        retainInstance = true
    }

    companion object {
        const val TAG = "DependenciesFragment"
    }
}