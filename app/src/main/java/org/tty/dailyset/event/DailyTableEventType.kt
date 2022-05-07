package org.tty.dailyset.event

@Deprecated("not used yet.", level = DeprecationLevel.WARNING)
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
    ModifyCell
}