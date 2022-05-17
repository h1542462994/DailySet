package org.tty.dailyset.ui.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.tty.dailyset.R
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.bean.enums.DailySetType
import org.tty.dailyset.bean.enums.toImageResource
import org.tty.dailyset.bean.lifetime.DailySetSummary
import org.tty.dailyset.component.common.asMutableState
import org.tty.dailyset.component.common.showToast
import org.tty.dailyset.component.dailyset.DailySetCreateDialogVM
import org.tty.dailyset.component.dailyset.rememberDailySetVM
import org.tty.dailyset.ui.component.ProfileMenuItem
import org.tty.dailyset.ui.component.TitleSpace
import org.tty.dailyset.ui.image.ImageResource

@ExperimentalFoundationApi
@Composable
@UseViewModel("/dailyset")
fun DailySetPage() {
    val scrollState = rememberScrollState()
    val dailySetVM = rememberDailySetVM()
    val dailySetSummaries by dailySetVM.dailySetSummaries.collectAsState()
    val nav = rememberDailySetVM().nav

    Column (
        modifier = Modifier.verticalScroll(state = scrollState, enabled = true)
    ) {
        DailySetAddPart(dailySetCreateDialogVM = dailySetVM.dailySetCreateDialogVM)
        DailySetAutoPart()
        DailySetUserPart(dailySetSummaries) {
            when(it.type) {
                DailySetType.ClazzAuto -> nav.toDailySetClazzAuto(dailySetUid = it.uid)
                else -> showToast("暂不支持该类型.")
            }
        }
    }

    /*
    DailySetCreateDialogCover(dailySetVM.dailySetCreateDialogVM)
    */
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
    var dialogOpen by dailySetCreateDialogVM.dialogOpen.asMutableState()
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
fun DailySetUserPart(dailySetSummaries: List<DailySetSummary>, onClick: (DailySetSummary) -> Unit) {
    Column {
        TitleSpace()
        dailySetSummaries.forEach {
            DailySetElement(dailySetSummary = it, onClick = onClick)
        }
    }
}

/**
 * dailySet element of user part.
 */
@Composable
fun DailySetElement(dailySetSummary: DailySetSummary, onClick: (DailySetSummary) -> Unit) {

    ProfileMenuItem(
        icon = dailySetSummary.icon.toImageResource(),
        next = true,
        title = dailySetSummary.name,
        content = "",
        onClick = {
            onClick(dailySetSummary)
        }
    )
}


@Preview
@Composable
fun DailySetAutoPartPreview() {
    DailySetAutoPart()
}
