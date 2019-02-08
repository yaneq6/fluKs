package io.scheme.util.android.databinding

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.View

interface DataBindingDelegate<Binding : ViewDataBinding> {
    val containerView: View?
    fun setBindingView(): Binding? = DataBindingUtil.bind(containerView!!)
    fun Activity.setBindingView(layoutId: Int): Binding = DataBindingUtil.setContentView(this, layoutId)
}