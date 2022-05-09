package org.tty.dailyset.bean.lifetime

import org.tty.dailyset.bean.entity.DailySetCourse
import java.time.DayOfWeek

data class DailySetCourseCell(
    val dayOfWeek: DayOfWeek,
    val section: IntRange,
    val courses: List<DailySetCourse>
)