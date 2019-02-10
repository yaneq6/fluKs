package io.fluks.debug.dependency

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.fluks.debug.R
import io.fluks.di.DependenciesAccumulator
import io.fluks.di.Snapshot
import io.fluks.di.hasDependency
import io.palaima.debugdrawer.base.DebugModuleAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.debug_dependencies.*
import kotlin.reflect.KClass

class DependenciesDebug(
    private val dependencies: DependenciesAccumulator
) : DebugModuleAdapter(), LayoutContainer {

    override var containerView: View? = null
        private set

    private lateinit var inflater: LayoutInflater

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View =
        inflater.inflate(R.layout.debug_dependencies, parent, false).also {
            this.inflater = inflater
            containerView = it
        }


    override fun onOpened() = itemsLayout.run {
        dependencies.snapshot()
            .filter(Snapshot::hasDependency)
            .groupBy(Snapshot::module)
            .forEach { (module, snapshots) ->
                addModule(module)
                addSnapshots(snapshots)
            }
        postInvalidate()
    }

    override fun onClosed() {
        itemsLayout.removeAllViews()
    }

    private fun ViewGroup.addSnapshots(snapshots: List<Snapshot>) = snapshots.forEach { snapshot ->
        addView(createDependencyItemView().setSnapshot(snapshot))
    }

    private fun ViewGroup.addModule(moduleType: KClass<*>) =
        addView(createModuleItemView().setModuleType(moduleType))
}



