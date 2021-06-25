package org.tty.dailyset.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailyDurationCreateState
import org.tty.dailyset.ui.component.NanoDialog

/**
 * the dailog for create clazz duration and import this to current dailySet ::clazzType.
 */
@Composable
fun ClazzDailySetClazzDurationCreateDialogCover(
    clazzDailyDurationCreateState: ClazzDailyDurationCreateState
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

        }
    }
}