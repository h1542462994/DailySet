package org.tty.dailyset.ui.page

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.data.scope.currentDailyTable
import org.tty.dailyset.data.scope.currentDailyTableDetail
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.lifetime.DailyTableCalc
import org.tty.dailyset.ui.component.CenterBar
import org.tty.dailyset.ui.theme.LocalPalette
import org.tty.dailyset.ui.utils.*
import java.sql.Time
import java.time.LocalDate




@Composable
fun DailyTablePreviewPage() {
    val currentDailyTRC by currentDailyTableDetail()
    val currentDailyTable by currentDailyTable()
    // TODO: 2021/3/27 消除过于复杂的变量依赖
    @Suppress
    val tempCurrentDailyTRC: DailyTRC? = currentDailyTRC
    val startDate = LocalDate.now().toWeekStart()
    val currentDate = LocalDate.now()
    val indexDiffNow = minus(currentDate, startDate).toInt()
    val (indexDiff, setIndexDiff) = remember {
        mutableStateOf(indexDiffNow)
    }

    Column {
        CenterBar(
            useBack = true,
            onBackPressed = LocalNav.current.action.upPress,
            content = { DailyTableTitlePreview(currentDailyTable = currentDailyTable) }
        )

        val measuredWidth = measuredWidth()
        // TODO: 2021/3/26 去除硬编码 25.dp
        val unit = toPx(dp = 25.dp)

        if (tempCurrentDailyTRC != null) {
            val dailyTableCalc = DailyTableCalc(tempCurrentDailyTRC, measuredWidth, unit)
            //Log.d(javaClass.name, dailyTableCalc.toString())
            val palette = LocalPalette.current
            val canvasHeightDp = toDp(px = dailyTableCalc.canvasHeight)

            DailyTablePreviewHeader(dailyTableCalc = dailyTableCalc, startDate = startDate, indexDiffNow = indexDiffNow, indexDiff = indexDiff, setIndexDiff = setIndexDiff)


            LazyColumn {
                item {
                    BoxWithConstraints {
                        Canvas(
                            modifier = Modifier.size(width = measuredWidthDp(), height = canvasHeightDp)
                        ) {
                            // draw vertical block
                            val (topLeft, size) = dailyTableCalc.offsetAndSizeBlock(indexDiff)
                            drawRect(color = palette.backgroundColor, topLeft = topLeft, size = size)
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
                        //val palette = LocalPalette.current

                        @Composable
                        fun createText(index: Int, start: Time, end: Time) {

                            val (topLeft, size) = dailyTableCalc.offsetsAndSizeBlockMenu(index)
                            Column(
                                modifier = Modifier
                                    .size(toDp(size.width), toDp(size.height))
                                    .absoluteOffset(toDp(topLeft.x), toDp(topLeft.y))
                                    .wrapContentSize(align = Alignment.Center)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .align(alignment = Alignment.CenterHorizontally)
                                        .padding(bottom = 4.dp),
                                    text = "${index + 1}",
                                    fontSize = 14.sp,
                                    color = palette.textColorDetail,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                                    text = start.toShortString(),
                                    fontSize = 12.sp,
                                    color = palette.textColorDetail,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                                    text = end.toShortString(),
                                    fontSize = 12.sp,
                                    color = palette.textColorDetail,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                        }

                        val currentDailyRC = tempCurrentDailyTRC.dailyRCs.find { it.dailyRow.weekdays.contains(indexDiff + 1) }

                        if (currentDailyRC != null) {
                            // FIXME: 2021/3/27 这将会在节数不一致时产生bug！
                            (0 until dailyTableCalc.counts.sum()).forEach { index ->
                                val currentRow = currentDailyRC.dailyCells[index]
                                createText(index = index, start = currentRow.start, end = currentRow.end)
                            }
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
fun DailyTablePreviewHeader(dailyTableCalc: DailyTableCalc, startDate: LocalDate, indexDiffNow: Int, indexDiff: Int, setIndexDiff: (Int) -> Unit) {
    BoxWithConstraints {
        val palette = LocalPalette.current
        val canvasHeight = dailyTableCalc.canvasHeightHeader

        Canvas(
            modifier = Modifier.size(width = measuredWidthDp(), height = toDp(px = canvasHeight))
        ) {
            val (topLeft, size) = dailyTableCalc.offsetAndSizeBlockHeader(indexDiff)
            drawRect(color = palette.backgroundColor, topLeft = topLeft, size = size)
            drawLine(color = palette.background2, start = Offset(x = 0f, y = canvasHeight), end = Offset(x = dailyTableCalc.measuredWidth, y = canvasHeight), strokeWidth = 2.0f)
        }

        @Composable
        fun createText(start: LocalDate, index: Int, value: String, style: Int = 0) {
            val dateString = start.plusDays(index.toLong()).toShortDateString()
            val color = if (style == 0) LocalPalette.current.textColorDetail else LocalPalette.current.textColorTitle
            val fontWeight = if (style == 0) FontWeight.Normal else FontWeight.Bold

            return Column(
                modifier = Modifier
                    .size(
                        width = toDp(px = dailyTableCalc.cellWidth),
                        height = toDp(px = canvasHeight)
                    )
                    .absoluteOffset(x = toDp(px = dailyTableCalc.menuWidth + dailyTableCalc.cellWidth * index))
                    .clickable { setIndexDiff(index) }
                    .wrapContentSize(align = Alignment.Center),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = value,
                    fontSize = 12.sp,
                    color = color,
                    fontWeight = fontWeight
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = dateString,
                    fontSize = 10.sp,
                    color = color,
                    fontWeight = fontWeight
                )
            }

        }

        //val start = LocalDate.now().toWeekStart()

        (0 until dailyTableCalc.cellColumnCount).forEach{ index ->
            // TODO: 2021/3/27 添加国际化的支持
            val style = if (index == indexDiffNow) 1 else 0
            createText(start = startDate, index = index, value = index.toWeekDayString(), style = style)
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