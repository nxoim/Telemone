package com.nxoim.blean.ui.composeUiCommons.modifiers.swipeable

import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.unit.Density

@ConsistentCopyVisibility
data class GestureScope internal constructor(
    private val scope: PointerInputScope,
) : Density by scope {
    val pointerInputScopeSize get() = scope.size
}