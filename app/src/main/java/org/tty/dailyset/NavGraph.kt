package org.tty.dailyset

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.*
import androidx.navigation.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.common.util.urlEncode
import org.tty.dailyset.component.common.sharedComponents
import org.tty.dailyset.component.login.LoginInput
import org.tty.dailyset.ui.page.*

/**
 * Destination used in the ([org.tty.dailyset.DailySetApp])
 */
internal object MainDestination {
    const val MAIN_ROUTE = "main"
    const val DEBUG_ROUTE = "test"
    const val INDEX_ROUTE = "index"
    const val LOGIN_ROUTE = "login"
    const val USER_ROUTE = "user"
    const val REGISTER_ROUTE = "register"
    const val TICKET_BIND_ROUTE = "ticket/bind"
    const val DAILYSET_CLAZZAUTO_ROUTE = "dailyset/clazzauto"
}


@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun NavGraph() {
    val navController = rememberAnimatedNavController()
    val actions = remember(navController) { MainActions(navController) }
    val nav = Nav(navController, actions)
    val sharedComponents = sharedComponents()

    LaunchedEffect(key1 = navController, block = {
        sharedComponents.useNav(nav)
    })

    CompositionLocalProvider(LocalNav provides nav) {
        AnimatedNavHost(
            navController = navController,
            startDestination = MainDestination.INDEX_ROUTE,
            enterTransition = { ->
                slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700))
            },
            exitTransition = { ->
                slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700))
            },
            popEnterTransition = { ->
                slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(700))
            },
            popExitTransition = { ->
                slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(700))
            }
        ) {
            composable(route = MainDestination.MAIN_ROUTE) {
                MainPage()
            }

            composable(route = MainDestination.DEBUG_ROUTE) {
                DebugPage()
            }

            composable(route = MainDestination.INDEX_ROUTE) {
                IndexPage()
            }
            composable(
                route = MainDestination.LOGIN_ROUTE + "?from={from}&username={username}",
                arguments = listOf(
                    navArgument("from") {
                        this.type = NavType.StringType
                        this.defaultValue = ""
                    },
                    navArgument("username") {
                        this.type = NavType.StringType
                        this.defaultValue = ""
                    })
            ) {
                val from = it.arguments?.getString("from") ?: ""
                val username = it.arguments?.getString("username") ?: ""
                val loginInput = LoginInput(from, username)
                LoginPage(loginInput)
            }
            composable(route = MainDestination.USER_ROUTE) {
                UserPage()
            }

            composable(route = MainDestination.REGISTER_ROUTE) {
                RegisterPage()
            }
            composable(route = MainDestination.TICKET_BIND_ROUTE) {
                TicketBindPage()
            }
            composable(
                route = MainDestination.DAILYSET_CLAZZAUTO_ROUTE + "?uid={uid}",
                arguments = listOf(
                    navArgument("uid") {
                        this.type = NavType.StringType
                        this.defaultValue = ""
                    }
                )
            ) {
                val dailySetUid = it.arguments?.getString("uid") ?: ""
                DailySetClazzAutoPage(dailySetUid = dailySetUid)
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


    fun toDebug() {
        navController.navigateExceptEqual(MainDestination.DEBUG_ROUTE)
    }

    fun toIndex() {
        navController.navigateExceptEqual(MainDestination.INDEX_ROUTE)
    }

    fun toLogin(input: LoginInput) {
        navController.navigateExceptEqual(
            MainDestination.LOGIN_ROUTE +
                    "?from=${urlEncode(input.from)}&username=${urlEncode(input.username)}"
        )
    }

    fun toUser() {
        navController.navigateExceptEqual(
            MainDestination.USER_ROUTE
        )
    }

    fun toMain() {
        navController.navigateExceptEqual(MainDestination.MAIN_ROUTE)
    }

    fun toRegister() {
        navController.navigateExceptEqual(MainDestination.REGISTER_ROUTE)
    }

    fun toTicketBind() {
        navController.navigateExceptEqual(MainDestination.TICKET_BIND_ROUTE)
    }

    fun toDailySetClazzAuto(dailySetUid: String) {
        navController.navigateExceptEqual(
            MainDestination.DAILYSET_CLAZZAUTO_ROUTE + "?uid=${
                urlEncode(
                    dailySetUid
                )
            }"
        )
    }

    /**
     * rollback to index page. you should only call this after logout.
     */
    fun rollbackToIndex() {
        while (true) {
            val route = navController.currentBackStackEntry?.destination?.route
            if (route != MainDestination.INDEX_ROUTE) {
                navController.popBackStack()
            } else {
                break
            }
        }
    }

}

data class Nav<T>(
    val navController: NavHostController,
    val action: T
)

/**
 * support nav Action.
 */
internal val LocalNav = compositionLocalOf<Nav<MainActions>> {
    error("nav not provided")
}

fun NavController.navigateExceptEqual(route: String) {
    if (currentDestination?.route != route) {
        navigate(route)
    }
}