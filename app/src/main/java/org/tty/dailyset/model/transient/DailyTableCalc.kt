package org.tty.dailyset.model.transient

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.tty.dailyset.model.entity.DailyTRC

/**
 * provide calculate properties for dailyTable, especially for draw.
 */
@Immutable
class DailyTableCalc(val dailyTRC: DailyTRC, private val measuredWidth: Float, private val unit: Float) {
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
    fun offsetYHLine(index: Int): Float {
        if (index <= counts[0]) {
            return cellHeight * index
        } else if (index <= counts[0] + counts[1] + 1) {
            return cellHeight * index - (cellHeight - spaceHeight)
        } else {
            return cellHeight * index - (cellHeight - spaceHeight) * 2
        }
    }

    fun offsetsHLine(index: Int): Pair<Offset, Offset> {
        return Pair(Offset(x = 0f, y = offsetYHLine(index)),
            Offset(x = measuredWidth, y = offsetYHLine(index)))
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