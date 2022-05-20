package org.tty.dailyset.ui.page

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tty.dailyset.R
import org.tty.dailyset.bean.entity.DailySetCell
import org.tty.dailyset.bean.entity.DailySetTable
import org.tty.dailyset.bean.lifetime.DailySetCourseCell
import org.tty.dailyset.bean.lifetime.DailySetCourseCoverage
import org.tty.dailyset.bean.lifetime.DailySetRC
import org.tty.dailyset.bean.lifetime.DailyTableCalc
import org.tty.dailyset.common.datetime.DateSpan
import org.tty.dailyset.common.datetime.indexTo
import org.tty.dailyset.common.datetime.toShortString
import org.tty.dailyset.common.util.toIntArray
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
fun DailyTablePreviewHeader(
    dailyTableCalc: DailyTableCalc,
    dateSpan: DateSpan?,
    nowDate: LocalDate,
    currentDayOfWeekState: MutableState<DayOfWeek>
) {
    BoxWithConstraints {
        val palette = LocalPalette.current
        val canvasHeight = dailyTableCalc.canvasHeightHeader
        val canvasWidthDp = toDp(dailyTableCalc.measuredWidth)
        val canvasHeightDp = toDp(canvasHeight)
        var currentDayOfWeek by currentDayOfWeekState
        // TODO: dayOfWeek start change support.
        val startDayOfWeek = DayOfWeek.MONDAY
        val currentIndex = currentDayOfWeek.indexTo(startDayOfWeek)


        Canvas(
            modifier = Modifier.size(width = canvasWidthDp, height = canvasHeightDp)
        ) {
            // draw rectangle of cursor.
            val (topLeft, size) = dailyTableCalc.offsetAndSizeBlockHeader(currentIndex)
            drawRect(color = palette.backgroundColor, topLeft = topLeft, size = size)
            // draw bottom line.
            drawLine(
                color = palette.lineColor,
                start = Offset(x = 0f, y = canvasHeight),
                end = Offset(x = dailyTableCalc.measuredWidth, y = canvasHeight),
                strokeWidth = 2.0f
            )
        }

        /**
         * inline function, to createText of weekDay.
         */
        @Composable
        fun createTextWeekDay(start: LocalDate?, index: Int, value: String, strong: Boolean) {

            val color1 = if (strong) DailySetTheme.color.primaryColor else DailySetTheme.color.sub
            val color2 = if (strong) DailySetTheme.color.primaryColor else DailySetTheme.color.primary
            val fontWeight = if (strong) FontWeight.Bold else FontWeight.Medium

            return Column(
                modifier = Modifier
                    .size(
                        width = toDp(px = dailyTableCalc.cellWidth),
                        height = toDp(px = canvasHeight)
                    )
                    .absoluteOffset(x = toDp(px = dailyTableCalc.menuWidth + dailyTableCalc.cellWidth * index))
                    .clickable {
                        currentDayOfWeek = startDayOfWeek + index.toLong()
                    }
                    .wrapContentSize(align = Alignment.Center),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = value,
                    fontSize = 14.sp,
                    color = color1,
                    fontWeight = fontWeight
                )

                if (start != null) {
                    val dateString = start.plusDays(index.toLong()).toShortDateString()
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = dateString,
                        fontSize = 12.sp,
                        color = color2,
                        fontWeight = fontWeight
                    )
                }
            }

        }

        @Composable
        fun createYearText(start: LocalDate) {
            Column(
                modifier = Modifier
                    .size(
                        toDp(px = dailyTableCalc.menuWidth),
                        toDp(px = dailyTableCalc.canvasHeightHeader)
                    )
                    .wrapContentSize(align = Alignment.Center)
            ) {
                Text(
                    text = start.year.toString(),
                    color = DailySetTheme.color.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        (0 until dailyTableCalc.cellColumnCount).forEach { index ->
            val strong = dateSpan != null && dateSpan.startDate.plusDays(index.toLong()) == nowDate

            createTextWeekDay(
                start = dateSpan?.startDate,
                index = index,
                value = index.toWeekDayString(),
                strong = strong
            )
        }

        if (dateSpan != null) {
            createYearText(start = dateSpan.startDate)
        }
    }
}


/**
 * DailyTablePreviewPage .body
 */
@Composable
fun DailyTablePreviewBody(
    dailyTableCalc: DailyTableCalc,
    dailySetCourseCoverage: DailySetCourseCoverage,
    currentDayOfWeek: DayOfWeek
) {
    val palette = LocalPalette.current
    val canvasHeight = dailyTableCalc.canvasHeightBody
    val canvasWidthDp = toDp(dailyTableCalc.measuredWidth)
    val canvasHeightDp = toDp(canvasHeight)
    // TODO: dayOfWeek start change support.
    val startDayOfWeek = DayOfWeek.MONDAY
    val currentIndex by derivedStateOf {
        currentDayOfWeek.indexTo(startDayOfWeek)
    }
    //val nowIndex= dailyTablePreviewState.weekDayNow - 1
    val courseBackgrounds = DailySetTheme.courseColors.backgrounds
    val courseForegrounds = DailySetTheme.courseColors.foregrounds

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
                        drawLine(
                            color = palette.lineColor,
                            start = start,
                            end = end, strokeWidth = 2.0f
                        )
                    }
                    // draw vertical lines
                    (0 until dailyTableCalc.cellColumnCount).forEach { index ->
                        val (start, end) = dailyTableCalc.offsetsVLine(index)
                        drawLine(
                            color = palette.lineColor,
                            start = start,
                            end = end, strokeWidth = 2.0f
                        )
                    }

                    for (courseCell in dailySetCourseCoverage.coverageData) {
                        val (topLeftCourse, sizeCourse) = dailyTableCalc.offsetAndSizeBlockCourseCell(
                            courseCell.dayOfWeek,
                            courseCell.section
                        )
                        val backgroundColor = dailyTableCalc.calcColorOfCourse(
                            dailySetCourse = courseCell.courses[0],
                            colors = courseBackgrounds
                        )
                        drawRoundRect(
                            color = backgroundColor,
                            topLeft = topLeftCourse,
                            size = sizeCourse,
                            cornerRadius = CornerRadius(8.0f, 8.0f)
                        )


                        // if the course has coverage data, will show a foreground color point on topRight
                        if (courseCell.courses.size > 1) {
                            val foregroundColor = dailyTableCalc.calcColorOfCourse(
                                dailySetCourse = courseCell.courses[0],
                                colors = courseForegrounds
                            )
                            val topRight = Offset(
                                x = topLeftCourse.x + sizeCourse.width - 16.0f,
                                y = topLeftCourse.y + 16.0f
                            )

                            drawCircle(
                                color = foregroundColor,
                                radius = 6.0f,
                                center = topRight
                            )

                        }
                    }


                }
                //val palette = LocalPalette.current

                /**
                 * inline function, createText of time duration.
                 */
                @Composable
                fun createTextTimeDuration(dailySetRC: DailySetRC, dailySetCell: DailySetCell) {
                    val currentIndexOfDailyCell =
                        dailyTableCalc.calcCurrentIndexOfDailyCell(dailySetRC, dailySetCell)
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

                @Composable
                fun createCourseText(dailySetCourseCell: DailySetCourseCell) {
                    val (offset, size) = dailyTableCalc.offsetAndSizeBlockCourseCell(
                        dailySetCourseCell.dayOfWeek,
                        dailySetCourseCell.section
                    )
                    val course = dailySetCourseCell.courses[0]
                    val color = dailyTableCalc.calcColorOfCourse(course, courseForegrounds)

                    Column(
                        modifier = Modifier
                            .size(toDp(px = size.width), toDp(px = size.height))
                            .absoluteOffset(toDp(px = offset.x), toDp(px = offset.y))
                            .wrapContentSize(align = Alignment.Center)
                    ) {
                        Text(
                            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                            text = course.name,
                            fontSize = if (course.name.length < 10) {
                                14.sp
                            } else {
                                12.sp
                            },
                            color = color,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                            text = course.location,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            color = color
                        )
                        Text(
                            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                            text = course.teacher,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            color = color
                        )
                    }
                }

                val currentDailyRC = dailyTableCalc.dailySetTRC.dailySetRCs.find {
                    it.dailySetRow.weekdays.toIntArray().contains(currentDayOfWeek.value)
                }

                @Suppress
                when {
                    currentDailyRC != null -> {
                        currentDailyRC.dailySetCells.forEach { dailyCell ->
                            createTextTimeDuration(
                                dailySetRC = currentDailyRC,
                                dailySetCell = dailyCell
                            )
                        }
                    }
                }

                dailySetCourseCoverage.coverageData.forEach {
                    createCourseText(dailySetCourseCell = it)
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
            fontSize = 18.sp
        )

        Text(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            text = currentDailyTable.name, color = DailySetTheme.color.sub
        )

    }
}