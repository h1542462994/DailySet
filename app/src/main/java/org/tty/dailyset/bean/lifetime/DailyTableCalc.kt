/**
 * create at 2021/4/21
 * @author h1542462994
 */

package org.tty.dailyset.bean.lifetime

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import org.tty.dailyset.bean.entity.DailySetCell
import org.tty.dailyset.bean.entity.DailySetCourse
import org.tty.dailyset.common.datetime.indexTo
import org.tty.dailyset.common.util.toIntArray
import org.tty.dailyset.ui.theme.DailySetTheme
import java.time.DayOfWeek

/**
 * provide calculate properties and measurements for dailySetTable, especially for draw.
 * @param dailySetTRC the reference data emitting.
 * @param measuredWidth the canvas width by pixel.
 * @param unit the unit of the measurement by pixel.
 */
class DailyTableCalc(val dailySetTRC: DailySetTRC, val measuredWidth: Float, val unit: Float) {
    /**
     * the column Count for cell.
     */
    val cellColumnCount = 7

    /**
     * the first column width of the table
     */
    val menuWidth = unit * 1.8f

    /**
     * the cell width
     */
    val cellWidth = (measuredWidth - menuWidth) / cellColumnCount

    /**
     * the cell height
     */
    val cellHeight = unit * 2.5f

    /**
     * the space height between am, pm and evening.
     */
    val spaceHeight = unit

    /**
     * counts for am, pm and evening.
     */
    val cellRowCounts = listOf(calcCountV(0), calcCountV(1), calcCountV(2))

    /**
     * no painting area for time duration text.
     */
    val noDurationTextVerticalIndexes = listOf(cellRowCounts[0], cellRowCounts[0] + cellRowCounts[1] + 1)

    /**
     * the count of horizontal lines to draw.
     */
    val drawCountHLine = cellRowCounts.sum() + 3
    val canvasHeightBody = cellHeight * cellRowCounts.sum() + spaceHeight * 2
    val canvasHeightHeader = unit * 1.5f


    /**
     * return rectangle of the menu by dailyCell
     */
    fun offsetsAndSizeBlockMenu(dailySetCell: DailySetCell): Pair<Offset, Size> {
        val indexOfTimeDuration = calcIndexOfTimeDuration(dailySetCell)
        return offsetsAndSizeBlockMenu(indexOfTimeDuration)
    }

    /**
     * calculate the dailySet's currentIndex
     */
    fun calcCurrentIndexOfDailyCell(dailySetRC: DailySetRC, dailySetCell: DailySetCell): Int {
        assert(dailySetCell.normalType in 0..2)
        val counts = calcCountsOfDailyRC(dailySetRC)
        if (dailySetCell.normalType == 0){
            return dailySetCell.serialIndex
        } else if (dailySetCell.normalType == 1) {
            return counts[0] + dailySetCell.serialIndex
        } else {
            return counts[0] + counts[1] + dailySetCell.serialIndex
        }
    }

    /**
     * calc the offset begin point and end point for the horizontal line.
     */
    fun offsetsHLine(index: Int): Pair<Offset, Offset> {
        assert(index in 0 until drawCountHLine)
        return Pair(Offset(x = 0f, y = offsetYHLine(index)), Offset(x = measuredWidth, y = offsetYHLine(index)))
    }

    /**
     * calc the offset begin point and end point for the vertical line.
     */
    fun offsetsVLine(index: Int): Pair<Offset, Offset> {
        assert(index in 0 until cellColumnCount)
        return Pair(Offset(x = offsetXVLine(index), y = 0f), Offset(x = offsetXVLine(index), y = canvasHeightBody))
    }

    /**
     * rectangle of the background in body area.
     */
    fun offsetAndSizeBlock(index: Int): Pair<Offset, Size> {
        val topLeft = offsetsVLine(index).first
        val size = Size(width = cellWidth, height = canvasHeightBody)
        return Pair(topLeft, size)
    }


    fun offsetAndSizeBlockHeader(index: Int): Pair<Offset, Size> {
        val topLeft = offsetsVLine(index).first
        val size = Size(width = cellWidth, height = canvasHeightHeader)
        return Pair(topLeft, size)
    }

