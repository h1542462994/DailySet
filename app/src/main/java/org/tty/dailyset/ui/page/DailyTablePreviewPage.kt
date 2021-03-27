package org.tty.dailyset.ui.page

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.textInputServiceFactory
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.data.scope.*
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.transient.DailyTableCalc
import org.tty.dailyset.ui.component.CenterBar
import org.tty.dailyset.ui.theme.LocalPalette
import org.tty.dailyset.ui.utils.*
import java.time.LocalDate


@Composable
fun DailyTablePreviewPage() {
    val currentDailyTRC by currentDailyTableDetail()
    val currentDailyTable by currentDailyTable()
    @Suppress
    val c: DailyTRC? = currentDailyTRC

    Column {
        CenterBar(
            useBack = true,
            onBackPressed = LocalNav.current.action.upPress,
            content = { DailyTableTitlePreview(currentDailyTable = currentDailyTable) }
        )

        val measuredWidth = measuredWidth()
        // TODO: 2021/3/26 去除硬编码 25.dp
        val unit = toPx(dp = 25.dp)

        if (c != null) {
            val dailyTableCalc = DailyTableCalc(c, measuredWidth, unit)
            Log.d(javaClass.name, dailyTableCalc.toString())
            val palette = LocalPalette.current
            val canvasHeightDp = toDp(px = dailyTableCalc.canvasHeight)

            DailyTablePreviewHeader(dailyTableCalc = dailyTableCalc)


            LazyColumn {
                item {

                    Canvas(
                        modifier = Modifier.size(width = measuredWidthDp(), height = canvasHeightDp)
                    ) {
                        // draw horizontal lines
                        (1 until dailyTableCalc.drawCountHLine).forEach { index ->
                            val (start, end) = dailyTableCalc.offsetsHLine(index)
                            drawLine(color = palette.background2,
                                start = start,
                                end = end, strokeWidth = 2.0f)
                        }
                        // draw vertical lines
                        (0 until dailyTableCalc.cellColumnCount).forEach{ index ->
                            val (start, end) = dailyTableCalc.offsetsVLine(index)
                            drawLine(color = palette.background2,
                                start = start,
                                end = end, strokeWidth = 2.0f)
                        }
                    }
                }
            }
        } else {
            // TODO: 2021/3/26 完成表格预览功能
            Text("hello world")
        }
    }
}

@Composable
fun DailyTablePreviewHeader(dailyTableCalc: DailyTableCalc) {
    BoxWithConstraints {
        val palette = LocalPalette.current
        val canvasHeight = dailyTableCalc.unit * 1.5f

        Canvas(
            modifier = Modifier.size(width = measuredWidthDp(), height = toDp(px = canvasHeight))
        ) {
            drawLine(color = palette.background2, start = Offset(x = 0f, y = canvasHeight), end = Offset(x = dailyTableCalc.measuredWidth, y = canvasHeight), strokeWidth = 2.0f)
        }

        @Composable
        fun createText(start: LocalDate, index: Int, value: String) {
            val dateString = start.plusDays(index.toLong()).toShortDateString()

            return Column(
                modifier = Modifier
                    .size(
                        width = toDp(px = dailyTableCalc.cellWidth),
                        height = toDp(px = canvasHeight)
                    )
                    .absoluteOffset(x = toDp(px = dailyTableCalc.menuWidth + dailyTableCalc.cellWidth * index))
                    .wrapContentSize(align = Alignment.Center),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = value,
                    fontSize = 12.sp,
                    color = LocalPalette.current.textColorDetail
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = dateString,
                    fontSize = 10.sp,
                    color = LocalPalette.current.textColorDetail
                )
            }

        }

        val start = LocalDate.now().toWeekStart()

        (0 until dailyTableCalc.cellColumnCount).forEach{ index ->
            // TODO: 2021/3/27 添加国际化的支持
            createText(start = start, index = index, value = index.toWeekDayString())
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