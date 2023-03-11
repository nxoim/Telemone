package com.dotstealab.telemone.ui.screens.editor

import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.navigation.NavHostController
import com.dotstealab.telemone.MainViewModel
import com.dotstealab.telemone.ui.screens.editor.components.preview.ListOfChatsScreenPreview
import com.dotstealab.telemone.utils.animateAlignmentAsState
import kotlinx.coroutines.delay
import kotlin.math.min
import kotlin.math.roundToInt


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditorScreen(navController: NavHostController, vm: MainViewModel) {
	val itemsList by remember {
		derivedStateOf {
			mutableStateListOf(
				"chatScreen",
				"homeScreen",
				"profileScreen"
			)
		}
	}
	val itemsOffset by remember {
		derivedStateOf { mutableStateMapOf("chatScreen" to Offset.Zero) }
	}

	val itemsExpanded by remember {
		derivedStateOf { mutableStateMapOf("chatScreen" to false) }
	}
	itemsList.forEach { name ->
		itemsOffset.putIfAbsent(name, Offset.Zero)
		itemsExpanded.putIfAbsent(name, false)
	}
	val itemList = itemsList.toList()
	val topPaddingAsDp = WindowInsets.systemBars.getTop(LocalDensity.current).dp
	val bottomPaddingAsDp = WindowInsets.systemBars.getBottom(LocalDensity.current).dp

	val w = LocalConfiguration.current.screenWidthDp
	val h = LocalConfiguration.current.screenHeightDp

	Column(verticalArrangement = Arrangement.SpaceBetween) {
		LazyRow(
			contentPadding = PaddingValues(
				top = topPaddingAsDp,
				bottom = bottomPaddingAsDp,
				start = 16.dp,
				end = 16.dp
			),
			horizontalArrangement = Arrangement.spacedBy(16.dp)
		) {
			items(itemList) { name ->
				// is an expansion/collapse animation running
				var isSpringAnimFinished by remember { mutableStateOf(false) }

				// wait until the spring animation stops
				LaunchedEffect(itemsExpanded[name]) {
					isSpringAnimFinished = if (!itemsExpanded[name]!!) {
						delay(600)
						false
					} else {
						true
					}
				}
				// placeholders is here
				// this is the placeholder for the name. it tells the name
				// where to spawn. had to do it like this due to latency
				// when using animate*AsState for draggable things.
				// this way its responsive and pretty
				// it disappears once the placeholder is spawned
				ExpandableItem(
					Modifier
						.onGloballyPositioned { coordinates ->
							itemsOffset.put(name, coordinates.positionInWindow())
						},
					Pair(name, itemsOffset[name]!!),
					true,
					vm,
					switchSize = {
						itemsExpanded.put(name, itemsExpanded[name] != true)
					},
					isSpringAnimFinished = isSpringAnimFinished
				)
			}
		}

		Column(
			Modifier.navigationBarsPadding().fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			OutlinedButton(onClick = { navController.navigate("AlternativeEditorScreen") }) {
				Text(text = "Go to alternative editor")
			}

			Spacer(modifier = Modifier.height(16.dp))

			OutlinedButton(onClick = { navController.navigate("ListOfChatsScreenPreview") }) {
				Text(text = "Go to other previews in developement")
			}
		}
	}

	// expanded
	Box {
		itemsList.forEach { name ->
			Box(Modifier.fillMaxSize()) {
				// is the item expanded
				var isItemExpanded = itemsExpanded[name] ?: false

				var isSpringAnimFinished by remember { mutableStateOf(false) }

				// interpolated value. goes from 0f to 1f in 0.57 seconds
				var animationProgress by remember { mutableStateOf(0f) }

				// item size
				val itemSize by animateSizeAsState(
					if (isItemExpanded)
						Size(
							min(w.toFloat() - 44f, 400f),
							// making sure its fullscreen
							min(h.toFloat(), 700f))
					else
						Size(150f, 180f),
					animationSpec = spring(
						if (isItemExpanded) 0.9f else 0.85f, if (isItemExpanded) 250f else 350f
					), label = ""
				)

				// animating from the placeholder item's place to the top left corner
				// so the item is properly aligned
				val animatedOffset by animateIntOffsetAsState(
					if (isItemExpanded) IntOffset.Zero else
						IntOffset(
							EaseOutExpo.transform(itemsOffset[name]?.x ?: 0f).roundToInt(),
							EaseOutExpo.transform(itemsOffset[name]?.y ?: 0f).roundToInt()
						),
					spring(
						if (isItemExpanded) 0.9f else 0.85f, if (isItemExpanded) 250f else 350f
					), label = ""
				)

				// gives a nice bounce from px and py towards the center and
				// from the center towards px py. looks like a pixel launcher animation
				val alignment by animateAlignmentAsState(
					if (isItemExpanded) Alignment.Center else Alignment.TopStart,
					spring(
						if (isItemExpanded) 0.9f else 0.85f, if (isItemExpanded) 250f else 350f
					)
				)

				val corner by animateDpAsState(
					if (isItemExpanded) 24.dp else 8.dp,
					spring(if (isItemExpanded) 0.9f else 0.85f, if (isItemExpanded) 250f else 350f)
				)

				// specifying the value that is needed dependent on the items state
				val targetValue = if (isItemExpanded) 1f else 0f

				// interpolating "value" once target value is changed
				LaunchedEffect(targetValue) {
					if (targetValue != animationProgress) {
						val startTime = System.currentTimeMillis()
						val duration = 600

						while (true) {
							val elapsedTime = System.currentTimeMillis() - startTime
							if (elapsedTime >= duration) break

							val progress = elapsedTime.toFloat() / duration
							val diff = targetValue - animationProgress
							animationProgress += progress * diff

							withFrameNanos {
								// Wait for the next frame to be rendered
							}
						}

						animationProgress = targetValue
					}
				}

				LaunchedEffect(isItemExpanded) {
					isSpringAnimFinished = if (!isItemExpanded) {
						delay(600)
						false
					} else {
						true
					}
				}

				// Scrim
				Box(
					Modifier
						.fillMaxSize()
						.background(MaterialTheme.colorScheme.scrim.copy(0.5f * animationProgress))
				)

				// item that appears in place of the placeholder once
				// expanded
				ExpandableItem(
					Modifier
						.align(alignment)
						.size(itemSize.width.dp, itemSize.height.dp)
						.alpha(if (isSpringAnimFinished) 1f else 0f)
						.offset { animatedOffset },
					Pair(name, itemsOffset[name]!!),
					false,
					vm,
					isSpringAnimFinished,
					animationProgress,
					{ itemsExpanded.replace(name, itemsExpanded[name] != true) },
					corner
				)
			}
		}
	}
}

