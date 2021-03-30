package org.tty.dailyset.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.data.scope.DataScope
import org.tty.dailyset.model.entity.DailyCell
import org.tty.dailyset.model.entity.DailyRC
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.lifetime.UserState
import org.tty.dailyset.ui.component.*
import org.tty.dailyset.ui.theme.LocalPalette
import org.tty.dailyset.ui.utils.toShortString

/**
 * Manage DailyTable Settings
 */
@Composable
fun DailyTablePage() {
    // TODO: 2021/3/28 仅Page节点可以拥有状态参量，其他所有子构件为无状态。所有事件必须在根节点进行处理。


    var dropDownOpen by remember { mutableStateOf(false) }
    val columnState = rememberLazyListState()


    with(DataScope) {
        val dailyTableSummaries by dailyTableSummaries()
        //val currentDailyTable by currentDailyTable()
        val currentDailyTRC by currentDailyTableDetail()
        val weekDaySelectState = weekDaySelectState()
        val currentUserState by currentUserState()
        val tempCurrentDailyTRC: DailyTRC? = currentDailyTRC

        if (tempCurrentDailyTRC != null) {
            Column {
                CenterBar(true, LocalNav.current.action.upPress) {
                    DailyTableTitle(dailyTable = tempCurrentDailyTRC.dailyTable, userState = currentUserState) {
                        dropDownOpen = true
                    }
                    DailyTableDropDown(
                        dailyTableSummaries = dailyTableSummaries,
                        userState = currentUserState,
                        dropDownOpen = dropDownOpen,
                        onDismissRequest = { dropDownOpen = false }) {
                    }
                }
                LazyColumn(state = columnState) {
                    item {
                        DailyTableContent(tempCurrentDailyTRC, userState = currentUserState)
                    }
                }
            }
        }
    }
}

/**
 * DailyTablePage .title / DailyTablePagePreview .title
 */
@Composable
fun DailyTableTitle(dailyTable: DailyTable, userState: UserState, isPreviewPage: Boolean = false, onClick: (() -> Unit)? = null) {
    var modifier = Modifier
        .wrapContentSize(align = Alignment.Center)

    if (onClick != null){
        modifier = modifier.clickable { onClick() }
    }

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .wrapContentSize(align = Alignment.Center),
            text = if (isPreviewPage) stringResource(id = R.string.time_table_preview) else stringResource(id = R.string.time_table),
            fontSize = 18.sp)
        DailyTableTitleDetail(dailyTable = dailyTable, userState= userState, isPreviewPage = isPreviewPage)
    }
}

/**
 * DailyTablePage .title.detail / DailyTablePage .dropDown.list@select.content
 */
@Composable
fun DailyTableTitleDetail(dailyTable: DailyTable, userState: UserState, isPreviewPage: Boolean) {
    Row(
        modifier = Modifier
            .wrapContentSize(align = Alignment.Center),
    ) {
        DailyTableTitleDescription(dailyTable = dailyTable, userState = userState, color = LocalPalette.current.textColorDetail)
        if (!isPreviewPage) {
            Icon(
                modifier = Modifier.scale(0.8f),
                imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = LocalPalette.current.textColorDetail)
        }
    }

}

/**
 * DailyTablePage .title.description
 */
@Composable
fun DailyTableTitleDescription(dailyTable: DailyTable, userState: UserState, color: Color) {
    Row(
        modifier = Modifier.wrapContentSize(align = Alignment.Center)
    ) {
        if (dailyTable.global) {
            Icon(
                modifier = Modifier.scale(0.8f),
                imageVector = Icons.Filled.Lock, contentDescription = null, tint = color
            )

        } else if (dailyTable.referenceUid != userState.currentUserUid) {
            Icon(
                modifier = Modifier.scale(0.8f),
                imageVector = Icons.Filled.Share, contentDescription = null, tint = color
            )
        }
        Text(
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            text = dailyTable.name, color = color)
    }
}

