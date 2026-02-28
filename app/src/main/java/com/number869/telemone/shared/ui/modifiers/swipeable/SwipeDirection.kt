package com.nxoim.blean.ui.composeUiCommons.modifiers.swipeable

sealed interface SwipeDirection {
    sealed interface Cardinal : SwipeDirection {
        data object Start : Cardinal
        data object End : Cardinal
        data object Top : Cardinal
        data object Bottom : Cardinal
    }

    sealed interface Diagonal : SwipeDirection {
        data object TopStart : Diagonal
        data object TopEnd : Diagonal
        data object BottomStart : Diagonal
        data object BottomEnd : Diagonal
    }

    companion object {
        val Start get() = Cardinal.Start
        val End get() = Cardinal.End
        val Top get() = Cardinal.Top
        val Bottom get() = Cardinal.Bottom
        val TopStart get() = Diagonal.TopStart
        val TopEnd get() = Diagonal.TopEnd
        val BottomStart get() = Diagonal.BottomStart
        val BottomEnd get() = Diagonal.BottomEnd
    }
}