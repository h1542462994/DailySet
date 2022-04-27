package org.tty.dailyset.ui.page

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tty.dailyset.R
import org.tty.dailyset.common.datetime.minutesTo
import org.tty.dailyset.database.processor.DailyTableProcessor
import org.tty.dailyset.bean.lifetime.UserState
import org.tty.dailyset.bean.lifetime.dailytable.*
import org.tty.dailyset.ui.component.*
import org.tty.dailyset.ui.theme.LocalPalette
import java.time.LocalTime
import java.time.temporal.ChronoUnit

/**
 * DailyTable .extensionArea.option.dialog<Delete>
 * @param dailyTableState state of the DailyTable
 */
@Composable
fun DailyTableDeleteDialogCover(
    dailyTableState2: DailyTableState2,
    dailyTableDeleteState: DailyTableDeleteState,
    dailyTableProcessor: DailyTableProcessor
) {
    // readOnly的项无法删除
    if (!dailyTableState2.readOnly) {
        NanoDialog(title = "删除时间表", dialogState = dailyTableDeleteState) {
            Text("你确认要删除时间表${dailyTableState2.dailyTRC.dailyTable.name}吗?")
            NanoDialogButton(text = "删除", error = true) {
                dailyTableProcessor.onDelete()
            }
        }
    }

}

/**
 * DailyTablePage .title d .dialog<create> version = 2
 * @param dailyTableCreateState dialogState for create DailyTable
 * @param userState current userState
 */
@Composable
fun DailyTableCreateDialogCover(
    dailyTableCreateState: DailyTableCreateState,
    userState: UserState,
    dailyTableProcessor: DailyTableProcessor
) {
    var name by dailyTableCreateState.name
    val dailyTableSummaries by dailyTableCreateState.dailyTableSummaries
    var currentDailyTable by dailyTableCreateState.currentDailyTable
    val isValid by derivedStateOf {
        name.isNotEmpty() && name.length in 2..20
    }

    NanoDialog(
        title = stringResource(id = R.string.time_table_add),
        dialogState = dailyTableCreateState
    ) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text("时间表名称") },
            value = name, onValueChange = {
                name = it
            })
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            ComboBox(
                title = "基于..创建",

                data = dailyTableSummaries,
                onSelected = { currentDailyTable = it }) {
                DailyTableTitleDescription(
                    dailyTable = it,
                    userState = userState,
                    color = LocalPalette.current.primary
                )

            }
        }
        NanoDialogButton(
            text = "创建",
            enabled = isValid
        ) {
            dailyTableProcessor.onCreate(name)
        }
    }

}

/**
 * DailyTablePage .content .dialog<add_row> version = 1
 */
@Composable
fun DailyTableAddRowDialogCover(
    dailyTableAddRowState: DailyTableAddRowState,
    dailyTableProcessor: DailyTableProcessor
) {
    NanoDialog(
        title = stringResource(id = R.string.time_table_group_add),
        dialogState = dailyTableAddRowState
    ) {
        Text(
            "适用星期",
            modifier = Modifier.padding(all = 8.dp),
            fontWeight = FontWeight.Bold
        )
        Row (
            modifier = Modifier
                .align(Alignment.End)
                .padding(all = 8.dp)
        ) {
            DailyRCContentWeekDay(
                weekDayStateList = dailyTableAddRowState.lastState,
                onItemSelect = { index ->
                    dailyTableAddRowState.onItemClick(index)
                })
        }

        val selectionCount = dailyTableAddRowState.lastState.count {
            it.checked
        }
        val buttonEnabled = selectionCount > 0

        NanoDialogButton(text = "创建", enabled = buttonEnabled) {
            dailyTableProcessor.onAddRow(dailyTableAddRowState.lastState)
        }
    }
}

/**
 * DailyTablePage .dialog<rename>
 */
@Composable
fun DailyTableRenameDialogCover(
    dailyTableRenameState: DailyTableRenameState,
    dailyTableProcessor: DailyTableProcessor
) {
    var name by dailyTableRenameState.name
    val isValid by derivedStateOf {
        name.isNotEmpty() && name.length in 2..20
    }

    NanoDialog(
        title = "${stringResource(id = R.string.time_table_rename)}:${dailyTableRenameState.dailyTable.name}",
        dialogState = dailyTableRenameState
    ) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text("时间表名称") },
            value = name, onValueChange = {
                name = it
            })
        NanoDialogButton(
            text = "重命名",
            enabled = isValid
        ) {
            dailyTableProcessor.onRename(name)
        }
    }
}

/**
 * DailyTablePage .dialog<deleteRow>
 */
