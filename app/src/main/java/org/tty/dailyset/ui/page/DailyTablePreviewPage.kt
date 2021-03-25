package org.tty.dailyset.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.data.scope.currentDailyTable
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.ui.component.CenterBar
import org.tty.dailyset.ui.theme.LocalPalette

@Composable
fun DailyTablePreviewPage() {
    val currentDailyTable by currentDailyTable()

    Column {
        CenterBar(
            useBack = true,
            onBackPressed = LocalNav.current.action.upPress,
            content = { DailyTableTitlePreview(currentDailyTable = currentDailyTable) }
        )

        //TODO("完善表格预览功能")
        Text("hello world")
    }
}

@Composable
fun DailyTableTitlePreview(currentDailyTable: DailyTable) {
    Column(
        modifier = Modifier
            .wrapContentSize(align = Alignment.Center)
    ) {
        Text(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .wrapContentSize(align = Alignment.Center),
            text = stringResource(id = R.string.time_table_preview),
            fontSize = 18.sp)

        Text(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            text = currentDailyTable.name, color = LocalPalette.current.textColorDetail)

    }
}