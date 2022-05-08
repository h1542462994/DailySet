package org.tty.dailyset

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.*
import androidx.core.text.htmlEncode
import androidx.navigation.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import okio.ByteString.Companion.encode
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.common.util.stringToBase64
import org.tty.dailyset.common.util.urlEncode
import org.tty.dailyset.component.common.sharedComponents
import org.tty.dailyset.component.login.LoginInput
import org.tty.dailyset.ui.page.*

/**
 * Destination used in the ([org.tty.dailyset.DailySetApp])
 */
object MainDestination {
    const val MAIN_ROUTE = "main"

    // TODO: 因为架构的原因暂时移除了这些页面
//    const val TIME_TABLE_ROUTE = "time_table"
//    const val TIME_TABLE_PREVIEW_ROUTE = "time_table_preview"
    const val TEST_ROUTE = "test"

    //    const val DAILY_SET_NORMAL = "daily_set_normal"
//    const val DAILY_SET_CLAZZ = "daily_set_clazz"
//    const val DAILY_SET_TASK = "daily_set_task"
    const val INDEX = "index"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val TICKET_BIND = "ticket_bind"
    const val DAILYSET_CLAZZAUTO = "dailyset/clazzauto"
}

//private var arguments = HashMap<String, Any>()


@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun NavGraph() {
    val navController = rememberAnimatedNavController()
    val actions = remember(navController) { MainActions(navController) }
    val nav = Nav(navController, actions)
    val sharedComponents = sharedComponents()

    LaunchedEffect(key1 = navController, block = {
        sharedComponents.useNav(nav)
//        sharedComponents.dataSourceCollection.runtimeDataSource.init()
    })

    CompositionLocalProvider(LocalNav provides nav) {
        AnimatedNavHost(
            navController = navController,
            startDestination = MainDestination.INDEX,
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
            composable(MainDestination.MAIN_ROUTE) {
                MainPage()
            }
//            composable(MainDestination.TIME_TABLE_ROUTE) {
//                DailyTablePage()
//            }
//            composable(MainDestination.TIME_TABLE_PREVIEW_ROUTE) {
//                DailyTablePreviewPage()
//            }
            composable(MainDestination.TEST_ROUTE) {
                TestPage()
            }
//            composable(MainDestination.DAILY_SET_NORMAL) {
//                NormalDailySetPage()
//            }
//            composable(MainDestination.DAILY_SET_CLAZZ) {
//                ClazzDailySetPage()
//            }
//            composable(MainDestination.DAILY_SET_TASK) {
//                TaskDailySetPage()
//            }
            composable(MainDestination.INDEX) {
                IndexPage()
            }
            composable(
                route = MainDestination.LOGIN + "?from={from}&username={username}",
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
            composable(MainDestination.REGISTER) {
                RegisterPage()
            }
            composable(MainDestination.TICKET_BIND) {
                TicketBindPage()
            }
            composable(
                route = MainDestination.DAILYSET_CLAZZAUTO + "?uid={uid}",
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

//    fun toTimeTable() {
//        navController.navigateExceptEqual(MainDestination.TIME_TABLE_ROUTE)
//    }
//
//    fun toTimeTablePreview() {
//        navController.navigateExceptEqual(MainDestination.TIME_TABLE_PREVIEW_ROUTE)
//    }

    fun toDebug() {
        navController.navigateExceptEqual(MainDestination.TEST_ROUTE)
    }

//    fun toNormalDailySet() {
//        navController.navigateExceptEqual(MainDestination.DAILY_SET_NORMAL)
//    }
//
//    fun toClazzDailySet() {
//        navController.navigateExceptEqual(MainDestination.DAILY_SET_CLAZZ)
//    }
//
//    fun toTaskDailySet() {
//        navController.navigateExceptEqual(MainDestination.DAILY_SET_TASK)
//    }

    fun toLogin(input: LoginInput) {
        navController.navigateExceptEqual(MainDestination.LOGIN + "?from=${urlEncode(input.from)}&username=${urlEncode(input.username)}")
    }

    fun toMain() {
        navController.navigateExceptEqual(MainDestination.MAIN_ROUTE)
    }

    fun toRegister() {
        navController.navigateExceptEqual(MainDestination.REGISTER)
    }

    fun toTicketBind() {
        navController.navigateExceptEqual(MainDestination.TICKET_BIND)
    }

    fun toDailySetClazzAuto(dailySetUid: String) {
        navController.navigateExceptEqual(MainDestination.DAILYSET_CLAZZAUTO + "?uid=${urlEncode(dailySetUid)}")
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