@Composable
fun DailyTableDeleteRowDialogCover(
    dailyTableDeleteRowState: DailyTableDeleteRowState,
    dailyTableProcessor: DailyTableProcessor
) {
    val rowIndex by dailyTableDeleteRowState.rowIndex
    NanoDialog(
        title = "${stringResource(id = R.string.time_table_group_delete)}:组${rowIndex + 1}",
        dialogState = dailyTableDeleteRowState
    ) {
        Text("你确认要删除组${rowIndex + 1}吗?")
        NanoDialogButton(text = "删除", error = true) {
            dailyTableProcessor.onDeleteDailyRow(rowIndex)
        }
    }
}

/**
 * DailyTablePage .dialog<modifySection>
 */
@Composable
fun DailyTableModifySectionDialogCover(
    dailyTableModifySectionState: DailyTableModifySectionState,
    dailyTableProcessor: DailyTableProcessor
) {
    val rowIndex by dailyTableModifySectionState.rowIndex
    val counts by dailyTableModifySectionState.counts
    NanoDialog(
        title = stringResource(id = R.string.time_table_group_modify_section, rowIndex),
        dialogState = dailyTableModifySectionState
    ) {
        var count1 by remember {
            mutableStateOf(counts[0])
        }
        var count2 by remember {
            mutableStateOf(counts[1])
        }
        var count3 by remember {
            mutableStateOf(counts[2])
        }

        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            CountSelector(text = "上午", index = counts[0] - 1) { count1 = it + 1 }
            CountSelector(text = "下午", index = counts[1] - 1) { count2 = it + 1 }
            CountSelector(text = "晚上", index = counts[2] - 1) { count3 = it + 1 }
        }

        //Text("#placeholder, ${rowIndex},${counts.joinToString(",")}")
//        ListSelector(
//            height = 100.dp,
//            state = lazyListState,
//            cellHeight = 40.dp
//        ) {
//            items(10) { index ->
//                Row(
//                    modifier = Modifier
//                        .height(40.dp)
//                ) {
//
//                    val isCurrent = lazyListState.firstVisibleItemIndex == index + 1
//                    Text(
//                        text = "第${index}项",
//                        modifier = Modifier.align(Alignment.CenterVertically),
//                        color = if (isCurrent) MaterialTheme.colors.primary else Color.Unspecified
//                        //modifier = Modifier.align(Alignment.CenterVertically)
//                    )
//                }
//
//            }
//        }


        NanoDialogButton(text = "修改") {
            Log.d("DailyTablePage", "section:${count1},${count2},${count3}")
            dailyTableProcessor.onModifySection(rowIndex, intArrayOf(count1, count2, count3))
        }
    }
}


@Composable
fun DailyTableModifyCellDialogCover(
    dailyTableModifyCellState: DailyTableModifyCellState,
    dailyTableProcessor: DailyTableProcessor
) {
    val tag = "DailyTableModifyCellDialogCover"
    val state by dailyTableModifyCellState.modifyCellStateWrap
    val (rowIndex, cellIndex, min, start, end) = state

    LaunchedEffect(key1 = state, block = {
        Log.d(tag, state.toString())
    })

    data class TimeRange(
        val start: LocalTime,
        val end: LocalTime
    )

    var current by remember(key1 = state) {
        mutableStateOf(TimeRange(start, end))
    }
    val endMin =
        minOf(current.end, current.start.plus(5, ChronoUnit.MINUTES))

    // TODO: 溢出时间修正
    val isEndTimeValid = true


    NanoDialog(
        title = "调整该节课时间",
        dialogState = dailyTableModifyCellState) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.CenterHorizontally)
        ) {
            Column {
                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .wrapContentWidth(align = Alignment.CenterHorizontally),
                    text = "开始",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                TimeSelector(
                    height = 180.dp,
                    cellHeight = 40.dp,
                    initTime = current.start,
                    min = min
                ) {
                    val span = current.start minutesTo current.end

                    current = TimeRange(it, it.plus(span, ChronoUnit.MINUTES))
                }
            }
            Column {
                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .wrapContentWidth(align = Alignment.CenterHorizontally),
                    text = " ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .height(180.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    text = " - ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column {
                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .wrapContentWidth(align = Alignment.CenterHorizontally),
                    text = "结束",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                // if the current.end is the follow day, then hidden it.
                if (isEndTimeValid) {
                    TimeSelector(
                        height = 180.dp,
                        cellHeight = 40.dp,
                        initTime = current.end,
                        min = endMin
                    ) {
                        current = current.copy(
                            end = it
                        )
                    }
                } else {
                    // TODO: 2021/6/19 use calculation function method
                    Spacer(modifier = Modifier.width(85.dp))
                }

            }
        }

        NanoDialogButton(text = "修改", enabled = isEndTimeValid) {
            Log.d("DailyTablePage", "modifyCell:${rowIndex},${cellIndex},${current.start},${current.end}")
            dailyTableProcessor.onModifyCell(rowIndex, cellIndex, current.start, current.end)
        }
    }
}
