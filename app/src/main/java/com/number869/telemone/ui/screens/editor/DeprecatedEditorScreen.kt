package com.number869.telemone.ui.screens.editor

//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun DeprecatedEditorScreen(navController: NavHostController, vm: MainViewModel) {
//	val topPaddingAsDp = WindowInsets.systemBars.getTop(LocalDensity.current).dp
//	val bottomPaddingAsDp = WindowInsets.systemBars.getBottom(LocalDensity.current).dp
//
//	val palette = fullPalette()
//	val context = LocalContext.current
//
//	Column(
//		Modifier
//			.fillMaxSize()
//			.navigationBarsPadding(),
//		verticalArrangement = Arrangement.SpaceAround,
//		horizontalAlignment = Alignment.CenterHorizontally
//	) {
//		LazyColumn(
//			contentPadding = PaddingValues(
//				top = topPaddingAsDp,
//				bottom = bottomPaddingAsDp,
//				start = 16.dp,
//				end = 16.dp
//			)
//		) {
//			item {
//				AppBarColoursItem(vm, palette)
//			}
//
//			item {
//				val yes = vm.disableShadows
//				Row(
//					Modifier
//						.height(32.dp)
//						.fillMaxWidth(),
//					horizontalArrangement = Arrangement.SpaceBetween,
//					verticalAlignment = Alignment.CenterVertically
//				) {
//					Text(text = "Disable shadows", style = MaterialTheme.typography.headlineMedium)
//					Checkbox(checked = yes, onCheckedChange = { vm.switchShadowsOnOff() })
//				}
//			}
//		}
//
//		Column {
//			Button(onClick = { vm.exportCustomTheme(context) }) {
//				Text(text = "Export Current")
//			}
//
////			OverlayItemWrapper(
////				isOriginalItemStatic = true,
////				originalCornerRadius = 16.dp,
////				key = "alternativeEditorScreen",
////				state = state
////			) {
////				val isExpanded = state.getIsExpanded("alternativeEditorScreen")
////
////				AnimatedVisibility(visible = !isExpanded, enter = fadeIn(), exit = fadeOut()) {
////					OutlinedButton(onClick = { navController.navigate(Screens.EditorScreen.route) }) {
////						Text(text = "Go to alternative editor")
////					}
////				}
////
////				AnimatedVisibility(visible = isExpanded, enter = fadeIn(), exit = fadeOut(), modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
////					EditorScreen(navController, vm)
////				}
////			}
//		}
//	}
//}
//
//@Composable
//fun AppBarColoursItem(vm: MainViewModel, palette: FullPaletteList) {
//	Row(
//		Modifier.fillMaxWidth(),
//		verticalAlignment = Alignment.Bottom
//	) {
//		Column(Modifier.weight(1f, false)) {
//			Text(
//				"Top App Bars",
//				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(MaterialTheme.typography.headlineMedium)
//			)
//
//			Spacer(Modifier.height(16.dp))
//
//			Column(
//				Modifier
//					.clip(RoundedCornerShape(32.dp))
//					.border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(32.dp))
//			) {
//				GroupInfoTopBar(
//					vm.colorFromCurrentTheme("actionBarDefault"),
//					vm.colorFromCurrentTheme("actionBarDefaultIcon"),
//					vm.colorFromCurrentTheme("avatar_backgroundOrange"),
//					vm.colorFromCurrentTheme("avatar_text"),
//					vm.colorFromCurrentTheme("actionBarDefaultTitle"),
//					vm.colorFromCurrentTheme("actionBarDefaultSubtitle"))
//				PinnedMessages(vm.colorFromCurrentTheme("actionBarDefault"))
//			}
//		}
//
//		Spacer(Modifier.width(16.dp))
//
//		Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//			ColorSelectionItem("actionBarDefault", vm, palette)
//			ColorSelectionItem("actionBarDefaultIcon", vm, palette)
//			ColorSelectionItem("actionBarDefaultTitle", vm, palette)
//
//		}
//
//	}
//}
//
//@Composable
//fun ColorSelectionItem(itemName: String, vm: MainViewModel, palette: FullPaletteList) {
//	var showPopUp by remember { mutableStateOf(false) }
//	val color by remember { derivedStateOf { vm.colorFromCurrentTheme(itemName) } }
//
//	Box(
//		Modifier
//			.size(48.dp)
//			.clip(CircleShape)
//			.border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
//			.clickable { showPopUp = true }
//			.aspectRatio(1f)
//			.background(color)
//	)
//
//	if (showPopUp) {
//		Popup(onDismissRequest = { showPopUp = false }) {
//			PalettePopup(itemName, vm, palette)
//		}
//	}
//}