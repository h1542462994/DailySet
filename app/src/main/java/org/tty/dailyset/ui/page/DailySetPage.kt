package org.tty.dailyset.ui.page

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.data.processor.DailySetProcessor
import org.tty.dailyset.data.scope.DataScope
import org.tty.dailyset.event.DailySetCreateEventArgs
import org.tty.dailyset.event.DailySetEventType
import org.tty.dailyset.model.entity.*
import org.tty.dailyset.model.lifetime.dailyset.DailySetCreateState
import org.tty.dailyset.ui.component.ProfileMenuItem
import org.tty.dailyset.ui.component.TitleSpace
import org.tty.dailyset.ui.image.ImageResource
import java.util.*

@ExperimentalFoundationApi
@Composable
fun DailySetPage() {


    // TODO: 2021/6/24 remember the scroll on page changed.
    val scrollState = rememberScrollState()
    with(DataScope) {
        val mainViewModel = mainViewModel()
        val service = mainViewModel.service
        val dailySetCreateState = dailySetCreateState()
        val userState by currentUserState()
        val dailySets by dailySets()
        val nav = LocalNav.current

        val dailySetProcessor = object: DailySetProcessor {
            override fun onCreate(dailySetName: String, icon: DailySetIcon?, type: DailySetType) {
                Log.d("DailySetPage", "addSet,${dailySetName},${icon},${type}")
                val uid = UUID.randomUUID().toString()
                val dailySetCreateEventArgs = DailySetCreateEventArgs(dailySetName, uid, userState.currentUserUid, type, icon)
                performProcess(service,
                    eventType = DailySetEventType.Create,
                    eventArgs = dailySetCreateEventArgs,
                    onBefore = {},
                    onCompletion = {
                        dailySetCreateState.clean()
                        dailySetCreateState.dialogOpen.value = false
                    }
                )
            }
        }

        Column (
            modifier = Modifier.verticalScroll(state = scrollState, enabled = true)
        ) {
            DailySetAddPart(dailySetCreateState = dailySetCreateState)
            DailySetAutoPart()
            DailySetUserPart(dailySets) {
                // change the current dailySet uid to selected
                mainViewModel.currentDailySetUid.postValue(it.uid)
                // changed the target page
                when(it.type) {
                    DailySetType.Normal -> {
                        nav.action.toNormalDailySet()
                    }
                    DailySetType.Clazz -> {
                        nav.action.toClazzDailySet()
                    }
                    DailySetType.TaskSpecific -> {
                        nav.action.toTaskDailySet()
                    }
                }
            }
        }

        DailySetCreateDialogCover(
            dailySetProcessor = dailySetProcessor ,
            dailySetCreateState = dailySetCreateState)
    }


}

/**
 * the add set part for dailySet.
 * .add
 */
@Composable
fun DailySetAddPart(
    dailySetCreateState: DailySetCreateState
) {
    ProfileMenuItem(
        icon = ImageResource.add(),
        next = false,
        title = stringResource(R.string.dailyset_list_add),
        content = "") {
        dailySetCreateState.dialogOpen.value = true
    }
}

/**
 * the auto generated set part for dailySet.
 * .auto
 */
@Composable
fun DailySetAutoPart() {
    Column {
        TitleSpace()
        ProfileMenuItem(icon = ImageResource.set_auto(), next = true, title = stringResource(R.string.class_table), content = "")
        ProfileMenuItem(icon = ImageResource.set_duration(), next = true, title = stringResource(R.string.time_duration), content = "")
        ProfileMenuItem(icon = ImageResource.set_star(), next = true, title = stringResource(R.string.important), content = "")
    }
}

/**
 * the user set part for dailySet
 * .user
 */
@Composable
fun DailySetUserPart(dailySets: List<DailySet>, onClick: (DailySet) -> Unit) {
    val groupedDailySets = dailySets.groupBy { it.type }

    @Composable
    fun list(dailySets: List<DailySet>) {
        Column {
            dailySets.forEach {
                DailySetElement(dailySet = it, onClick = onClick)
            }
        }
    }

    Column {
        val normalList = groupedDailySets[DailySetType.Normal]
        if (normalList != null) {
            TitleSpace(title = stringResource(id = R.string.dailyset_type_normal))
            list(normalList)
        }
        val clazzList = groupedDailySets[DailySetType.Clazz]
        if (clazzList != null) {
            TitleSpace(title = stringResource(id = R.string.dailyset_type_clazz))
            list(clazzList)
        }
        val taskList = groupedDailySets[DailySetType.TaskSpecific]
        if (taskList != null) {
            TitleSpace(title = stringResource(id = R.string.dailyset_type_task))
            list(taskList)
        }
    }
}

/**
 * dailySet element of user part.
 */
@Composable
fun DailySetElement(dailySet: DailySet, onClick: (DailySet) -> Unit) {
    val icon = dailySet.icon.toImageResource()
    val useTint = dailySet.icon == null

    ProfileMenuItem(
        icon = icon,
        useTint = useTint,
        next = true,
        title = dailySet.name,
        content = "",
        onClick = {
            onClick(dailySet)
        }
    )
}
