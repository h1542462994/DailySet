package org.tty.dailyset.ui.page

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tty.dailyset.R
import org.tty.dailyset.bean.entity.DailySetCell
import org.tty.dailyset.bean.entity.DailySetTable
import org.tty.dailyset.bean.lifetime.DailySetCourseCoverage
import org.tty.dailyset.bean.lifetime.DailySetRC
import org.tty.dailyset.bean.lifetime.DailyTableCalc
import org.tty.dailyset.common.datetime.DateSpan
import org.tty.dailyset.common.datetime.indexTo
import org.tty.dailyset.common.datetime.toShortString
import org.tty.dailyset.common.util.toIntArray
import org.tty.dailyset.component.common.measuredWidthDp
import org.tty.dailyset.component.common.toDp
import org.tty.dailyset.toShortDateString
import org.tty.dailyset.toWeekDayString
import org.tty.dailyset.ui.theme.DailySetTheme
import org.tty.dailyset.ui.theme.LocalPalette
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * DailyTablePreviewPage .header
 */
@Composable
fun DailyTablePreviewHeader(dailyTableCalc: DailyTableCalc, dataSpan: DateSpan) {
    BoxWithConstraints {
        val palette = LocalPalette.current
        val canvasHeight = dailyTableCalc.canvasHeightHeader
        val canvasWidthDp = measuredWidthDp()
        assert(canvasWidthDp == toDp(dailyTableCalc.measuredWidth))
        val canvasHeightDp = toDp(canvasHeight)
        val nowDate = LocalDate.now()
        val nowWeekDay = nowDate.dayOfWeek
        var currentWeekDay = DayOfWeek.MONDAY
        val startWeekDay = DayOfWeek.MONDAY
        val currentIndex = currentWeekDay.indexTo(startWeekDay)


        Canvas(
            modifier = Modifier.size(width = canvasWidthDp, height = canvasHeightDp)
        ) {
            // draw rectangle of cursor.
            val (topLeft, size) = dailyTableCalc.offsetAndSizeBlockHeader(currentIndex)
            drawRect(color = palette.backgroundColor, topLeft = topLeft, size = size)
            // draw bottom line.
            drawLine(color = palette.lineColor, start = Offset(x = 0f, y = canvasHeight), end = Offset(x = dailyTableCalc.measuredWidth, y = canvasHeight), strokeWidth = 2.0f)
        }

        /**
         * inline function, to createText of weekDay.
         */
        @Composable
        fun createTextWeekDay(start: LocalDate, index: Int, value: String, style: Int = 0) {
            val dateString = start.plusDays(index.toLong()).toShortDateString()
            val color = if (style == 0) LocalPalette.current.sub else LocalPalette.current.primaryColor
            val color2 = if (style == 0) LocalPalette.current.primary else LocalPalette.current.primaryColor
            val fontWeight = if (style == 0) FontWeight.Normal else FontWeight.Bold

            return Column(
                modifier = Modifier
                    .size(
                        width = toDp(px = dailyTableCalc.cellWidth),
                        height = toDp(px = canvasHeight)
                    )
                    .absoluteOffset(x = toDp(px = dailyTableCalc.menuWidth + dailyTableCalc.cellWidth * index))
                    .clickable {
                        currentWeekDay = startWeekDay + index.toLong()
                    }
                    .wrapContentSize(align = Alignment.Center),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = value,
                    fontSize = 14.sp,
                    color = color,
                    fontWeight = fontWeight
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = dateString,
                    fontSize = 12.sp,
                    color = color2,
                    fontWeight = fontWeight
                )
            }

        }

        //val start = LocalDate.now().toWeekStart()

        (0 until dailyTableCalc.cellColumnCount).forEach{ index ->
            createTextWeekDay(start = dataSpan.startDate, index = index, value = index.toWeekDayString(), style = 0)
        }
    }
}


/**
 * DailyTablePreviewPage .body
 */
@Composable
fun DailyTablePreviewBody(dailyTableCalc: DailyTableCalc, dailySetCourseCoverage: DailySetCourseCoverage) {
    val palette = LocalPalette.current
    val canvasHeight = dailyTableCalc.canvasHeightBody
    val canvasWidthDp = measuredWidthDp()
    assert(canvasWidthDp == toDp(dailyTableCalc.measuredWidth))
    val canvasHeightDp = toDp(canvasHeight)
    val currentWeekDay = DayOfWeek.MONDAY
    val startWeekDay = DayOfWeek.MONDAY
    val currentIndex by derivedStateOf {
        currentWeekDay.indexTo(startWeekDay)
    }
    //val nowIndex= dailyTablePreviewState.weekDayNow - 1
    val courseBackgrounds = DailySetTheme.courseColors.backgrounds

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
                        drawLine(color = palette.lineColor,
                            start = start,
                            end = end, strokeWidth = 2.0f)
                    }
                    // draw vertical lines
                    (0 until dailyTableCalc.cellColumnCount).forEach{ index ->
                        val (start, end) = dailyTableCalc.offsetsVLine(index)
                        drawLine(color = palette.lineColor,
                            start = start,
                            end = end, strokeWidth = 2.0f)
                    }

                    for (courseCell in dailySetCourseCoverage.coverageData) {
                        val (topLeftCourse, sizeCourse) = dailyTableCalc.offsetAndSizeBlockCourseCell(courseCell.dayOfWeek, courseCell.section)
                        val backgroundColor = dailyTableCalc.calcColorOfCourse(
                            dailySetCourse = courseCell.courses[0],
                            colors = courseBackgrounds
                        )
                        drawRect(
                            color = backgroundColor,
                            topLeft = topLeftCourse,
                            size = sizeCourse
                        )
                    }


                }
                //val palette = LocalPalette.current

                /**
                 * inline function, createText of time duration.
                 */
                @Composable
                fun createTextTimeDuration(dailySetRC: DailySetRC, dailySetCell: DailySetCell) {
                    val currentIndexOfDailyCell = dailyTableCalc.calcCurrentIndexOfDailyCell(dailySetRC, dailySetCell)
                    val (topLeft, size) = dailyTableCalc.offsetsAndSizeBlockMenu(dailySetCell)
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
                            color = palette.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                            text = dailySetCell.startTime.toShortString(),
                            fontSize = 12.sp,
                            color = palette.sub,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                            text = dailySetCell.endTime.toShortString(),
                            fontSize = 12.sp,
                            color = palette.sub,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }

                val currentDailyRC = dailyTableCalc.dailySetTRC.dailySetRCs.find { it.dailySetRow.weekdays.toIntArray().contains(currentWeekDay.value) }

                @Suppress
                when {
                    currentDailyRC != null -> {
                        currentDailyRC.dailySetCells.forEach { dailyCell ->
                            createTextTimeDuration(dailySetRC = currentDailyRC, dailySetCell = dailyCell)
                        }
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
fun DailyTablePreviewTitle(currentDailyTable: DailySetTable) {
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
            text = currentDailyTable.name, color = DailySetTheme.color.sub)

    }
}