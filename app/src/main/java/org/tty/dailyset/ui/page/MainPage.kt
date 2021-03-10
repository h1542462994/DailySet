package org.tty.dailyset.ui.page

import android.graphics.drawable.Icon
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
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
        bottomBar = {
            BottomNavigation(
                backgroundColor = LocalPalette.current.gray5
            ) {
                tabs.forEach { tab ->
                    BottomNavigationItem(
                        icon = { Icon(painter = painterResource(tab.icon), contentDescription = stringResource(tab.title)) },
                        label = { Text(stringResource(tab.title)) },
                        selected = tab == selectedTab,
                        onClick = { setSelectedTab(tab) },
                        alwaysShowLabel = false,
                        selectedContentColor = MaterialTheme.colors.secondary,
                        unselectedContentColor = LocalPalette.current.gray80,
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