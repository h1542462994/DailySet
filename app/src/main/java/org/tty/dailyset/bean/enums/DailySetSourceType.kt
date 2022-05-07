package org.tty.dailyset.bean.enums

enum class DailySetSourceType(val value: Int) {
    Table(1),
    Row(2),
    Cell(3),
    Duration(4),
    Course(10)
}