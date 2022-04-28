package org.tty.dailyset

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
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
@OptIn(ExperimentalAnimationApi::class)
@ExperimentalFoundationApi
@Composable
fun NavGraph(startDestination: String = MainDestination.MAIN_ROUTE) {
    val navController = rememberAnimatedNavController()

    val actions = remember(navController) { MainActions(navController) }
    val nav = Nav(navController, actions)

    CompositionLocalProvider(LocalNav provides nav) {
        AnimatedNavHost(
            navController = navController,
            startDestination = startDestination,
            enterTransition = { _, _ ->
                slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700))
            },
            exitTransition = { _, _ ->
                slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700))
            },
            popEnterTransition = { _, _ ->
                slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(700))
            },
            popExitTransition = { _,_ ->
                slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(700))
            }
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
class MainActions(private val navController: NavHostController) {


    fun upPress() {
        navController.navigateUp()
    }
    fun toTimeTable() {
        navController.navigate(MainDestination.TIME_TABLE_ROUTE)
    }
    fun toTimeTablePreview() {
        navController.navigate(MainDestination.TIME_TABLE_PREVIEW_ROUTE)
    }
    fun toDebug() {
        navController.navigate(MainDestination.TEST_ROUTE)
    }
    fun toNormalDailySet() {
        navController.navigate(MainDestination.DAILY_SET_NORMAL)
    }
    fun toClazzDailySet() {
        navController.navigate(MainDestination.DAILY_SET_CLAZZ)
    }
    fun toTaskDailySet() {
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