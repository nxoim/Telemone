package com.number869.telemone.utils

import com.arkivanov.decompose.GenericComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.bindToLifecycleOf(context: GenericComponentContext<*>) = this.apply {
    context.lifecycle.doOnDestroy(coroutineContext::cancel)
}

fun GenericComponentContext<*>.lifecycledCoroutineScope(
    coroutineContext: CoroutineContext = Dispatchers.Default + SupervisorJob()
) = CoroutineScope(coroutineContext).bindToLifecycleOf(this)