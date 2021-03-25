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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.data.scope.currentDailyTable
import org.tty.dailyset.data.scope.currentDailyTableDetail
import org.tty.dailyset.data.scope.dailyTableSummaries
import org.tty.dailyset.data.scope.groupDailyCells
import org.tty.dailyset.model.entity.*
import org.tty.dailyset.ui.component.CenterBar
import org.tty.dailyset.ui.component.ProfileMenuGroup
import org.tty.dailyset.ui.component.ProfileMenuItem
import org.tty.dailyset.ui.component.TitleSpace
import org.tty.dailyset.ui.theme.LocalPalette
import org.tty.dailyset.ui.utils.*

@Composable
fun DailyTablePage() {
    //calculate
    val dailyTableSummaries by dailyTableSummaries()
    val currentDailyTable by currentDailyTable()
    var dropDownOpen by remember { mutableStateOf(false) }


    Column {
        CenterBar(true, LocalNav.current.action.upPress) {
            DailyTableTitle(currentDailyTable = currentDailyTable) {
                dropDownOpen = true
            }
            DropdownMenu(offset = DpOffset(x = 0.dp, y = 56.dp), expanded = dropDownOpen, onDismissRequest = { dropDownOpen = false }) {
                dailyTableSummaries.forEach { dailyTable ->
                    DropdownMenuItem(onClick = { }) {
                        Text(text = dailyTable.name)
                    }
                }
            }
        }

        val columnState = rememberLazyListState()
        LazyColumn(state = columnState) {
           item {
               DailyTableContent()
           }
        }


    }
}

@Composable
fun DailyTableContent() {
    ProfileMenuItem(icon = Icons.Filled.Place, title = "预览", next = true, content =
        "点击以进行预览", onClick = LocalNav.current.action.toTimeTablePreview
    )

    val currentDailyTRC by currentDailyTableDetail()
    @Suppress
    val c: DailyTRC? = currentDailyTRC
    if (c != null){
        c.dailyRCs.forEachIndexed { index, item ->
            DailyRCContent(dailyRC = item, index = index)
        }
    } else {
        Text("Not Initialized")
    }
}

@Composable
fun DailyRCContent(dailyRC: DailyRC, index: Int) {
    val dailyRow = dailyRC.dailyRow
    ProfileMenuGroup(title = "组${index + 1}") {
        ProfileMenuItem(title = "适用星期", next = false, content =
            dailyRow.weekdays.joinToString(" ")
        )
        ProfileMenuItem(title = "节数", next = true, content =
            dailyRow.counts.joinToString(" ")
        )
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

@Composable
fun DailyCellContent(dailyCell: DailyCell, index: Int) {
    ProfileMenuItem(title = "第${index + 1}节", next = true,
        content = "${dailyCell.start.toShortString()}-${dailyCell.end.toShortString()}"
    )
}




@Composable
fun DailyTableTitle(currentDailyTable: DailyTable, onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .wrapContentSize(align = Alignment.Center)
            .clickable(onClick = onClick)
    ) {
        Text(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .wrapContentSize(align = Alignment.Center),
            text = stringResource(id = R.string.time_table),
            fontSize = 18.sp)
        Row(
            modifier = Modifier
                .wrapContentSize(align = Alignment.Center),
        ) {
            Text(
                modifier = Modifier.align(alignment = Alignment.CenterVertically),
                text = currentDailyTable.name, color = LocalPalette.current.textColorDetail)
            Icon(
                modifier = Modifier.scale(0.8f),
                imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = LocalPalette.current.textColorDetail)
        }
    }
}

@Preview
@Composable
fun DailyTableTitlePreview() {
    DailyTableTitle(currentDailyTable = DailyTable.default())
}



