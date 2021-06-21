package org.tty.dailyset.ui.page

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.tty.dailyset.R
import org.tty.dailyset.provider.LocalMainViewModel
import org.tty.dailyset.provider.LocalWindow
import org.tty.dailyset.ui.component.TopBar
import org.tty.dailyset.ui.theme.LocalPalette
import org.tty.dailyset.ui.utils.StatusBarToBackground1

@Composable
fun MainPage() {

    val selectedTab by LocalMainViewModel.current.mainTab.observeAsState(MainPageTabs.DAILY_SET)
    val setSelectedTab = LocalMainViewModel.current.setMainTab
    val tabs = MainPageTabs.values()
    val window = LocalWindow.current

    StatusBarToBackground1()

    Column {
        TopBar(title = stringResource(id = selectedTab.title))

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when(selectedTab) {
                MainPageTabs.SUMMARY -> SummaryPage()
                MainPageTabs.DAILY_SET -> DailySetPage()
                MainPageTabs.PROFILE -> ProfilePage()
                else -> error("route error")
            }
        }

        BottomNavigation(
            backgroundColor = LocalPalette.current.background1
        ) {
            tabs.forEach { tab ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(tab.icon),
                            contentDescription = stringResource(tab.title),
                        ) },
                    label = { Text(stringResource(tab.title)) },
                    selected = tab == selectedTab,
                    onClick = { setSelectedTab(tab) },
                    alwaysShowLabel = false,
                    selectedContentColor = LocalPalette.current.primaryColor,
                    unselectedContentColor = LocalPalette.current.primary,
                )
            }
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

enum class MainPageTabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int
){
    SUMMARY(R.string.summary, R.drawable.ic_baseline_timelapse_24),
    DAILY_SET(R.string.daily_set, R.drawable.ic_baseline_sort_24),
    PROFILE(R.string.profile, R.drawable.ic_user_24)
}