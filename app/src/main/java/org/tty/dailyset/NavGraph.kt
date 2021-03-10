package org.tty.dailyset

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.tty.dailyset.ui.page.MainPage

/**
 * Destination used in the ([org.tty.dailyset.DailySetApp])
 */
object MainDestination {
    const val MAIN_ROUTE = "main"
}

@Composable
fun NavGraph(startDestination: String = MainDestination.MAIN_ROUTE) {
    val navController = rememberNavController()

    val actions = remember(navController) { MainActions(navController) }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(MainDestination.MAIN_ROUTE) {
            MainPage()
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
}