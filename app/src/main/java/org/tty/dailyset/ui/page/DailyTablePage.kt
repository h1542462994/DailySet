package org.tty.dailyset.ui.page

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.data.processor.DailyTableProcessor
import org.tty.dailyset.data.scope.DataScope
import org.tty.dailyset.event.*
import org.tty.dailyset.model.entity.DailyCell
import org.tty.dailyset.model.entity.DailyRC
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.lifetime.*
import org.tty.dailyset.ui.component.*
import org.tty.dailyset.ui.theme.LocalPalette
import org.tty.dailyset.ui.utils.toIntArray
import org.tty.dailyset.ui.utils.toShortString
import org.tty.dailyset.ui.utils.toWeekDayString
import org.tty.dailyset.model.entity.DailyTRC
import java.util.*

/**
 * Manage DailyTable Settings
 */
@Composable
fun DailyTablePage() {
    // TODO: 2021/3/28 仅Page节点可以拥有状态参量，其他所有子构件为无状态。所有事件必须在根节点进行处理。


    val dropDownTitleOpenState = remember { mutableStateOf(false) }
    val dropDownExtensionOpenState = remember { mutableStateOf(false) }
    val columnState = rememberLazyListState()


    with(DataScope) {
        val mainViewModel = mainViewModel()
        val service = mainViewModel().service

        val dailyTableSummaries by dailyTableSummaries()
        val currentDailyTRC by currentDailyTableDetail()
        val currentUserState by currentUserState()


        val dailyTableCreateState = dailyTableCreateState()
        val dailyTableDeleteState = dailyTableDeleteState()
        val dailyTableAddRowState = dailyTableAddRowState()
        //val dailyTableState = dailyTableState()
        val dailyTableState2 by dailyTableState2()
        val dailyTableProcessor = object: DailyTableProcessor {
            override fun onCreate(dailyTableName: String, currentDailyTable: DailyTable) {
                val uid = UUID.randomUUID().toString()
                val createEventArgs = DailyTableCreateEventArgs(dailyTableName, currentDailyTable, uid, currentUserState.currentUserUid)
                performProcess(service, DailyTableEventType.create, createEventArgs,
                        onBefore = {},
                        onCompletion = { e ->
                            if (e is DailyTableCreateEventArgs) {
                                mainViewModel.currentDailyTableUid.postValue(e.uid)
                            }
                        }
                    )


//                dailyTableCreateFromTemplate(service, currentUserUid = currentUserState.currentUserUid, dailyTableName, cloneFrom =currentDailyTable, null,
//                    onCompletion = { dailyTableUid ->
//                        // operation after create DailyTable on success
//                        mainViewModel.currentDailyTableUid.postValue(dailyTableUid)
//                    })
            }

            override fun onDelete(dailyTRC: DailyTRC) {
                // TODO: 2021/5/2 加入检查代码，以防止DailyTask引用到空的DailyTable,DailyRow,DailyCell.
                val deleteEventArgs = DailyTableDeleteEventArgs(dailyTRC)
                performProcess(service, DailyTableEventType.delete, deleteEventArgs,
                        onBefore = { mainViewModel.currentDailyTableUid.postValue(DailyTable.default) },
                        onCompletion = {}
                    )

//                dailyTableDelete(service, dailyTRC = dailyTRC, onBefore = {
//                    // change the currentDailyTable to DailyTable.default() before delete.
//                    mainViewModel.currentDailyTableUid.postValue(DailyTable.default)
//                })
            }

            override fun onAddRow(weekDays: List<WeekDayState>) {
                Log.d("DailyTablePage", "dailyTableAddRow:${weekDays}")
                val dailyTableAddRowEventArgs = DailyTableAddRowEventArgs(dailyTRC = currentDailyTRC, weekDays = weekDays.toIntArray())
                performProcess(service, DailyTableEventType.addRow, dailyTableAddRowEventArgs,
                        onBefore = {},
                        onCompletion = {
                            dailyTableAddRowState.dialogOpen.value = false
                            mainViewModel.currentDailyTableUid.postValue(currentDailyTRC.dailyTable.uid)
                        }
                    )

//                dailyTableAddRow(service, dailyTRC = currentDailyTRC, weekDays = weekDays.toIntArray(), onCompletion = {
//                    dailyTableAddRowState.dialogOpen.value = false
//                    mainViewModel.currentDailyTableUid.postValue(currentDailyTRC.dailyTable.uid)
//                })
            }

            override fun onClickWeekDay(rowIndex: Int, weekDay: Int) {
                val dailyTableClickWeekDayEventArgs = DailyTableClickWeekDayEventArgs(currentDailyTRC, rowIndex, weekDay)
                performProcess(service, DailyTableEventType.clickWeekDay, dailyTableClickWeekDayEventArgs,
                        onBefore = {},
                        onCompletion = {}
                    )
            }
        }

        Column {
            CenterBar(true, LocalNav.current.action.upPress,
                extensionArea = {
                    DailyTableExtensionDropDown(
                        dropDownExtensionOpenState = dropDownExtensionOpenState,
                        dailyTableState2 = dailyTableState2,
                        dailyTableCreateState = dailyTableCreateState,
                        dailyTableAddRowState = dailyTableAddRowState,
                        dailyTableDeleteState = dailyTableDeleteState
                    )
                }) {
                DailyTableTitle(dailyTable = currentDailyTRC.dailyTable, userState = currentUserState) {
                    dropDownTitleOpenState.value = true
                }
                DailyTableDropDown(
                    dailyTableSummaries = dailyTableSummaries,
                    userState = currentUserState,
                    dropDownOpenState = dropDownTitleOpenState) { dailyTable ->
                    // TODO: 2021/4/7 优化代码
                    mainViewModel.currentDailyTableUid.value = dailyTable.uid

                    dropDownTitleOpenState.value = false
                }
            }
            LazyColumn(state = columnState) {
                item {
                    DailyTableContent(dailyTableState2 = dailyTableState2, userState = currentUserState, dailyTableProcessor = dailyTableProcessor)
                }
            }
        }
        // register dialogs
        DailyTableAddRowDialogCover(dailyTableAddRowState = dailyTableAddRowState, dailyTableProcessor = dailyTableProcessor)
        DailyTableDeleteDialogCover(dailyTableState2 = dailyTableState2, dailyTableDeleteState = dailyTableDeleteState, dailyTableProcessor = dailyTableProcessor)
        DailyTableCreateDialogCover(dailyTableCreateState = dailyTableCreateState, userState = currentUserState,dailyTableProcessor = dailyTableProcessor)
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
 * DailyTablePage .centerBar .extensionArea .dropDown
 * @param dropDownExtensionOpenState state of dropDown
 * @param dailyTableState2 state of DailyTable
 * @param dailyTableCreateState state of createDialog for DailyTable
 * @param dailyTableAddRowState state of addRowDialog for DailyRow
 * @param dailyTableDeleteState state of deleteDailog for DailyTable
 */
@Composable
fun DailyTableExtensionDropDown(
    dropDownExtensionOpenState: MutableState<Boolean>,
    dailyTableState2: DailyTableState2,
    dailyTableCreateState: DailyTableCreateState,
    dailyTableAddRowState: DailyTableAddRowState,
    dailyTableDeleteState: DailyTableDeleteState)
{
    BarExtension(expandedState = dropDownExtensionOpenState) {
        DropdownMenuItem(onClick = {
            // open createDialog for DailyTable
            dailyTableCreateState.dialogOpen.value = true
            // close the dropDown
            dropDownExtensionOpenState.value = false
        }) {
            IconText(imageVector = Icons.Filled.Add , text = stringResource(id = R.string.time_table_add))
        }


        if (!dailyTableState2.readOnly){
            if (dailyTableState2.canAddRow) {
                // 添加组
                DropdownMenuItem(onClick = {
                    // open addRowDialog for DailyTable
                    dailyTableAddRowState.dialogOpen.value = true
                    // close the dropDown
                    dropDownExtensionOpenState.value = false
                }) {
                    IconText(imageVector = Icons.Filled.Add, text = stringResource(id = R.string.time_table_group_add))
                }
            }


            // 当课程表可编辑时，意味着其可以删除。
            DropdownMenuItem(onClick = {
                // open deleteDialog for DailyTable
                dailyTableDeleteState.dialogOpen.value = true
                // close the dropDown
                dropDownExtensionOpenState.value = false
            }) {
                IconText(imageVector = Icons.Filled.Delete, text = stringResource(id = R.string.time_table_delete))
            }


        }
    }
}

/**
 * DailyTablePage .dropDown
 */
@Composable
fun DailyTableDropDown(dailyTableSummaries: List<DailyTable>, userState: UserState, dropDownOpenState: MutableState<Boolean>, onSelectItem: (DailyTable) -> Unit) {
    DropdownMenu(
        modifier = Modifier.width(150.dp),
        offset = DpOffset(x = 0.dp, y = 56.dp), expanded = dropDownOpenState.value, onDismissRequest = { dropDownOpenState.value = false }) {
        dailyTableSummaries.forEach { dailyTable ->
            DropdownMenuItem(onClick = { onSelectItem(dailyTable) }) {
                DailyTableTitleDescription(dailyTable = dailyTable, userState = userState, color = LocalPalette.current.textColor)
            }
        }
    }
}

/**
 * DailyTablePage .content
 */
@Composable
fun DailyTableContent(dailyTableState2: DailyTableState2, userState: UserState, dailyTableProcessor: DailyTableProcessor) {
    DailyTableTipBox(dailyTable = dailyTableState2.dailyTRC.dailyTable, userState = userState)

    ProfileMenuItem(icon = Icons.Filled.Place, title = "预览", next = true, content =
    "点击以进行预览", onClick = LocalNav.current.action.toTimeTablePreview
    )

    dailyTableState2.dailyTRC.dailyRCs.forEachIndexed { index, item ->
        DailyRCContent(dailyRC = item, index = index, weekDayStateList = dailyTableState2.weekDays[index], dailyTableProcessor)
    }


    //DailyTableAddRowButton(dailyTableState = dailyTableState)
}

/**
 * DailyTablePage .content .button<add_row>
 */
@Composable
@Deprecated("move to .extensionArea")
fun DailyTableAddRowButton(dailyTableState2: DailyTableState2, dailyTableAddRowState: DailyTableAddRowState) {
    if (!dailyTableState2.readOnly) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 4.dp),
            enabled = dailyTableState2.canAddRow,
            onClick = { dailyTableAddRowState.dialogOpen.value = true }) {
           Text(text = "添加组")
        }
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
fun DailyRCContent(dailyRC: DailyRC, index: Int, weekDayStateList: List<WeekDayState>, dailyTableProcessor: DailyTableProcessor) {
    val dailyRow = dailyRC.dailyRow
    ProfileMenuGroup(title = "组${index + 1}") {
        ProfileMenuItem(title = "适用星期", next = false, content = {

            DailyRCContentWeekDay(weekDayStateList = weekDayStateList, onItemSelect = {
                dailyTableProcessor.onClickWeekDay(index, it + 1)
            })
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
 * DailyTablePage .content.RC.weekDay
 */
@Composable
fun DailyRCContentWeekDay(weekDayStateList: List<WeekDayState>, onItemSelect: (Int) -> Unit) {
    // TODO: 2021/4/7 修改样式
    Row {
        weekDayStateList.forEachIndexed { index,weekDayState ->
            Spacer(
                modifier = Modifier.width(4.dp)
            )
            Badge(readOnly = weekDayState.readOnly, checked = weekDayState.checked, text = index.toWeekDayString().substring(1)) {
                onItemSelect(index)
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

/**
 * DailyTablePage .title d .dialog<create> version = 1
 */
@Composable
@Deprecated("there's bug on dismissing the Dialog. use [DailyTableCreateDialogCover] instead.")
fun DailyTableCreateDialog(dailyTableCreateState: DailyTableCreateState) {
    val dialogOpen by dailyTableCreateState.dialogOpen
    val focusRequester = remember { FocusRequester() }

    if (dialogOpen) {
        AlertDialog(
            modifier = Modifier
                .padding(16.dp),
            onDismissRequest = {},
            //onDismissRequest = { dailyTableCreateState.dialogOpen.value = false },
            buttons = {
                Row(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        modifier = Modifier.background(color = LocalPalette.current.background1),
                        onClick = {
                            focusRequester.captureFocus()
                            focusRequester.freeFocus()

                        }
                    ){
                        Text("取消")
                    }
                    Button(
                        onClick = {  }) {
                        Text("确认")
                    }
                }
            },
            title = {
                Text("创建课程表")
            },
            text = {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        modifier = Modifier.focusRequester(focusRequester),
                        value = dailyTableCreateState.name.value,
                        placeholder = {
                            Text("课程名")
                        },
                        onValueChange = {
                            dailyTableCreateState.name.value = it
                        })
                }

            }
        )

    }


}

/**
 * DailyTable .extensionArea.option.dialog<Delete>
 * @param dailyTableState state of the DailyTable
 */
@Composable
fun DailyTableDeleteDialogCover(dailyTableState2: DailyTableState2, dailyTableDeleteState: DailyTableDeleteState, dailyTableProcessor: DailyTableProcessor) {
    // readOnly的项无法删除
    if (!dailyTableState2.readOnly) {
        NanoDialog(title = "删除时间表", dialogState = dailyTableDeleteState) {
            Text("你确认要删除时间表${dailyTableState2.dailyTRC.dailyTable.name}吗?")
            // TODO: 2021/5/2 将按钮抽取成一个控件
            NanoDialogButton(text = "删除", error = true) {
                dailyTableProcessor.onDelete(dailyTableState2.dailyTRC)
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
fun DailyTableCreateDialogCover(dailyTableCreateState: DailyTableCreateState, userState: UserState, dailyTableProcessor: DailyTableProcessor) {
    var name by dailyTableCreateState.name
    val dailyTableSummaries by dailyTableCreateState.dailyTableSummaries
    var currentDailyTable by dailyTableCreateState.currentDailyTable
    val isValid by derivedStateOf {
        name.isNotEmpty() && name.length in 2..20
    }

    NanoDialog(title = stringResource(id = R.string.time_table_add), dialogState = dailyTableCreateState) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text("时间表名称") },
            value = name, onValueChange = {
                name = it
            })
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            ComboBox(title = "基于..创建", data = dailyTableSummaries, onSelected = { currentDailyTable = it }) {
                DailyTableTitleDescription(dailyTable = it, userState = userState, color = LocalPalette.current.textColor)

            }
        }
        NanoDialogButton(
            text = "创建",
            enabled = isValid
        ) {
            dailyTableProcessor.onCreate(name, currentDailyTable)
        }
    }

}

/**
 * DailyTablePage .content .dialog<add_row> version = 1
 */
@Composable
fun DailyTableAddRowDialogCover(dailyTableAddRowState: DailyTableAddRowState, dailyTableProcessor: DailyTableProcessor) {
    NanoDialog(title = stringResource(id = R.string.time_table_group_add), dialogState = dailyTableAddRowState) {
        ProfileMenuItem(title = "适用星期", next = false, content = {
            DailyRCContentWeekDay(weekDayStateList = dailyTableAddRowState.lastState, onItemSelect = { index ->
                dailyTableAddRowState.onItemClick(index)
            })
        })
        val selectionCount = dailyTableAddRowState.lastState.count {
            it.checked
        }
        val buttonEnabled = selectionCount > 0

        NanoDialogButton(text = "创建", enabled = buttonEnabled) {
            dailyTableProcessor.onAddRow(dailyTableAddRowState.lastState)
        }
    }
}

