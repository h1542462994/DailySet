package org.tty.dailyset.ui.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.tty.dailyset.R
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.bean.entity.DailySet
import org.tty.dailyset.bean.entity.DailySetIcon
import org.tty.dailyset.bean.entity.DailySetType
import org.tty.dailyset.bean.entity.toImageResource
import org.tty.dailyset.common.datetime.epochLocalDateTime
import org.tty.dailyset.common.observable.collectAsState
import org.tty.dailyset.component.dailyset.DailySetCreateDialogVM
import org.tty.dailyset.component.dailyset.dailySetVM
import org.tty.dailyset.ui.component.ProfileMenuItem
import org.tty.dailyset.ui.component.TitleSpace
import org.tty.dailyset.ui.image.ImageResource
import java.util.*

@ExperimentalFoundationApi
@Composable
@UseViewModel("/dailyset")
fun DailySetPage() {
    val scrollState = rememberScrollState()
    val dailySetVM = dailySetVM()
    val dailySets by dailySetVM.dailySets.collectAsState()
    val nav = dailySetVM().nav

    Column (
        modifier = Modifier.verticalScroll(state = scrollState, enabled = true)
    ) {
        DailySetAddPart(dailySetCreateDialogVM = dailySetVM.dailySetCreateDialogVM)
        DailySetAutoPart()
        DailySetUserPart(dailySets) {
            // change the current dailySet uid to selected
            dailySetVM.setCurrentDailySetUid(it.uid)
            // changed the target page
            when(it.type) {
                DailySetType.Normal -> {
                    nav.toNormalDailySet()
                }
                DailySetType.Clazz -> {
                    nav.toClazzDailySet()
                }
                DailySetType.TaskSpecific -> {
                    nav.toTaskDailySet()
                }
            }
        }
    }

    DailySetCreateDialogCover(dailySetVM.dailySetCreateDialogVM)

}

/**
 * the add set part for dailySet.
 * .add
 */
@Composable
@UseViewModel("/dailyset?add")
fun DailySetAddPart(
    dailySetCreateDialogVM: DailySetCreateDialogVM
) {
    var dialogOpen by dailySetCreateDialogVM.dialogOpen.collectAsState()
    ProfileMenuItem(
        icon = ImageResource.add(),
        next = false,
        title = stringResource(R.string.dailyset_list_add),
        content = "") {
        dialogOpen = true
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


@Preview
@Composable
fun DailySetAutoPartPreview() {
    DailySetAutoPart()
}

@Preview
@Composable
fun DailySetUserPartPreview() {
    val dailySets = listOf(
        DailySet(type = DailySetType.Normal, icon = DailySetIcon.Book, updateAt = epochLocalDateTime()),
        DailySet(type = DailySetType.Clazz, icon = DailySetIcon.Book, updateAt = epochLocalDateTime()),
        DailySet(type = DailySetType.TaskSpecific, icon = DailySetIcon.Book, updateAt = epochLocalDateTime())
    )
    DailySetUserPart(dailySets) {

    }
}