package io.scheme.util.debug.dependency

import android.view.ViewGroup
import io.scheme.R
import io.scheme.util.android.inflate
import io.scheme.util.core.util.innerName
import kotlinx.android.synthetic.main.debug_module_item.view.*
import kotlin.reflect.KClass

fun ViewGroup.createModuleItemView(): ViewGroup = inflate(R.layout.debug_module_item)

fun ViewGroup.setModuleType(moduleType: KClass<*>) = apply {
    moduleTextView.text = moduleType.innerName
}