/**
 * DailyTablePage .dropDown
 */
@Composable
fun DailyTableDropDown(dailyTableSummaries: List<DailyTable>, userState: UserState, dropDownOpen: Boolean, onDismissRequest: () -> Unit, onClick: (DailyTable?) -> Unit) {
    DropdownMenu(
        modifier = Modifier.width(150.dp),
        offset = DpOffset(x = 0.dp, y = 56.dp), expanded = dropDownOpen, onDismissRequest = onDismissRequest) {
        dailyTableSummaries.forEach { dailyTable ->
            DropdownMenuItem(onClick = { onClick(dailyTable) }) {
                DailyTableTitleDescription(dailyTable = dailyTable, userState = userState, color = LocalPalette.current.textColor)
            }

            DropdownMenuItem(onClick = { onClick(null) }) {
                Row(
                    modifier = Modifier
                        .wrapContentSize(align = Alignment.Center)
                ) {
                    Icon(
                        modifier = Modifier.scale(0.8f),
                        imageVector = Icons.Filled.Add, contentDescription = null, tint = LocalPalette.current.textColor
                    )
                    Text(
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        text = stringResource(R.string.time_table_add), color = LocalPalette.current.textColor
                    )
                }
            }
        }
    }
}

/**
 * DailyTablePage .content
 */
@Composable
fun DailyTableContent(currentDailyTRC: DailyTRC, userState: UserState) {
    DailyTableTipBox(dailyTable = currentDailyTRC.dailyTable, userState = userState)

    ProfileMenuItem(icon = Icons.Filled.Place, title = "预览", next = true, content =
    "点击以进行预览", onClick = LocalNav.current.action.toTimeTablePreview
    )

    currentDailyTRC.dailyRCs.forEachIndexed { index, item ->
        DailyRCContent(dailyRC = item, index = index)
    }
}

/**
 * DailyTablePage .content.tipBox
 */
@Composable
fun DailyTableTipBox(dailyTable: DailyTable, userState: UserState) {
    if (dailyTable.global) {
        TipBox(value = "系统默认时间表 (只读)")
    } else if(dailyTable.referenceUid != userState.currentUserUid) {
        TipBox(value = "共享时间表 (只读)")
    }
}

/**
 * DailyTablePage .content.RC
 */
@Composable
fun DailyRCContent(dailyRC: DailyRC, index: Int) {
    val dailyRow = dailyRC.dailyRow
    ProfileMenuGroup(title = "组${index + 1}") {
        ProfileMenuItem(title = "适用星期", next = false, content = {
            // TODO: 2021/3/28 完善该功能
            Badge(
                borderColor = LocalPalette.current.background2,
                background = LocalPalette.current.background1,
                text = "1"
            )
        })
        ProfileMenuItem(title = "节数", next = true, content =
            dailyRow.counts.joinToString(" ")
        )

        with(DataScope) {
            val groupedDailyCells = groupDailyCells(dailyRC.dailyCells)

            TitleSpace(title = "上午")
            groupedDailyCells[0]?.forEachIndexed { index, dailyCell ->
                DailyCellContent(dailyCell = dailyCell, index = index)
            }
            TitleSpace(title = "下午")
            groupedDailyCells[1]?.forEachIndexed { index, dailyCell ->
                DailyCellContent(dailyCell = dailyCell, index = index)
            }
            TitleSpace(title = "晚上")
            groupedDailyCells[2]?.forEachIndexed { index, dailyCell ->
                DailyCellContent(dailyCell = dailyCell, index = index)
            }
        }
    }
}

/**
 * DailyTablePage .content.RC.cell
 */
@Composable
fun DailyCellContent(dailyCell: DailyCell, index: Int) {
    ProfileMenuItem(title = "第${index + 1}节", next = true,
        content = "${dailyCell.start.toShortString()}-${dailyCell.end.toShortString()}"
    )
}

