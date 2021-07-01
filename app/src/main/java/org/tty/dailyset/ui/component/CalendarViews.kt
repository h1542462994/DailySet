package org.tty.dailyset.ui.component

import android.util.AttributeSet
import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.tty.dailyset.model.lifetime.DatePickerDialogState
import org.tty.dailyset.model.lifetime.DialogState
import org.tty.dailyset.toEpochMilli
import org.tty.dailyset.ui.theme.LocalPalette
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.jar.Attributes

@Composable
fun DatePicker(onDateSelected: (LocalDate) -> Unit, onDismissRequest: () -> Unit) {
    val selDate = remember { mutableStateOf(LocalDate.now()) }

    //todo - add strings to resource after POC
    Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(size = 16.dp)
                )
        ) {
            Column(
                Modifier
                    .defaultMinSize(minHeight = 72.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select date".toUpperCase(Locale.ENGLISH),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onPrimary
                )

                Spacer(modifier = Modifier.size(24.dp))

                Text(
                    text = selDate.value.format(DateTimeFormatter.ofPattern("MMM d, YYYY")),
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.onPrimary
                )

                Spacer(modifier = Modifier.size(16.dp))
            }

            CustomCalendarView(onDateSelected = {
                selDate.value = it
            }, initDate = LocalDate.now())

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 16.dp, end = 16.dp)
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    //TODO - hardcode string
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.onPrimary
                    )
                }

                TextButton(
                    onClick = {
                        onDateSelected(selDate.value)
                        onDismissRequest()
                    }
                ) {
                    //TODO - hardcode string
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.onPrimary
                    )
                }

            }
        }
    }
}

/**
 * the date picker
 */
@Composable
fun DatePicker(datePickerDialogState: DatePickerDialogState, onDateSelected: (LocalDate) -> Unit) {
    var date by datePickerDialogState.date
    val minDate by datePickerDialogState.minDate
    val maxDate by datePickerDialogState.maxDate
    var dialogOpen by datePickerDialogState.dialogOpen

    NanoDialog(title = "选择日期", dialogState = datePickerDialogState, autoClose = false) {


        CustomCalendarView(initDate = date, minDate = minDate, maxDate = maxDate) {
            date = it
        }

        Row(
            modifier = Modifier.padding(vertical = 16.dp),
        ){
            Spacer(modifier = Modifier.weight(1f))
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = LocalPalette.current.lineColor,
                    disabledBackgroundColor = LocalPalette.current.backgroundInvalid,
                    disabledContentColor = LocalPalette.current.primary
                ),
                enabled = true,
                onClick = { dialogOpen = false }) {
                Text("取消")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = LocalPalette.current.primaryColor,
                    disabledBackgroundColor = LocalPalette.current.backgroundInvalid,
                    disabledContentColor = LocalPalette.current.primary
                ),
                enabled = true,
                onClick = {
                    onDateSelected(date)
                    dialogOpen = false
                }) {
                Text("确认")
            }
        }
    }
}

@Composable
fun CustomCalendarView(initDate: LocalDate, minDate: LocalDate? = null, maxDate: LocalDate? = null, onDateSelected: (LocalDate) -> Unit) {
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = { context ->
            CalendarView(context)
        },
        update = { view ->
            view.firstDayOfWeek = Calendar.MONDAY
            view.date = initDate.toEpochMilli()
            if (minDate != null) {
                view.minDate = minDate.toEpochMilli()
            }
            if (maxDate != null) {
                view.maxDate = maxDate.toEpochMilli()
            }
            // view.minDate = ?
            // view.maxDate = ?
            view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                onDateSelected(
                    LocalDate.of(year, month + 1, dayOfMonth)
                )
            }
        }
    )
}