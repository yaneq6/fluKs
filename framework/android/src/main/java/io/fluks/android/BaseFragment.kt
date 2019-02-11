package io.fluks.android

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.fluks.android.databinding.DataBindingDelegate
import io.fluks.common.android.getSerializable
import io.fluks.common.Event
import io.fluks.common.UI
import io.fluks.common.Depends
import io.fluks.common.di
import io.reactivex.disposables.Disposable

abstract class BaseFragment<
    DataBinding : ViewDataBinding,
    Component: UI.Component<DataBinding>> :

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
            binding.bind()
        }
    }

    override fun onDestroyView() {
        disposable?.dispose()
        super.onDestroyView()
    }
}