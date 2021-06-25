package org.tty.dailyset.model.lifetime.dailyset

import androidx.compose.runtime.MutableState
import org.tty.dailyset.model.entity.PeriodCode
import org.tty.dailyset.model.lifetime.DialogState
import java.time.LocalDate

/**
 * @param sourceUid 引用源的uid,如果为null,表示新建,否则表示导入已有的源.
 *
 */
class ClazzDailyDurationCreateState(
    override val dialogOpen: MutableState<Boolean>,
    val sourceUid: MutableState<String?>,
    val startDate: MutableState<LocalDate?>,
    val endDate: MutableState<LocalDate?>,
    val weekCount: MutableState<Int?>,
    val name: MutableState<String>,
    val periodCode: MutableState<PeriodCode>
): DialogState(dialogOpen) {

}