package io.fluks.base.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import io.fluks.base.*
import io.fluks.base.android.databinding.DataBindingDelegate
import timber.log.Timber
import java.lang.ref.WeakReference

abstract class BaseActivity<DataBinding, Component> :

    AppCompatActivity(),
    DataBindingDelegate<DataBinding>,
    Depends<Component>,
    DispatchDelegate<Event>,
    UI.Finishable

    where DataBinding : ViewDataBinding,
          Component : UI.Component<DataBinding>,
          Component : BaseActivity.Component {

    final override val containerView: View? get() = binding.root
    override val dispatch: Dispatch<Event> get() = component.dispatch

    val binding by lazyDi { setBindingView(layoutId) }

    val toolbar by lazy { findViewById<Toolbar?>(R.id.toolbar) }
    private val progress by lazy { findViewById<ProgressBar?>(R.id.progress) }
    private val progressLayout by lazy { findViewById<ViewGroup?>(R.id.progressBarLayout) }
    private val progressBarController by lazy {
        if (toolbar != null && progress != null) {
            ProgressBarController(progressLayout!!, progress!!)
        } else {
            null
        }
    }
    open val disposable by lazyDi {
        eventsLifecycle.subscribe {
            setProgressVisibility(it.isRunning)
        }!!
    }

    final override fun onCreate(savedInstanceState: Bundle?): Unit = this.measure("onCreate") {
        Timber.d("onCreate started")
        dispatch(Platform::OnTop)
        super.onCreate(savedInstanceState)
        binding
        disposable
        setSupportActionBar(toolbar)
        di {
            binding.init()
            intent.event?.let { dispatch(it) }
            initDebugDrawer()
        }
        onCreateSafe(savedInstanceState)
    }

    open fun setProgressVisibility(visible: Boolean) {
        progressBarController?.setVisibility(visible)
    }

    protected open fun onCreateSafe(savedInstanceState: Bundle?) {/*no-op*/
    }

    override fun onResume() {
        dispatch(Platform::OnTop)
        super.onResume()
    }

    override fun onNewIntent(intent: Intent): Unit = dispatch(intent.event)

    override fun onDestroy() {
        disposable.dispose()
        binding.unbind()
        di {
            disposable.dispose()
            binding.destroy()
        }
        super.onDestroy()
    }

    fun dispatch(getEffect: (WeakReference<Context>) -> Effect) = dispatch(
        getEffect(this@BaseActivity.weak())
    )


    interface Component {
        fun Activity.initDebugDrawer()
    }
}

class ProgressBarController(
    private val anchor: ViewGroup,
    private val progressBar: ProgressBar
) {

    fun setVisibility(visible: Boolean) = anchor.post {
        if (visible) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }
}