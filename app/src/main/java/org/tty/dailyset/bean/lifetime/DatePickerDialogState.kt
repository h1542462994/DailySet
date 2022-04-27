package org.tty.dailyset.bean.lifetime

import androidx.compose.runtime.MutableState
import java.time.LocalDate

class DatePickerDialogState(
    override val dialogOpen: MutableState<Boolean>,
    val date: MutableState<LocalDate>,
    val minDate: MutableState<LocalDate?>,
    val maxDate: MutableState<LocalDate?>
) : DialogState(dialogOpen)