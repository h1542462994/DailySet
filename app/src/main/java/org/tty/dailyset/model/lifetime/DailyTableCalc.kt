package org.tty.dailyset.model.lifetime

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import org.tty.dailyset.model.entity.DailyTRC

/**
 * provide calculate properties for dailyTable, especially for draw.
 */
@Immutable
class DailyTableCalc(val dailyTRC: DailyTRC, val measuredWidth: Float, val unit: Float) {
    /**
     * the column Count for cell.
     */
    val cellColumnCount = 7


    /**
     * the first column's width of the table
     */
    val menuWidth = unit * 1.8f

    val cellWidth = (measuredWidth - menuWidth) / cellColumnCount
    val cellHeight = unit * 2.5f
    val spaceHeight = unit

    /**
     * counts for am, pm and evening.
     */
    val counts = listOf(calcCountV(0), calcCountV(1), calcCountV(2))
    val noPaintIndexes = listOf(counts[0], counts[0] + counts[1] + 1)

    /**
     * the count of horizontal lines to draw.
     */
    val drawCountHLine = counts.sum() + 3
    val canvasHeight = cellHeight * counts.sum() + spaceHeight * 2
    val canvasHeightHeader = unit * 1.5f

    fun offsetsAndSizeBlockMenu(index: Int): Pair<Offset, Size> {
        //val noPaintIndexes = listOf(counts[0], counts[0] + counts[1] + 1)
        var real = index;
        if (real >= counts[0] + counts[1]) {
            real += 2
        } else if (real >= counts[0]) {
            real += 1
        }

        val offset = offsetsHLine(real).first
        val size = Size(width = menuWidth, height = cellHeight)
        return Pair(offset, size)
    }

    /**
     * calc the offset.Y for the line.
     */
     private fun offsetYHLine(index: Int): Float {
        return when {
            index <= noPaintIndexes[0] -> cellHeight * index
            index <= noPaintIndexes[1] -> cellHeight * index - (cellHeight - spaceHeight)
            else -> cellHeight * index - (cellHeight - spaceHeight) * 2
        }
    }

    private fun offsetXVLine(index: Int): Float {
        return menuWidth + index * cellWidth
    }

    fun offsetsHLine(index: Int): Pair<Offset, Offset> {
        return Pair(Offset(x = 0f, y = offsetYHLine(index)), Offset(x = measuredWidth, y = offsetYHLine(index)))
    }

    fun offsetsVLine(index: Int): Pair<Offset, Offset> {
        return Pair(Offset(x = offsetXVLine(index), y = 0f), Offset(x = offsetXVLine(index), y = canvasHeight))
    }

    fun offsetAndSizeBlock(index: Int): Pair<Offset, Size> {
        val topLeft = offsetsVLine(index).first
        val size = Size(width = cellWidth, height = canvasHeight)
        return Pair(topLeft, size)
    }

    fun offsetAndSizeBlockHeader(index: Int): Pair<Offset, Size> {
        val topLeft = offsetsVLine(index).first
        val size = Size(width = cellWidth, height = canvasHeightHeader)
        return Pair(topLeft, size)
    }

    /**
     * calc the vertical count of the cell by normalType
     */
    private fun calcCountV(x: Int): Int {
        return dailyTRC.dailyRCs.maxOf { dailyRC ->
            dailyRC.dailyCells.count { it.normalType == x }
        }
    }

    override fun toString(): String {
        return "DailyTableCalc(counts=$counts)"
    }


}