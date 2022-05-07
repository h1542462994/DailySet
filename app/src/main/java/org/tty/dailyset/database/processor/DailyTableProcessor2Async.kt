package org.tty.dailyset.database.processor

//import androidx.room.Ignore
//import org.tty.dailyset.event.*
//import java.lang.IllegalArgumentException
//
//@Deprecated("instead with component VM.")
///**
// * DailyTable related operation used in dailyTableDao
// * @see [org.tty.dailyset.bean.dao.DailyTableDao]
// */
//interface DailyTableProcessor2Async : EventProcessorAsync {
//    @Ignore
//    override suspend fun performProcess(eventType: EventType, eventArgs: EventArgs) {
//        when (eventType) {
//            DailyTableEventType.Create -> {
//                createFromTemplate(eventArgs as DailyTableCreateEventArgs)
//            }
//            DailyTableEventType.Delete -> {
//                delete(eventArgs as DailyTableDeleteEventArgs)
//            }
//            DailyTableEventType.AddRow -> {
//                addRow(eventArgs as DailyTableAddRowEventArgs)
//            }
//            DailyTableEventType.ClickWeekDay -> {
//                clickWeekDay(eventArgs as DailyTableClickWeekDayEventArgs)
//            }
//            DailyTableEventType.Rename -> {
//                rename(eventArgs as DailyTableRenameEventArgs)
//            }
//            DailyTableEventType.DeleteRow -> {
//                deleteRow(eventArgs as DailyTableRowDeleteEventArgs)
//            }
//            DailyTableEventType.ModifySection -> {
//                modifySection(eventArgs as DailyTableModifySectionEventArgs)
//            }
//            DailyTableEventType.ModifyCell -> {
//                modifyCell(eventArgs as DailyTableModifyCellEventArgs)
//            }
//            else -> {
//                throw IllegalArgumentException("eventType not supported.")
//            }
//        }
//    }
//
//    suspend fun createFromTemplate(dailyTableCreateEventArgs: DailyTableCreateEventArgs)
//    suspend fun delete(dailyTableDeleteEventArgs: DailyTableDeleteEventArgs)
//    suspend fun addRow(dailyTableAddRowEventArgs: DailyTableAddRowEventArgs)
//    suspend fun clickWeekDay(dailyTableClickWeekDayEventArgs: DailyTableClickWeekDayEventArgs)
//    suspend fun rename(dailyTableRenameEventArgs: DailyTableRenameEventArgs)
//    suspend fun deleteRow(dailyTableRowDeleteEventArgs: DailyTableRowDeleteEventArgs)
//    suspend fun modifySection(dailyTableModifySectionEventArgs: DailyTableModifySectionEventArgs)
//    suspend fun modifyCell(dailyTableModifyCellEventArgs: DailyTableModifyCellEventArgs)
//}