package com.number869.telemone.ui.screens.about

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.number869.telemone.ui.RootDestinations
import com.number869.telemone.ui.screens.about.components.DescriptionItem
import com.number869.telemone.ui.screens.about.components.DevelopersItem
import com.number869.telemone.ui.screens.about.components.LegalItem
import com.number869.telemone.ui.screens.about.components.SourceAndLinksItem
import com.number869.telemone.ui.screens.about.components.SpecialMentionsItem
import com.number869.telemone.ui.screens.about.components.VersionItem
import com.nxoim.decomposite.core.common.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    rootNavController: NavController<RootDestinations>,
    dialogsNavController: NavController<AboutDestinations.Dialogs>
) {
    val topAppBarState = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "About Telemone") },
                navigationIcon = {
                    IconButton(onClick = { rootNavController.navigateBack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                scrollBehavior = topAppBarState
            )
        },
        modifier = Modifier.nestedScroll(topAppBarState.nestedScrollConnection)
    ) { scaffoldPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp + scaffoldPadding.calculateTopPadding(),
                bottom = scaffoldPadding.calculateBottomPadding()
            ),
        ) {
            item {
                Column(verticalArrangement = spacedBy(16.dp)) {
                    DescriptionItem()
                    VersionItem()
                    DevelopersItem()
                    SourceAndLinksItem()
                    SpecialMentionsItem()
                    LegalItem(dialogsNavController)
                }
            }
        }
    }
}

