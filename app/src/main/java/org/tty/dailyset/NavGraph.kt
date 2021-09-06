package org.tty.dailyset

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.tty.dailyset.ui.page.*

/**
 * Destination used in the ([org.tty.dailyset.DailySetApp])
 */
object MainDestination {
    const val MAIN_ROUTE = "main"
    const val TIME_TABLE_ROUTE = "time_table"
    const val TIME_TABLE_PREVIEW_ROUTE = "time_table_preview"
    const val TEST_ROUTE = "test"
    const val DAILY_SET_NORMAL = "daily_set_normal"
    const val DAILY_SET_CLAZZ = "daily_set_clazz"
    const val DAILY_SET_TASK = "daily_set_task"
}

/**
 * the navigation graph for page navigating.
 */
@ExperimentalFoundationApi
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
                DailyTablePage()
            }
            composable(MainDestination.TIME_TABLE_PREVIEW_ROUTE) {
                DailyTablePreviewPage()
            }
            composable(MainDestination.TEST_ROUTE) {
                TestPage()
            }
            composable(MainDestination.DAILY_SET_NORMAL) {
                NormalDailySetPage()
            }
            composable(MainDestination.DAILY_SET_CLAZZ) {
                ClazzDailySetPage()
            }
            composable(MainDestination.DAILY_SET_TASK) {
                TaskDailySetPage()
            }
        }

    }
}

/**
 * Models the navigation actions in the app.
 */
class MainActions(navController: NavHostController) {
    // TODO: 2021/6/24 添加切换动画效果.
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
    val toTimeTable: () -> Unit = {
        navController.navigate(MainDestination.TIME_TABLE_ROUTE)
    }
    val toTimeTablePreview: () -> Unit = {
        navController.navigate(MainDestination.TIME_TABLE_PREVIEW_ROUTE)
    }
    val toTest: () -> Unit = {
        navController.navigate(MainDestination.TEST_ROUTE)
    }
    val toNormalDailySet: () -> Unit = {
        navController.navigate(MainDestination.DAILY_SET_NORMAL)
    }
    val toClazzDailySet: () -> Unit = {
        navController.navigate(MainDestination.DAILY_SET_CLAZZ)
    }
    val toTaskDailySet: () -> Unit = {
        navController.navigate(MainDestination.DAILY_SET_TASK)
    }

}

data class Nav<T>(
    val navController: NavHostController,
    val action: T
)

/**
 * support nav Action.
 */
internal val LocalNav = staticCompositionLocalOf<Nav<MainActions>> {
    error("nav not provided")
}