    fun offsetAndSizeBlockCourseCell(dayOfWeek: DayOfWeek, section: IntRange): Pair<Offset, Size> {
        val xIndex = dayOfWeek.indexTo(DayOfWeek.MONDAY)
        val x1 = offsetXVLine(xIndex)
        val y1 = offsetYHLine(calcRealIndexOfSectionIndex(dayOfWeek, section.first - 1))
        val x2 = offsetXVLine(xIndex) + cellWidth
        val y2 = offsetYHLine(calcRealIndexOfSectionIndex(dayOfWeek, section.last - 1)) + cellHeight


        return Pair(Offset(x1, y1), Size(x2 - x1, y2 - y1))
    }

    fun calcColorOfCourse(dailySetCourse: DailySetCourse, colors: List<Color>): Color {
        val index = dailySetCourse.name.hashCode().mod(colors.size)
        return colors[index]
    }

    private fun calcRealIndexOfSectionIndex(dayOfWeek: DayOfWeek, sectionIndex: Int): Int {
        val dailySetRC = dailySetTRC.dailySetRCs.firstOrNull {
            dayOfWeek.value in it.dailySetRow.weekdays.toIntArray()
        } ?: return 0

        val dailySetCell = dailySetRC.dailySetCells.firstOrNull {
            sectionIndex == it.currentIndex
        } ?: return 0

        val currentIndex = calcCurrentIndexOfDailyCell(dailySetRC, dailySetCell)
        return currentIndex + dailySetCell.normalType
    }

    /**
     * get the calculated draw property of the index of time duration, it will skip if current.sum < max.sum..
     * @param indexOfTimeDuration the index of the time duration.
     */
    private fun offsetsAndSizeBlockMenu(indexOfTimeDuration: Int): Pair<Offset, Size> {
        //val noPaintIndexes = listOf(counts[0], counts[0] + counts[1] + 1)
        assert(indexOfTimeDuration in 0 until cellRowCounts.sum())
        var real = indexOfTimeDuration;
        if (real >= cellRowCounts[0] + cellRowCounts[1]) {
            real += 2
        } else if (real >= cellRowCounts[0]) {
            real += 1
        }

        val offset = offsetsHLine(real).first
        val size = Size(width = menuWidth, height = cellHeight)
        return Pair(offset, size)
    }

    /**
     * translate the dailyCell to the index of the row.
     * @param dailySetCell which dailyCell
     */
    private fun calcIndexOfTimeDuration(dailySetCell: DailySetCell): Int {
        assert(dailySetCell.normalType in 0..2)
        return when (dailySetCell.normalType) {
            0 -> {
                dailySetCell.serialIndex
            }
            1 -> {
                cellRowCounts[0] + dailySetCell.serialIndex
            }
            else -> {
                cellRowCounts[0] + cellRowCounts[1] + dailySetCell.serialIndex
            }
        }
    }



    private fun calcCountsOfDailyRCAndNormalType(dailySetRC: DailySetRC, normalType: Int): Int {
        return dailySetRC.dailySetCells.count { it.normalType == normalType }
    }

    private fun calcCountsOfDailyRC(dailySetRC: DailySetRC): List<Int> {
        return (0..2).map { index ->
            calcCountsOfDailyRCAndNormalType(dailySetRC, index)
        }
    }

    /**
     * calc the offset.Y for the horizontal line.
     */
    private fun offsetYHLine(index: Int): Float {
        return when {
            index <= noDurationTextVerticalIndexes[0] -> cellHeight * index
            index <= noDurationTextVerticalIndexes[1] -> cellHeight * index - (cellHeight - spaceHeight)
            else -> cellHeight * index - (cellHeight - spaceHeight) * 2
        }
    }

    /**
     * calc the offset.X for the vertical line
     * @param index column index, means equal to weekDayCurrent
     */
    private fun offsetXVLine(index: Int): Float {
        return menuWidth + index * cellWidth
    }



    /**
     * calc the vertical count of the cell by normalType
     */
    private fun calcCountV(normalType: Int): Int {
        assert(normalType in 0..2)
        return dailySetTRC.dailySetRCs.maxOf { dailySetRC ->
            calcCountsOfDailyRCAndNormalType(dailySetRC, normalType)
        }
    }

    override fun toString(): String {
        return "DailyTableCalc(counts=$cellRowCounts)"
    }

}