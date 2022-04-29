package org.tty.dailyset

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import org.tty.dailyset.component.common.sharedComponents
import org.tty.dailyset.component.login.LoginInput
import org.tty.dailyset.ui.page.*
import kotlin.collections.MutableMap
import kotlin.collections.mutableMapOf
import kotlin.collections.set

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
    const val INDEX = "index"
    const val LOGIN = "login"
    const val REGISTER = "register"
}

/**
 * the navigation graph for page navigating.
 */
@OptIn(ExperimentalAnimationApi::class)
@ExperimentalFoundationApi
@Composable
fun NavGraph() {
    val navController = rememberAnimatedNavController()
    val arguments = remember { mutableMapOf<String, Any>() }
    val actions = remember(navController) { MainActions(navController, arguments) }
    val nav = Nav(navController, actions, arguments)
    val sharedComponents = sharedComponents()

    LaunchedEffect(key1 = "", block = {
        sharedComponents.useNav { nav }
//        sharedComponents.dataSourceCollection.runtimeDataSource.init()
    })

    CompositionLocalProvider(LocalNav provides nav) {
        AnimatedNavHost(
            navController = navController,
            startDestination = MainDestination.INDEX,
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
            composable(MainDestination.INDEX) {
                IndexPage()
            }
            composable(MainDestination.LOGIN) {
                val input = arguments[MainDestination.LOGIN] as LoginInput
                LoginPage(input)
            }
            composable(MainDestination.REGISTER) {
                RegisterPage()
            }
        }

    }


}

/**
 * Models the navigation actions in the app.
 */
class MainActions(private val navController: NavHostController, private val arguments: MutableMap<String, Any>) {


    fun upPress() {
        navController.navigateUp()
    }
    fun toTimeTable() {
        navController.navigateExceptEqual(MainDestination.TIME_TABLE_ROUTE)
    }
    fun toTimeTablePreview() {
        navController.navigateExceptEqual(MainDestination.TIME_TABLE_PREVIEW_ROUTE)
    }
    fun toDebug() {
        navController.navigateExceptEqual(MainDestination.TEST_ROUTE)
    }
    fun toNormalDailySet() {
        navController.navigateExceptEqual(MainDestination.DAILY_SET_NORMAL)
    }
    fun toClazzDailySet() {
        navController.navigateExceptEqual(MainDestination.DAILY_SET_CLAZZ)
    }
    fun toTaskDailySet() {
        navController.navigateExceptEqual(MainDestination.DAILY_SET_TASK)
    }
    fun toLogin(input: LoginInput) {
        arguments[MainDestination.LOGIN] = input
        navController.navigateExceptEqual(MainDestination.LOGIN)
    }
    fun toMain() {
        navController.navigateExceptEqual(MainDestination.MAIN_ROUTE)
    }
    fun toRegister() {
        navController.navigateExceptEqual(MainDestination.REGISTER)
    }


}

data class Nav<T>(
    val navController: NavHostController,
    val action: T,
    val arguments: MutableMap<String, Any>
)

/**
 * support nav Action.
 */
internal val LocalNav = staticCompositionLocalOf<Nav<MainActions>> {
    error("nav not provided")
}

fun NavController.navigateExceptEqual(route: String) {
    if (currentDestination?.route != route) {
        navigate(route)
    }
}