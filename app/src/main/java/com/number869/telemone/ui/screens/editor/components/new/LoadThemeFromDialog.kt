package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.number869.telemone.R
import com.number869.telemone.shared.utils.ThemeStorageType

@Composable
fun LoadWithOptionsDialog(
    close: () -> Unit,
    loadSavedTheme: (ThemeStorageType) -> Unit,
    uuid: String
) {
    var selectedTokens by remember { mutableStateOf<Boolean?>(null) }

    if (selectedTokens == null) {
        AlertDialog(
            title = {
                Text(stringResource(R.string.load_theme_with_options))
            },
            text = {
                Column() {
                    TextButton(
                        onClick = {
                            selectedTokens = false
                        }
                    ) {
                        Icon(
                            Icons.Default.Numbers,
                            contentDescription = null
                        )

                        Spacer(Modifier.padding(horizontal = 4.dp))

                        Text("Load color values")
                    }

                    TextButton(
                        onClick = {
                            selectedTokens = true
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Label,
                            contentDescription = null
                        )

                        Spacer(Modifier.padding(horizontal = 4.dp))

                        Text("Load color tokens")
                    }
                }
            },
            confirmButton = {
                FilledTonalButton(onClick = { close() }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            onDismissRequest = { close() }
        )
    } else {
        AlertDialog(
            icon = {
                if (selectedTokens!!) {
                    Icon(
                        Icons.AutoMirrored.Filled.Label,
                        contentDescription = null
                    )
                } else {
                    Icon(
                        Icons.Default.Numbers,
                        contentDescription = null
                    )
                }
            },
            title = {
                Text(
                    if (selectedTokens!!)
                        "Load color tokens, and..."
                    else
                        "Load color values, and..."
                )
            },
            text = {
                Column() {
                    TextButton(
                        onClick = {
                            close()
                            loadSavedTheme(
                                ThemeStorageType.ByUuid(
                                    uuid,
                                    withTokens = selectedTokens!!,
                                    clearCurrentTheme = true
                                )
                            )
                        }
                    ) {
                        Text("Clear current theme")
                    }

                    TextButton(
                        onClick = {
                            close()
                            loadSavedTheme(
                                ThemeStorageType.ByUuid(
                                    uuid,
                                    withTokens = selectedTokens!!,
                                    clearCurrentTheme = false
                                )
                            )
                        }
                    ) {
                        Text("Keep current theme but add missing values")
                    }
                }
            },
            confirmButton = {
                FilledTonalButton(onClick = { close() }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            onDismissRequest = { close() }
        )
    }
}