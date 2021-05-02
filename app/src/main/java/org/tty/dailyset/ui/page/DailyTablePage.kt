package org.tty.dailyset.ui.page

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
import org.tty.dailyset.DailySetApplication
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.data.scope.DataScope
import org.tty.dailyset.model.entity.DailyCell
import org.tty.dailyset.model.entity.DailyRC
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.lifetime.*
import org.tty.dailyset.ui.component.*
import org.tty.dailyset.ui.theme.LocalPalette
import org.tty.dailyset.ui.utils.toShortString
import org.tty.dailyset.ui.utils.toWeekDayString

/**
 * Manage DailyTable Settings
 */
@Composable
fun DailyTablePage() {
    // TODO: 2021/3/28 仅Page节点可以拥有状态参量，其他所有子构件为无状态。所有事件必须在根节点进行处理。


    var dropDownTitleOpen by remember { mutableStateOf(false) }
    val dropDownExtensionOpenState = remember { mutableStateOf(false) }
    val columnState = rememberLazyListState()


    with(DataScope) {
        val mainViewModel = mainViewModel()
        val service = mainViewModel().service

        val dailyTableSummaries by dailyTableSummaries()
        //val currentDailyTable by currentDailyTable()
        val currentDailyTRC by currentDailyTableDetail()
        val currentUserState by currentUserState()
        //val tempCurrentDailyTRC: DailyTRC = currentDailyTRC

        val dailyTableCreateState = dailyTableCreateState(initialName = "",
            onCreate = { dailyTableName, currentDailyTable ->
                dailyTableCreateFromTemplate(service, currentUserUid = currentUserState.currentUserUid, dailyTableName, cloneFrom =currentDailyTable, null,
                    onCompletion = { dailyTableUid ->
                        // operation after create DailyTable on success
                        mainViewModel.currentDailyTableUid.postValue(dailyTableUid)
                })
        })

        val dailyTableState = dailyTableState(onDelete = { dailyTRC ->
            dailyTableDelete(service, dailyTRC = dailyTRC, onBefore = {
                // change the currentDailyTable to DailyTable.default() before delete.
                mainViewModel.currentDailyTableUid.postValue(DailyTable.default)
            })
        })

        Column {
            CenterBar(true, LocalNav.current.action.upPress,
                extensionArea = {
                    DailyTableExtensionDropDown(
                        dropDownExtensionOpenState = dropDownExtensionOpenState,
                        dailyTableState = dailyTableState,
                        dailyTableCreateState = dailyTableCreateState
                    )
                }) {
                DailyTableTitle(dailyTable = currentDailyTRC.dailyTable, userState = currentUserState) {
                    dropDownTitleOpen = true
                }
                DailyTableDropDown(
                    dailyTableSummaries = dailyTableSummaries,
                    userState = currentUserState,
                    dropDownOpen = dropDownTitleOpen,
                    onDismissRequest = { dropDownTitleOpen = false }) { dailyTable ->
                    // TODO: 2021/4/7 优化代码
                    mainViewModel.currentDailyTableUid.value = dailyTable.uid

                    dropDownTitleOpen = false
                }
            }
            LazyColumn(state = columnState) {
                item {
                    DailyTableContent(dailyTableState = dailyTableState, userState = currentUserState)
                }
            }
        }
        // register dialogs
        DailyTableAddRowDialogCover(dailyTableAddRowState = dailyTableState.addRowState, service = service)
        DailyTableDeleteDialogCover(dailyTableState = dailyTableState)
        DailyTableCreateDialogCover(dailyTableCreateState = dailyTableCreateState, userState = currentUserState)
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
 * @param dailyTableCreateState state of createDialog for DailyTable
 */
@Composable
fun DailyTableExtensionDropDown(dropDownExtensionOpenState: MutableState<Boolean>, dailyTableState: DailyTableState, dailyTableCreateState: DailyTableCreateState) {
    BarExtension(expandedState = dropDownExtensionOpenState) {
        DropdownMenuItem(onClick = {
            // open createDialog for DailyTable
            dailyTableCreateState.dialogOpen.value = true
            // close the dropDown
            dropDownExtensionOpenState.value = false
        }) {
            IconText(imageVector = Icons.Filled.Add , text = stringResource(id = R.string.time_table_add))
        }

        // 当课程表可编辑时，意味着其可以删除。
        if (!dailyTableState.readOnly){
            DropdownMenuItem(onClick = {
                // open deleteDialog for DailyTable
                dailyTableState.deleteState.dialogOpen.value = true
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
fun DailyTableDropDown(dailyTableSummaries: List<DailyTable>, userState: UserState, dropDownOpen: Boolean, onDismissRequest: () -> Unit, onClick: (DailyTable) -> Unit) {
    DropdownMenu(
        modifier = Modifier.width(150.dp),
        offset = DpOffset(x = 0.dp, y = 56.dp), expanded = dropDownOpen, onDismissRequest = onDismissRequest) {
        dailyTableSummaries.forEach { dailyTable ->
            DropdownMenuItem(onClick = { onClick(dailyTable) }) {
                DailyTableTitleDescription(dailyTable = dailyTable, userState = userState, color = LocalPalette.current.textColor)
            }
        }
    }
}

/**
 * DailyTablePage .content
 */
@Composable
fun DailyTableContent(dailyTableState: DailyTableState, userState: UserState) {
    DailyTableTipBox(dailyTable = dailyTableState.dailyTRC.dailyTable, userState = userState)

    ProfileMenuItem(icon = Icons.Filled.Place, title = "预览", next = true, content =
    "点击以进行预览", onClick = LocalNav.current.action.toTimeTablePreview
    )

    dailyTableState.dailyTableRowStateList.forEachIndexed { index, item ->
        DailyRCContent(dailyRC = item.dailyRC, index = index, weekDayStateList = item.weekDays)
    }

    DailyTableAddRowButton(dailyTableState = dailyTableState)
}

/**
 * DailyTablePage .content .button<add_row>
 */
@Composable
fun DailyTableAddRowButton(dailyTableState: DailyTableState) {
    if (!dailyTableState.readOnly) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 4.dp),
            enabled = dailyTableState.canAddRow,
            onClick = { dailyTableState.addRowState.dialogOpen.value = true }) {
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
fun DailyRCContent(dailyRC: DailyRC, index: Int, weekDayStateList: List<WeekDayState>) {
    val dailyRow = dailyRC.dailyRow
    ProfileMenuGroup(title = "组${index + 1}") {
        ProfileMenuItem(title = "适用星期", next = false, content = {
            // TODO: 2021/3/28 完善该功能
            DailyRCContentWeekDay(weekDayStateList = weekDayStateList)
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
fun DailyRCContentWeekDay(weekDayStateList: List<WeekDayState>) {
    // TODO: 2021/4/7 修改样式
    Row(

    ) {
        weekDayStateList.forEachIndexed { index,weekDayState ->
            Spacer(
                modifier = Modifier.width(4.dp)
            )
            Badge(readOnly = true, checked = weekDayState.checked, text = index.toWeekDayString().substring(1)) {

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
                Column(
                ) {
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
fun DailyTableDeleteDialogCover(dailyTableState: DailyTableState) {
    // readOnly的项无法删除
    if (!dailyTableState.readOnly) {
        NanoDialog(title = "删除时间表", dialogState = dailyTableState.deleteState) {
            Text("你确认要删除时间表${dailyTableState.dailyTRC.dailyTable.name}吗?")
            Row(
                modifier = Modifier.padding(vertical = 16.dp),
            ){
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.error
                    ) ,
                    onClick = { dailyTableState.deleteState.onDelete(dailyTableState.dailyTRC) }) {
                    Text("删除")
                }
            }
        }
    }

}

/**
 * DailyTablePage .title d .dialog<create> version = 2
 * @param dailyTableCreateState dialogState for create DailyTable
 * @param userState current userState
 * @param service application service, see also [org.tty.dailyset.provider.LocalServices]
 */
@Composable
fun DailyTableCreateDialogCover(dailyTableCreateState: DailyTableCreateState, userState: UserState) {
    var name by dailyTableCreateState.name
    val dailyTableSummaries by dailyTableCreateState.dailyTableSummaries
    var currentDailyTable by dailyTableCreateState.currentDailyTable
    val isValid by derivedStateOf {
        name.isNotEmpty() && name.length in 2..20
    }

    NanoDialog(title = "创建时间表", dialogState = dailyTableCreateState) {

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
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
        ){
            Spacer(modifier = Modifier.weight(1f))
            Button(
                enabled = isValid,
                onClick = { dailyTableCreateState.onCreate(name, currentDailyTable) }) {
                Text("创建")
            }
        }
    }

}

/**
 * DailyTablePage .content .dialog<add_row> version = 1
 */
@Composable
fun DailyTableAddRowDialogCover(dailyTableAddRowState: DailyTableAddRowState, service: DailySetApplication) {
    NanoDialog(title = "创建组", dialogState = dailyTableAddRowState) {
        Text("hello world")
    }
}