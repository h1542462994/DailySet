package org.tty.dailyset.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.tty.dailyset.model.entity.DailyDuration
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailyDurationCreateState
import org.tty.dailyset.ui.component.ComboBox
import org.tty.dailyset.ui.component.NanoDialog
import org.tty.dailyset.ui.theme.LocalPalette

/**
 * the dailog for create clazz duration and import this to current dailySet ::clazzType.
 */
@Composable
fun ClazzDailySetClazzDurationCreateDialogCover(
    clazzDailyDurationCreateState: ClazzDailyDurationCreateState,
    notIncludedDailyDurations: List<DailyDuration>
) {
    var dialogOpen by clazzDailyDurationCreateState.dialogOpen
    var sourceUid by clazzDailyDurationCreateState.sourceUid
    var name by clazzDailyDurationCreateState.name
    var startDate by clazzDailyDurationCreateState.startDate
    var endDate by clazzDailyDurationCreateState.endDate
    val weekCount by clazzDailyDurationCreateState.weekCount
    var periodCode by clazzDailyDurationCreateState.periodCode

    NanoDialog(title = "添加学期", dialogState = clazzDailyDurationCreateState) {
        Column {
            // sourceUid is the dailyDuration to reference to
            val selections = listOf(DailyDuration.empty()).plus(notIncludedDailyDurations)
            ComboBox(
                title = "引用源",
                data = selections,
                onSelected = {
                    sourceUid = if (it == DailyDuration.empty()) {
                        null
                    } else {
                        it.uid
                    }
                }) {
                Text(
                    text = if (it == DailyDuration.empty()) "(新建)" else it.name
                )
            }
            OutlinedTextField(value = name, label = { Text("学期名称") }, onValueChange = {
                name = it
            })
        }
    }
}