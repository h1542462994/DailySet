package org.tty.dailyset.bean.entity

enum class DailySetType(val key: String) {
    Normal("normal"),
    Clazz("clazz"),
    /**
     * TODO dailySetType task_specific
     */
    TaskSpecific("task_specific")
}