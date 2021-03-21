package org.tty.dailyset

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import org.tty.dailyset.ui.page.MainPage
import org.tty.dailyset.ui.page.TimeTablePage

/**
 * Destination used in the ([org.tty.dailyset.DailySetApp])
 */
object MainDestination {
    const val MAIN_ROUTE = "main"
    const val TIME_TABLE_ROUTE = "time_table"
}

@Composable
fun NavGraph(startDestination: String = MainDestination.MAIN_ROUTE) {
    val navController = rememberNavController()

    val actions = remember(navController) { MainActions(navController) }
    val nav = Nav<MainActions>(navController, actions)

    CompositionLocalProvider(LocalNav provides nav) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable(MainDestination.MAIN_ROUTE) {
                MainPage()
            }
            composable(MainDestination.TIME_TABLE_ROUTE) {
                TimeTablePage()
            }
        }

    }
}

/**
 * Models the navigation actions in the app.
 */
class MainActions(navController: NavHostController) {
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
    val toTimeTable: () -> Unit = {
        navController.navigate(MainDestination.TIME_TABLE_ROUTE)
    }
}

data class Nav<T>(
    val navController: NavHostController,
    val action: T
)

/**
 * support nav Action
 */
internal val LocalNav = staticCompositionLocalOf<Nav<MainActions>> {
    error("nav not provided")
}