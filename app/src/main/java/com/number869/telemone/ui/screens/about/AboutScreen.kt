package com.number869.telemone.ui.screens.about

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.number869.telemone.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "About TeleMone")
                },
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues),
            verticalArrangement = Arrangement.Top
        ) {
            item {

                AboutCard(
                    label = "About",
                    content = {
                        Text(
                            text = "TeleMone is an app that allows you to create custom Telegram Monet Themes.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                )
                AboutCard(
                    label = "Version",
                    content = {
                        val context = LocalContext.current
                        val versionName = context.packageManager.getPackageInfo(
                            context.packageName,
                            0
                        ).versionName
                        Text(
                            text = versionName,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                )
                AboutCard(
                    label = "Developers",
                    content = {
                        val context = LocalContext.current
                        val useWhiteLogos = isSystemInDarkTheme()

                        var number869Expanded by remember { mutableStateOf(false) }
                        val number869Github = "Number869"
                        val number869Telegram = "koorDesart"
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = { number869Expanded = !number869Expanded })
                                .animateContentSize(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioLowBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                        ) {
                            Row {
                                Text(
                                    text = "Number869",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    imageVector = if (number869Expanded) {
                                        Icons.Filled.ExpandLess
                                    } else {
                                        Icons.Filled.ExpandMore
                                    },
                                    contentDescription = "Expand",
                                )
                            }
                            if (number869Expanded) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp),
                                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            id = if (useWhiteLogos) {
                                                R.drawable.github_mark_white
                                            } else {
                                                R.drawable.github_mark
                                            }
                                        ),
                                        contentDescription = "GitHub",
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = number869Github,
                                        modifier = Modifier
                                            .clickable(onClick = {
                                                val intent = Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse("https://github.com/$number869Github")
                                                )
                                                context.startActivity(intent)
                                            }),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp),
                                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            id = if (useWhiteLogos) {
                                                R.drawable.telegram_app_white
                                            } else {
                                                R.drawable.telegram_app
                                            }
                                        ),
                                        contentDescription = "Telegram",
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = number869Telegram,
                                        modifier = Modifier
                                            .clickable(onClick = {
                                                val intent = Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse("https://t.me/$number869Telegram")
                                                )
                                                context.startActivity(intent)
                                            }),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        var Lambada10Expanded by remember { mutableStateOf(false) }
                        val Lambada10Github = "Lambada10"
                        val Lambada10Telegram = "Lambada10"
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = { Lambada10Expanded = !Lambada10Expanded })
                                .animateContentSize(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioLowBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                        ) {
                            Row {
                                Text(
                                    text = "Lambada10",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    imageVector = if (Lambada10Expanded) {
                                        Icons.Filled.ExpandLess
                                    } else {
                                        Icons.Filled.ExpandMore
                                    },
                                    contentDescription = "Expand",
                                )
                            }
                            if (Lambada10Expanded) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            id = if (useWhiteLogos) {
                                                R.drawable.github_mark_white
                                            } else {
                                                R.drawable.github_mark
                                            }
                                        ),
                                        contentDescription = "GitHub",
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = Lambada10Github,
                                        modifier = Modifier
                                            .clickable(onClick = {
                                                val intent = Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse("https://github.com/$Lambada10Github")
                                                )
                                                context.startActivity(intent)
                                            }),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp),
                                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            id = if (useWhiteLogos) {
                                                R.drawable.telegram_app_white
                                            } else {
                                                R.drawable.telegram_app
                                            }
                                        ),
                                        contentDescription = "Telegram",
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = Lambada10Telegram,
                                        modifier = Modifier
                                            .clickable(onClick = {
                                                val intent = Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse("https://t.me/$Lambada10Telegram")
                                                )
                                                context.startActivity(intent)
                                            }),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                )
                AboutCard(
                    label = "Source And Support",
                    content = {
                        val useWhiteLogos = isSystemInDarkTheme()
                        val context = LocalContext.current
                        val sourceCodeLink = "https://github.com/Number869/TeleMone"
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val intent =
                                        Intent(Intent.ACTION_VIEW, Uri.parse(sourceCodeLink))
                                    context.startActivity(intent)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (useWhiteLogos) {
                                        R.drawable.github_mark_white
                                    } else {
                                        R.drawable.github_mark
                                    }
                                ),
                                contentDescription = "GitHub",
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "View Source Code",
                            )
                        }

                        val supportLink = "https://t.me/number869"
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(supportLink))
                                    context.startActivity(intent)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (useWhiteLogos) {
                                        R.drawable.telegram_app_white
                                    } else {
                                        R.drawable.telegram_app
                                    }
                                ),
                                contentDescription = "Telegram",
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Telegram Channel",
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "License: GPLv3"
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun AboutCard(label: String, content: @Composable () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}