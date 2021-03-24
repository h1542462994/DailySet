package org.tty.dailyset.ui.page

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.data.scope.*
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.ui.component.CenterBar
import org.tty.dailyset.ui.theme.LocalPalette

@Composable
fun DailyTablePage() {
    //calculate
    val dailyTableSummaries by dailyTableSummaries()
    val currentDailyTable by currentDailyTable()
    var dialogOpen by remember { mutableStateOf(false) }

    if (dialogOpen) {

    }

    Column {
        CenterBar(true, LocalNav.current.action.upPress) {
            DailyTableTitle(currentDailyTable = currentDailyTable) {
                Log.d("UI","dialogOpen:${dialogOpen}")
                dialogOpen = true
                Log.d("UI","dialogOpen:${dialogOpen}")
            }
        }

        Text(text = "TimeTable")
    }
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

@Composable
fun DailyTableSelector() {

}

@Preview
@Composable
fun DailyTableTitlePreview() {
    DailyTableTitle(currentDailyTable = DailyTable.default())
}



