package io.fluks.util.android

import android.content.Context
import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ProgressBar
import io.fluks.Debug
import io.fluks.R
import io.fluks.common.measure
import io.fluks.core.*
import io.fluks.util.android.databinding.DataBindingDelegate
import io.fluks.core.android.middleware.Finishable
import io.fluks.util.di.Depends
import io.fluks.util.di.di
import io.fluks.util.di.lazyDi
import timber.log.Timber
import java.lang.ref.WeakReference

abstract class BaseActivity<
    DataBinding,
    ViewModel : Model,
    State : Reduce<*, State>,
    Component> :

    AppCompatActivity(),
    DataBindingDelegate<DataBinding>,
    Depends<Component>,
    DispatchDelegate<Event>,
    Finishable

    where DataBinding : ViewDataBinding,
          Component : Dispatcher.Component,
          Component : Platform.Component<DataBinding, ViewModel, State>,
          Component : Debug.Component {

    final override val containerView: View? get() = binding.root
    override val dispatch: Dispatch<Event> get() = component.dispatcher

    val binding by lazyDi { setBindingView(layoutId) }
    private val viewModel by lazyDi { viewModel }

    val toolbar: Toolbar? get() = findViewById(R.id.toolbar)
    val progress: ProgressBar? get() = findViewById(R.id.progress)

    final override fun onCreate(savedInstanceState: Bundle?): Unit = this.measure("onCreate") {
        Timber.d("onCreate started")
        dispatch(Platform::OnTop)
        super.onCreate(savedInstanceState)
        binding
        setSupportActionBar(toolbar)
        di {
            viewModel.init()
            binding.bind(viewModel)
            intent.event?.let { dispatch(it) }
            initDebugDrawer()
        }
        onCreateSafe(savedInstanceState)
    }

    protected open fun onCreateSafe(savedInstanceState: Bundle?) {/*no-op*/
    }

    override fun onResume() {
        dispatch(Platform::OnTop)
        super.onResume()
    }

    override fun onNewIntent(intent: Intent): Unit = dispatch(intent.event)

    override fun onDestroy() {
        binding.unbind()
        viewModel.dispose()
        super.onDestroy()
    }

    fun dispatch(getEffect: (WeakReference<Context>) -> Effect) = dispatch(
        getEffect(this@BaseActivity.weak())
    )
}