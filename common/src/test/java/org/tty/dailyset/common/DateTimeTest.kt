@file:Suppress("SameParameterValue")

package org.tty.dailyset.common

import org.junit.Assert.assertEquals
import org.junit.Test
import org.tty.dailyset.common.datetime.*
import java.time.LocalDate
import java.time.LocalTime

class DateTimeTest {
    /**
     * test the function toWeekStart
     */
    @Test
    fun testWeekStart() {
        assertEquals(testDate.toWeekStart(), testStart)
        assertEquals(testDate.toWeekEnd(), testEnd)
    }

    @Test
    fun testWeekCount() {
        testASpanCase(testStart, testEnd, 1, 1)
        testASpanCase(testStart, testDate, 1, 1)
        testASpanCase(testDate, testEnd, 1, 1)
        testASpanCase(testStart2, testEnd2, 1, 2)
        testASpanCase(testStart2, testEnd, 2, 2)
    }

    @Test
    fun testSpanExtend() {
        val span = DateSpan(testStart, testDate)
        val expectSpan = DateSpan(testStart, testEnd)
        assertEquals(span.expandToFullWeek(), expectSpan)
    }

    @Test
    fun test() {
        val date1 = LocalDate.parse("2021-10-04")
        val date2 = LocalDate.parse("2022-01-23")
        val span = DateSpan(date1, date2)
        println(span)
    }

    @Test
    fun testFormat() {
        val time1 = LocalTime.of(8,0)
        assertEquals("08:00", time1.toShortString())
        assertEquals("8:00", time1.toShortString(ignoreFirstZero = true))

        val date1 = LocalDate.of(2021, 1, 1)
        println(date1.toShortString())
    }

    @Test
    fun testMinutesTo() {
        val time1 = LocalTime.of(8, 0)
        val time2 = LocalTime.of(9, 30)

        println(time1 minutesTo time2)
    }

    private fun testASpanCase(startDate: LocalDate, endDate: LocalDate, expectWeekCount: Int, expectWeekCountFull: Int) {
        val dateSpan = DateSpan(startDate, endDate)
        println(dateSpan)
        assertEquals(dateSpan.weekCount, expectWeekCount)
        assertEquals(dateSpan.weekCountFull, expectWeekCountFull)
    }

    companion object {
        val testDate: LocalDate = LocalDate.of(2021, 10, 14)
        val testStart: LocalDate = LocalDate.of(2021, 10, 11)
        val testEnd: LocalDate = LocalDate.of(2021, 10, 17)
        val testStart2: LocalDate = LocalDate.of(2021, 10, 10)
        val testEnd2: LocalDate = LocalDate.of(2021, 10, 16)
    }
}