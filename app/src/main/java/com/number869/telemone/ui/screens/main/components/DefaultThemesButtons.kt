@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.number869.telemone.ui.screens.main.components

import android.R.attr.text
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.LocalMotionScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.number869.telemone.shared.ui.TextWithFixedSize
import com.number869.telemone.ui.theme.SolarSet
import kotlin.math.max

@Composable
fun DefaultThemesButtons(exportTheme: (light: Boolean) -> Unit) {
    Column(
        verticalArrangement = spacedBy(16.dp)
    ) {
        val lightInteractionSource = remember { MutableInteractionSource() }
        val darkInteractionSource = remember { MutableInteractionSource() }

        ThemeButton(save = { exportTheme(true) }, interactionSource = lightInteractionSource) {
            Icon(
                SolarSet.Sun,
                contentDescription = null,
                Modifier.size(52.dp)
            )
            AnimatedVisibility(
                !lightInteractionSource.collectIsPressedAsState().value,
                enter = textEnter,
                exit = textExit
            ) {
                TextWithFixedSize(
                    text = "Save Light",
                    fontSize = 35.sp,
                    style = MaterialTheme.typography.displaySmall.plus(
                        noPlatformPaddingText
                    ),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        ThemeButton(save = { exportTheme(false) }, interactionSource = darkInteractionSource) {
            Icon(
                SolarSet.Moon,
                contentDescription = null,
                Modifier.size(52.dp)
            )

            AnimatedVisibility(
                !darkInteractionSource.collectIsPressedAsState().value,
                enter = textEnter,
                exit = textExit
            ) {
                TextWithFixedSize(
                    text = "Save Dark",
                    fontSize = 35.sp,
                    style = MaterialTheme.typography.displaySmall.plus(
                        noPlatformPaddingText
                    ),
                    Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun ThemeButton(
    save: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Row(
            modifier
                .widthIn(max = 400.dp)
                .combinedClickable(interactionSource = interactionSource) { save() }
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.displaySmall.plus(
                    // this removes default padding around text
                    TextStyle(
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                        fontSize = 35.sp
                    )
                )
            ) {
                content()
            }
        }
    }
}
private val noPlatformPaddingText = TextStyle(platformStyle = PlatformTextStyle(false))

val textEnter
    @Composable
    get() = fadeIn( LocalMotionScheme.current.fastEffectsSpec()) + expandIn(
        animationSpec = LocalMotionScheme.current.fastSpatialSpec(),
        expandFrom = Alignment.Center,
        clip = false
    )

val textExit
    @Composable
    get() = fadeOut(LocalMotionScheme.current.fastEffectsSpec()) + shrinkOut(
        animationSpec = LocalMotionScheme.current.fastSpatialSpec(),
        shrinkTowards = Alignment.Center,
        clip = false
    )
