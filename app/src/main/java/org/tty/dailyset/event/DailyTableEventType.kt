package org.tty.dailyset.event

enum class DailyTableEventType: EventType {
    /**
     * 创建
     */
    Create,
    Delete,
    AddRow,
    ClickWeekDay,
    Rename,
    DeleteRow,
    ModifySection,
}