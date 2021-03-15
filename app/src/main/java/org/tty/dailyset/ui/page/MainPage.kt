package org.tty.dailyset.ui.page

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.tty.dailyset.R
import org.tty.dailyset.ui.theme.LocalPalette

@Composable
fun MainPage() {
    val (selectedTab, setSelectedTab) = remember { mutableStateOf(MainPageTabs.DAILY_SET) }
    val tabs = MainPageTabs.values()

    Scaffold(
        topBar = {
            AppBar(title = selectedTab.title)
        },
        bottomBar = {
            BottomNavigation(
                backgroundColor = LocalPalette.current.background1
            ) {
                tabs.forEach { tab ->
                    BottomNavigationItem(
                        icon = { Icon(painter = painterResource(tab.icon), contentDescription = stringResource(tab.title)) },
                        label = { Text(stringResource(tab.title)) },
                        selected = tab == selectedTab,
                        onClick = { setSelectedTab(tab) },
                        alwaysShowLabel = false,
                        selectedContentColor = MaterialTheme.colors.secondary,
                        unselectedContentColor = LocalPalette.current.textColor,
                    )
                }
            }
        }
    ) {
        when(selectedTab) {
            MainPageTabs.SUMMARY -> SummaryPage()
            MainPageTabs.DAILY_SET -> DailySetPage()
            MainPageTabs.PROFILE -> ProfilePage()
        }
    }
}

@Composable
fun AppBar(@StringRes title: Int, useBack: Boolean = false , onBackPressed: () -> Unit = {}) {
    TopAppBar(
        backgroundColor = LocalPalette.current.background1,
        title = { Text(stringResource(id = title)) },
        navigationIcon = {
            if (useBack) {
                IconButton(onClick = onBackPressed) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }
            }
        }
    )
}

@Preview
@Composable
fun MainPagePreview() {
    MainPage()
}

private enum class MainPageTabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int
){
    SUMMARY(R.string.summary, R.drawable.ic_baseline_timelapse_24),
    DAILY_SET(R.string.daily_set, R.drawable.ic_baseline_sort_24),
    PROFILE(R.string.profile, R.drawable.ic_baseline_person_24)
}