// here we hold our content
@Composable
fun ExpandableItem(
	modifier: Modifier,
	itemType: Pair<String, Offset>,
	isPlaceholder: Boolean,
	vm: MainViewModel,
	isSpringAnimFinished: Boolean = false,
	animationProgress: Float = 1f,
	switchSize: () -> Unit = {},
	corners: Dp = 0.dp,
) {
	val type = itemType.first

	val modifierForPlaceholder = modifier
		.clip(RoundedCornerShape(8.dp))
		.alpha(if (isSpringAnimFinished) 0f else 1f)
		.size(150.dp, 180.dp)
		.clickable { switchSize() }
		.background(MaterialTheme.colorScheme.secondaryContainer)

	val modifierForExpanded = modifier
		.clickable { switchSize() }
		.border(
			BorderStroke(
				1.dp, MaterialTheme.colorScheme.outline.copy(animationProgress)
			),
			shape = RoundedCornerShape(corners)
		)
		// if we dont specify max the app will crash because
		// corner radius can not be -1.dp, which the spring
		// animation causes it to be at one point
		.clip(RoundedCornerShape(max(0.dp, corners)))
		.background(MaterialTheme.colorScheme.secondaryContainer)
//		.alpha(animationProgress)
		.fillMaxSize()

	when (type) {
		"chatScreen" -> {
			if (isPlaceholder) {
				Box(modifierForPlaceholder) {
//					ChatScreenPreview(vm)
				}
			} else {
				Box(modifierForExpanded) {
//					ChatScreenPreview(vm)
				}
			}
		}

		"homeScreen" -> {
			if (isPlaceholder) {
				Box(modifierForPlaceholder) {
					ListOfChatsScreenPreview(vm,0.0f)
				}
			} else {
				Box(modifierForExpanded) {
					ListOfChatsScreenPreview(vm, animationProgress)
				}
			}
		}

		"profileScreen" -> {
			if (isPlaceholder) {
				Box(modifierForPlaceholder) {

				}
			} else {
				Box(modifierForExpanded) {

				}
			}
		}
	}
}