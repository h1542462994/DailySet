package org.tty.dailyset.model.entity

enum class DailySetType(val key: String) {
    Normal("normal"),
    Clazz("clazz"),
    /**
     * TODO dailySetType task_specific
     */
    TaskSpecific("task_specific")
}