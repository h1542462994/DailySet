package org.tty.dailyset.model.transient

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
    val menuWidth = unit * 2

    val cellWidth = (measuredWidth - menuWidth) / cellColumnCount
    val cellHeight = unit * 2.5f
    val spaceHeight = unit

    /**
     * counts for am, pm and evening.
     */
    val counts = listOf(calcCountV(0), calcCountV(1), calcCountV(2))

    /**
     * the count of horizontal lines to draw.
     */
    val drawCountHLine = counts.sum() + 3
    val canvasHeight = cellHeight * counts.sum() + spaceHeight * 2

    /**
     * calc the offset.Y for the line.
     */
     private fun offsetYHLine(index: Int): Float {
        return when {
            index <= counts[0] -> cellHeight * index
            index <= counts[0] + counts[1] + 1 -> cellHeight * index - (cellHeight - spaceHeight)
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