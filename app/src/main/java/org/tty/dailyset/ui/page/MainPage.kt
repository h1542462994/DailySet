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
import org.tty.dailyset.data.scope.DataScope
import org.tty.dailyset.provider.LocalWindow
import org.tty.dailyset.ui.component.TopBar
import org.tty.dailyset.ui.theme.LocalPalette
import org.tty.dailyset.ui.utils.StatusBarToBackground1

@ExperimentalFoundationApi
@Composable
fun MainPage() {

    with(DataScope) {
        var selectedTab by mainTab()
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
                        onClick = { selectedTab = tab },
                        alwaysShowLabel = false,
                        selectedContentColor = LocalPalette.current.primaryColor,
                        unselectedContentColor = LocalPalette.current.primary,
                    )
                }
            }
        }
    }



}

@ExperimentalFoundationApi
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