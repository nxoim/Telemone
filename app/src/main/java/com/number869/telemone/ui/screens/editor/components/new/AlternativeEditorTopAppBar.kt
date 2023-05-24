package com.number869.telemone.ui.screens.editor.components.new

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ShortText
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.number869.telemone.MainViewModel
import com.number869.telemone.ui.Screens
import com.number869.telemone.ui.screens.editor.components.old.ClearBeforeLoadDialog
import com.number869.telemone.ui.theme.fullPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlternativeEditorTopAppBar(
	topAppBarState: TopAppBarScrollBehavior,
	navController: NavController,
	vm: MainViewModel
) {
	val context = LocalContext.current
	var showMenu by remember { mutableStateOf(false) }
	val palette = fullPalette()
	var showClearBeforeLoadDialog by remember { mutableStateOf(false) }
	val pickedFileUriState = remember { mutableStateOf<Uri?>(null) }

	val launcherThatClears = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.OpenDocument()
	) { result ->
		pickedFileUriState.value = result

		result?.let { uri ->
			vm.loadThemeFromFile(context, uri, palette, true)
		}
	}
	val launcherThatDoesnt = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.OpenDocument()
	) { result ->
		pickedFileUriState.value = result

		result?.let { uri ->
			vm.loadThemeFromFile(context, uri, palette, false)
		}
	}

	TopAppBar(
		navigationIcon = {
			IconButton(onClick = { navController.popBackStack() }) {
				Icon(Icons.Default.ArrowBack, contentDescription = "Back")
			}
		},
		title = { Text("Theme Editor") },
		actions = {
			IconButton(onClick = { vm.exportCustomTheme(context) }) {
				Icon(Icons.Default.Upload, contentDescription = "Export current theme")
			}
			IconButton(onClick = { vm.saveCurrentTheme() }) {
				Icon(Icons.Default.Save, contentDescription = "Save current theme")
			}
			Box {
				IconButton(onClick = { showMenu = true }) {
					Icon(Icons.Default.MoreVert, contentDescription = "Options")
				}

				DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
					DropdownMenuItem(
						text = { Text(text = "Reset current theme") },
						onClick = { vm.resetCurrentTheme() },
						leadingIcon = { Icon(Icons.Default.Refresh, contentDescription = "Reset current theme") }
					)
					DropdownMenuItem(
						text = { Text(text = "Load stock light theme") },
						onClick = { vm.loadStockLightTheme(palette, context) },
						leadingIcon = { Icon(Icons.Default.LightMode, contentDescription = "Load stock light theme") }
					)
					DropdownMenuItem(
						text = { Text(text = "Load stock dark theme") },
						onClick = { vm.loadStockDarkTheme(palette, context) },
						leadingIcon = { Icon(Icons.Default.DarkMode, contentDescription = "Load stock dark theme") }
					)
					DropdownMenuItem(
						text = { Text(text = "Show values") },
						onClick = {
							navController.navigate(Screens.ThemeValuesScreen.route)
							showMenu = false
						},
						leadingIcon = { Icon(Icons.Default.ShortText, contentDescription = "Show values") }
					)
					DropdownMenuItem(
						text = { Text(text = "Load default light theme") },
						onClick = { vm.loadDefaultLightTheme(palette) },
						leadingIcon = { Icon(Icons.Default.LightMode, contentDescription = "Load default light theme") }
					)
					DropdownMenuItem(
						text = { Text(text = "Load default dark theme") },
						onClick = { vm.loadDefaultDarkTheme(palette) },
						leadingIcon = { Icon(Icons.Default.DarkMode, contentDescription = "Load default dark theme") }
					)
					DropdownMenuItem(
						text = { Text(text = "Load theme from file") },
						onClick = { showClearBeforeLoadDialog = true; showMenu = false },
						leadingIcon = { Icon(Icons.Default.UploadFile, contentDescription = "Load default dark theme") }
					)
				}
			}
		},
		scrollBehavior = topAppBarState
	)

	// Dialog that asks if the user wants to clear current theme
	// before loading one from file
	AnimatedVisibility(
		visible = showClearBeforeLoadDialog,
		enter = expandVertically(),
		exit = shrinkVertically()
	) {
		ClearBeforeLoadDialog(
			{ showClearBeforeLoadDialog = false },
			{
				showClearBeforeLoadDialog = false
				vm.saveCurrentTheme()
				launcherThatClears.launch(arrayOf("*/*"))
			},
			{
				showClearBeforeLoadDialog = false
				vm.saveCurrentTheme()
				launcherThatDoesnt.launch(arrayOf("*/*"))
			}
		)
	}
}