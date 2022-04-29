package org.tty.dailyset.ui.page

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.tty.dailyset.R
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.common.observable.collectAsState
import org.tty.dailyset.component.common.StatusBarToBackground1
import org.tty.dailyset.component.main.mainVM
import org.tty.dailyset.ui.component.TopBar
import org.tty.dailyset.ui.theme.LocalPalette

@UseViewModel("/")
@ExperimentalFoundationApi
@Composable
fun MainPage() {
    val mainVM = mainVM()
    var selectedTab by mainVM.mainTab.collectAsState()
    val tabs = MainPageTabs.values()
    StatusBarToBackground1()


    Column {
        TopBar(title = stringResource(id = selectedTab.title))

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when (selectedTab) {
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
                        )
                    },
                    label = { Text(stringResource(tab.title)) },
                    selected = tab == selectedTab,
                    onClick = { selectedTab = tab },
                    alwaysShowLabel = false,
                    selectedContentColor = LocalPalette.current.primaryColor,
                    unselectedContentColor = LocalPalette.current.primary,
                )
            }
        }
    }

}


enum class MainPageTabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {
    SUMMARY(R.string.summary, R.drawable.ic_baseline_timelapse_24),
    DAILY_SET(R.string.daily_set, R.drawable.ic_baseline_sort_24),
    PROFILE(R.string.profile, R.drawable.ic_user_24)
}

@Preview
@Composable
fun MainPagePreview() {
    val selectedTab = MainPageTabs.SUMMARY
    val tabTitle = "概览"
    val tabs = MainPageTabs.values()
    Column {
        TopBar(title = tabTitle)
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {

        }
        BottomNavigation(
            backgroundColor = LocalPalette.current.background1
        ) {
            tabs.forEach { tab ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(tab.icon),
                            contentDescription = tabTitle,
                        )
                    },
                    label = { Text(tabTitle) },
                    selected = tab == selectedTab,
                    onClick = {  },
                    alwaysShowLabel = false,
                    selectedContentColor = LocalPalette.current.primaryColor,
                    unselectedContentColor = LocalPalette.current.primary,
                )
            }
        }
    }




}