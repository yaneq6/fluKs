package io.fluks.debug.dependency

import android.view.ViewGroup
import io.fluks.common.android.inflate
import io.fluks.common.innerName
import io.fluks.debug.R
import kotlinx.android.synthetic.main.debug_module_item.view.*
import kotlin.reflect.KClass

fun ViewGroup.createModuleItemView(): ViewGroup = inflate(R.layout.debug_module_item)

fun ViewGroup.setModuleType(moduleType: KClass<*>) = apply {
    moduleTextView.text = moduleType.innerName
}



