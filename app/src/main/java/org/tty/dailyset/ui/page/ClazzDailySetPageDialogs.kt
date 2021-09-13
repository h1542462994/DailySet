package org.tty.dailyset.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import org.tty.dailyset.data.processor.ClazzDailySetProcessor
import org.tty.dailyset.data.scope.DataScope
import org.tty.dailyset.minus
import org.tty.dailyset.model.entity.DailyDuration
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.entity.PeriodCode
import org.tty.dailyset.model.entity.toDisplay
import org.tty.dailyset.model.lifetime.UserState
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailyDurationCreateState
import org.tty.dailyset.toLongDateString
import org.tty.dailyset.ui.component.*
import org.tty.dailyset.ui.theme.LocalPalette
import org.tty.dailyset.weekCount

/**
 * the dailog for create clazz duration and import this to current dailySet ::clazzType.
 */
@Composable
fun ClazzDailySetClazzDurationCreateDialogCover(
    clazzDailyDurationCreateState: ClazzDailyDurationCreateState,
    notIncludedDailyDurations: List<DailyDuration>,
    dailyTableSummaries: List<DailyTable>,
    userState: UserState,
    clazzDailySetProcessor: ClazzDailySetProcessor
) {
    var dialogOpen by clazzDailyDurationCreateState.dialogOpen
    var sourceUid by clazzDailyDurationCreateState.sourceUid
    var name by clazzDailyDurationCreateState.name
    var startDate by clazzDailyDurationCreateState.startDate
    var endDate by clazzDailyDurationCreateState.endDate
    /**
     * 开始和结束相隔的天数
     */
    val gap = minus(endDate, startDate)
    var weekCount by clazzDailyDurationCreateState.weekCount
    var periodCode by clazzDailyDurationCreateState.periodCode
    var dailyTableUid by clazzDailyDurationCreateState.bindingDailyTableUid

    fun update() {
        if (sourceUid != null) {
            val duration = notIncludedDailyDurations.find { it.uid == sourceUid }!!
            name = duration.name
            startDate = duration.startDate
            endDate = duration.endDate
            weekCount = weekCount(endDate, startDate).toInt()
            periodCode = PeriodCode.values().find { it.code == duration.bindingPeriodCode }!!
        }
    }

//    LaunchedEffect(key1 = sourceUid, block =  {
//
//    })

    with(DataScope) {


        val startDatePickerDialogState = datePickerDialogState(
            date = startDate
        )
        val endDatePickerDialogState = datePickerDialogState(
            date = endDate,
            minDate = startDate
        )

        LaunchedEffect(key1 = startDate, block = {
            startDatePickerDialogState.date.value = startDate
        })

        LaunchedEffect(key1 = listOf(startDate, endDate), block = {
            endDatePickerDialogState.date.value = endDate
            endDatePickerDialogState.minDate.value = startDate
        })

        NanoDialog(title = "添加学期", dialogState = clazzDailyDurationCreateState) {
            Column {
                // sourceUid is the dailyDuration to reference to
                val selections = listOf(DailyDuration.empty()).plus(notIncludedDailyDurations)
                val source = if (sourceUid == null) DailyDuration.empty() else {
                    selections.find { it.uid == sourceUid }
                } ?: DailyDuration.empty()
                val periodCodes = PeriodCode.values().toList()
                val dailyTables = dailyTableSummaries
                val dailyTable = dailyTableSummaries.find {
                    it.uid == dailyTableUid
                }!!
                ComboBox(
                    title = "引用源",
                    data = selections,
                    defaultValue = source,
                    onSelected = {
                        sourceUid = if (it == DailyDuration.empty()) {
                            null
                        } else {
                            it.uid
                        }
                        update()
                    }) {
                    Text(
                        text = if (it == DailyDuration.empty()) "(新建)" else it.name
                    )
                }
                OutlinedTextField(value = name, label = { Text("学期名称") }, onValueChange = {
                    name = it
                })
                SelectButton(title = "开始日期", content = startDate.toLongDateString()) {
                    startDatePickerDialogState.dialogOpen.value = true
                }
                SelectButton(title = "结束日期", content = endDate.toLongDateString()) {
                    endDatePickerDialogState.dialogOpen.value = true
                }
                SelectButton(title = "周数", content = weekCount.toString()) {

                }
                ComboBox(title = "阶段", data = periodCodes, defaultValue = periodCode, onSelected = {
                    periodCode = it
                }) {
                    Text(
                        text = it.toDisplay()
                    )
                }
                ComboBox(title = "绑定时间表", data = dailyTableSummaries, onSelected = { dailyTableUid = it.uid }) {
                    DailyTableTitleDescription(
                        dailyTable = it,
                        userState = userState,
                        color = LocalPalette.current.primary
                    )
                }
            }

            // TODO: 2021/7/1 添加时间是否冲突的判断
            val enabled = name.length in 2..20


            NanoDialogButton(text = "添加", enabled = enabled) {
                clazzDailySetProcessor.onCreateClazzDurationAndBinding(
                    name = name,
                    uid = sourceUid,
                    startDate = startDate,
                    endDate = endDate,
                    periodCode = periodCode,
                    bindingDailyTableUid = dailyTableUid
                )
            }
        }



        DatePicker(datePickerDialogState = startDatePickerDialogState) {
            val g = gap
            startDate = it
            endDate = startDate.plusDays(g)
        }
        DatePicker(datePickerDialogState = endDatePickerDialogState) {
            endDate = it
            weekCount = weekCount(endDate, startDate).toInt()
        }
    }




}