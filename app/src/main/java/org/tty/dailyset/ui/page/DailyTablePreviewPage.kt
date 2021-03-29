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
import org.tty.dailyset.data.scope.*
import org.tty.dailyset.model.entity.DailyCell
import org.tty.dailyset.model.entity.DailyRC
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.lifetime.DailyTableCalc
import org.tty.dailyset.ui.component.CenterBar
import org.tty.dailyset.ui.theme.LocalPalette
import org.tty.dailyset.ui.utils.*
import java.sql.Time
import java.time.LocalDate


/**
 * DailyTablePreviewPage
 */
@Composable
fun DailyTablePreviewPage() {
    val currentDailyTRC by currentDailyTableDetail()
    val currentDailyTable by currentDailyTable()
    // TODO: 2021/3/26 去除硬编码 25.dp
    val unit = toPx(dp = 25.dp)
    val currentUserState by currentUserState()
    // complex state
    val dailyTablePreviewState = dailyTablePreviewState()

    @Suppress
    val tempCurrentDailyTRC: DailyTRC? = currentDailyTRC

    // TODO: 2021/3/27 消除过于复杂的变量依赖
    val startDate = LocalDate.now().toWeekStart()
    val currentDate = LocalDate.now()
    val indexDiffNow = minus(currentDate, startDate).toInt()
    val (indexDiff, setIndexDiff) = remember {
        mutableStateOf(indexDiffNow)
    }


    val measuredWidth = measuredWidth()
    assert(measuredWidth > 0)

    Column {
        // must provide a placeholder, otherwise measuredWidth will return not correctly.
        CenterBar(
            useBack = true,
            onBackPressed = LocalNav.current.action.upPress,
            content = { DailyTableTitle(dailyTable = currentDailyTable, userState = currentUserState, isPreviewPage = true) }
        )


        if (tempCurrentDailyTRC != null) {
            val dailyTableCalc = DailyTableCalc(tempCurrentDailyTRC, measuredWidth, unit)

            DailyTablePreviewHeader(dailyTableCalc = dailyTableCalc, dailyTablePreviewState = dailyTablePreviewState)
            DailyTablePreviewBody(dailyTableCalc = dailyTableCalc, dailyTablePreviewState = dailyTablePreviewState)
        }
    }
}

/**
 * DailyTablePreviewPage .header
 */
@Composable
fun DailyTablePreviewHeader(dailyTableCalc: DailyTableCalc, dailyTablePreviewState: DailyTablePreviewState) {
    BoxWithConstraints {
        val palette = LocalPalette.current
        val canvasHeight = dailyTableCalc.canvasHeightHeader
        val canvasWidthDp = measuredWidthDp()
        assert(canvasWidthDp == toDp(dailyTableCalc.measuredWidth))
        val canvasHeightDp = toDp(canvasHeight)
        val currentIndex = dailyTablePreviewState.weekDayCurrent - 1
        val nowIndex= dailyTablePreviewState.weekDayNow - 1

        Canvas(
            modifier = Modifier.size(width = canvasWidthDp, height = canvasHeightDp)
        ) {
            // draw rectangle of cursor.
            val (topLeft, size) = dailyTableCalc.offsetAndSizeBlockHeader(currentIndex)
            drawRect(color = palette.backgroundColor, topLeft = topLeft, size = size)
            // draw bottom line.
            drawLine(color = palette.background2, start = Offset(x = 0f, y = canvasHeight), end = Offset(x = dailyTableCalc.measuredWidth, y = canvasHeight), strokeWidth = 2.0f)
        }

        /**
         * inline function, to createText of weekDay.
         */
        @Composable
        fun createTextWeekDay(start: LocalDate, index: Int, value: String, style: Int = 0) {
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
                    .clickable {
                        dailyTablePreviewState.setWeekDayCurrent(index + 1)
                        assert(dailyTablePreviewState.weekDayCurrent == index + 1)
                    }
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
            val style = if (index == nowIndex) 1 else 0
            createTextWeekDay(start = dailyTablePreviewState.startDate, index = index, value = index.toWeekDayString(), style = style)
        }
    }
}

@Composable
fun DailyTablePreviewBody(dailyTableCalc: DailyTableCalc, dailyTablePreviewState: DailyTablePreviewState) {
    val palette = LocalPalette.current
    val canvasHeight = dailyTableCalc.canvasHeightBody
    val canvasWidthDp = measuredWidthDp()
    assert(canvasWidthDp == toDp(dailyTableCalc.measuredWidth))
    val canvasHeightDp = toDp(canvasHeight)
    val currentIndex = dailyTablePreviewState.weekDayCurrent - 1
    //val nowIndex= dailyTablePreviewState.weekDayNow - 1

    LazyColumn {
        item {
            BoxWithConstraints {
                Canvas(
                    modifier = Modifier.size(width = canvasWidthDp, height = canvasHeightDp)
                ) {
                    // draw rectangle of cursor.
                    val (topLeft, size) = dailyTableCalc.offsetAndSizeBlock(currentIndex)
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

                /**
                 * inline function, createText of time duration.
                 */
                @Composable
                fun createTextTimeDuration(dailyRC: DailyRC, dailyCell: DailyCell) {
                    val currentIndexOfDailyCell = dailyTableCalc.calcCurrentIndexOfDailyCell(dailyRC, dailyCell)
                    val (topLeft, size) = dailyTableCalc.offsetsAndSizeBlockMenu(dailyCell)
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
                            text = "${currentIndexOfDailyCell + 1}",
                            fontSize = 14.sp,
                            color = palette.textColorDetail,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                            text = dailyCell.start.toShortString(),
                            fontSize = 12.sp,
                            color = palette.textColorDetail,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                            text = dailyCell.end.toShortString(),
                            fontSize = 12.sp,
                            color = palette.textColorDetail,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }

                val currentDailyRC = dailyTableCalc.dailyTRC.dailyRCs.find { it.dailyRow.weekdays.contains(dailyTablePreviewState.weekDayCurrent) }

                @Suppress
                if (currentDailyRC != null) {
                    currentDailyRC.dailyCells.forEach { dailyCell ->
                        createTextTimeDuration(dailyRC = currentDailyRC, dailyCell = dailyCell)
                    }
                }

            }

        }
    }
}

/**
 * DailyTablePreviewPage .title
 */
@Composable
@Deprecated("use DailyTableTitle instead.", level = DeprecationLevel.WARNING)
fun DailyTablePreviewTitle(currentDailyTable: DailyTable) {
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