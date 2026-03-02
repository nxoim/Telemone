package com.number869.telemone.utils

import androidx.annotation.StringRes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow

private val _toastChannel = Channel<Pair<Int, Array<out Any>>>()
/**
 * Pair(res id, formatArgs) for context.getString()
 */
val toastChannel = _toastChannel.consumeAsFlow()

fun showToast(@StringRes id: Int, vararg formatArgs: Any) {
    _toastChannel.trySend(id to formatArgs)
}


