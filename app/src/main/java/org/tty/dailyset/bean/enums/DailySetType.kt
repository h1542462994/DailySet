package org.tty.dailyset.bean.enums

enum class DailySetType(val value: Int) {
    Normal(0),
    Clazz(1),
    ClazzAuto(2),
    Task(3),
    Global(4),
    Generated(5),
    User(6);

    companion object {
        fun of(value: Int): DailySetType {
            return DailySetType.values().single { it.value == value }
        }
    }
}