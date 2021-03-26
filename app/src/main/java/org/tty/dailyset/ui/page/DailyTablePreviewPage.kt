package org.tty.dailyset.ui.page

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.data.scope.*
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.transient.DailyTableCalc
import org.tty.dailyset.ui.component.CenterBar
import org.tty.dailyset.ui.theme.LocalPalette
import org.tty.dailyset.ui.utils.*


@Composable
fun DailyTablePreviewPage() {
    val currentDailyTRC by currentDailyTableDetail()
    val currentDailyTable by currentDailyTable()
    @Suppress
    val c = currentDailyTRC

    Column {
        CenterBar(
            useBack = true,
            onBackPressed = LocalNav.current.action.upPress,
            content = { DailyTableTitlePreview(currentDailyTable = currentDailyTable) }
        )


        val measuredWidth = measuredWidth()
        val unit = toPx(dp = 25.dp)

        if (c != null) {
            val dailyTableCalc = DailyTableCalc(c, measuredWidth, unit)
            Log.d(javaClass.name, dailyTableCalc.toString())
            val palette = LocalPalette.current
            val canvasHeightDp = toDp(px = dailyTableCalc.canvasHeight)

            LazyColumn {
                item {
                    Canvas(
                        modifier = Modifier.size(width = measuredWidthDp(),height = canvasHeightDp)) {
                        // draw horizontal lines
                        (0 until dailyTableCalc.drawCountHLine).forEach { index ->
                            drawLine(color = palette.background2,
                                start = Offset(x = 0f, y = dailyTableCalc.offsetYHLine(index)),
                                end = Offset(x = measuredWidth, y = dailyTableCalc.offsetYHLine(index)))
                        }
                    }
                }
            }
        } else {

            // FIXME: 2021/3/26 完成表格预览功能
            Text("hello world")
        }
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