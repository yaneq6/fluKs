package io.fluks.util.android

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.Disposable
import io.fluks.util.android.databinding.DataBindingDelegate
import io.fluks.di.Depends
import io.fluks.di.android.di
import io.fluks.core.Event
import io.fluks.core.Platform
import io.fluks.core.Reduce

abstract class BaseFragment<
    DataBinding : ViewDataBinding,
    ViewModel,
    State: Reduce<*, State>,
    Component: Platform.Component<DataBinding, ViewModel, State>> :

    Fragment(),
    DataBindingDelegate<DataBinding>,
    Depends<Component> {


    private var disposable: Disposable? = null
    private val binding: DataBinding by lazy { setBindingView()!! }
    override val containerView: View? get() = view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = di {
        return inflater.inflate(layoutId, container)
    }

    // TODO adjust to activity
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding
        di {
            savedInstanceState?.getSerializable<Event>("event") // TODO
            bind(binding, viewModel)
        }
    }

    override fun onDestroyView() {
        disposable?.dispose()
        super.onDestroyView()
    }
}