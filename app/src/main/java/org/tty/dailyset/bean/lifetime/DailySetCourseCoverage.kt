package org.tty.dailyset.bean.lifetime

import org.tty.dailyset.bean.entity.DailySetCourse
import org.tty.dailyset.common.util.toIntArray
import org.tty.dioc.util.pair
import java.time.DayOfWeek

data class DailySetCourseCoverage(
    val courses: List<DailySetCourse>,
    /**
     * **week**, if the week is null, means course will not be filtered by week.
     */
    val week: Int? = null
) {
    // coverage of the course. to handle the coverage of the course
    val coverageData: MutableList<DailySetCourseCell> = mutableListOf()

    init {
        val coursesMap = courses.groupBy { it.weekDay }
        for (entry in coursesMap) {
            val range = mutableListOf<Pair<IntRange, List<DailySetCourse>>>()

            for (course in entry.value) {
                val courseWeeks = course.weeks.toIntArray()
                if (week != null && week !in courseWeeks) {
                    continue
                }

                val currentRange = course.sectionStart..course.sectionEnd
                val coverRange = selectCoverRange(currentRange, range)
                if (coverRange.isEmpty()) {
                    range.add(pair(course.sectionStart..course.sectionEnd, listOf(course)))
                } else {
                    // FIXME: 修复这部分的逻辑错误，主要是课程覆盖相关的问题。
                    range.add(reshape(course, coverRange.flatMap { it.second }))
                }
            }

            coverageData.addAll(range.map {
                DailySetCourseCell(
                    dayOfWeek = DayOfWeek.of(entry.key),
                    section = it.first,
                    courses = it.second
                )
            })
        }
    }

    private fun selectCoverRange(
        current: IntRange,
        range: MutableList<Pair<IntRange, List<DailySetCourse>>>
    ): List<Pair<IntRange, List<DailySetCourse>>> {
        val result = range.filter {
            val r = it.first
            current.first in r || current.last in r
        }
        range.removeAll(result)
        return result
    }

    private fun reshape(
        course: DailySetCourse,
        source: List<DailySetCourse>
    ): Pair<IntRange, List<DailySetCourse>> {
        val ranges = buildList {
            add(course.sectionStart..course.sectionEnd)
            addAll(source.map { it.sectionStart..it.sectionEnd })
        }

        val rangesMin = ranges.minOf { it.first }
        val c = ranges
            .filter { it.first == rangesMin }
            .maxByOrNull { it.last }!!

        return pair(c, buildList {
            addAll(source)
            add(course)
        })
    }


}