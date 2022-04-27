package org.tty.dailyset.ui.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.tty.dailyset.common.datetime.epochLocalDateTime
import org.tty.dailyset.database.processor.DailySetProcessor
import org.tty.dailyset.database.scope.DataScope
import org.tty.dailyset.bean.entity.DailySet
import org.tty.dailyset.bean.entity.DailySetIcon
import org.tty.dailyset.bean.entity.DailySetType

@Preview
@Composable
fun DailySetAddPartPreview() {
    with(DataScope) {
        DailySetAddPart(dailySetCreateState = dailySetCreateState())
    }
}

@Preview
@Composable
fun DailySetAutoPartPreview() {
    DailySetAutoPart()
}

@Preview
@Composable
fun DailySetUserPartPreview() {
    val dailySets = listOf(
        DailySet(type = DailySetType.Normal, icon = DailySetIcon.Book, updateAt = epochLocalDateTime()),
        DailySet(type = DailySetType.Clazz, icon = DailySetIcon.Book, updateAt = epochLocalDateTime()),
        DailySet(type = DailySetType.TaskSpecific, icon = DailySetIcon.Book, updateAt = epochLocalDateTime())
    )
    DailySetUserPart(dailySets) {

    }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun DailySetCreateDialogCoverPreview() {
    val processor = object: DailySetProcessor {
        override fun onCreate(dailySetName: String, icon: DailySetIcon?, type: DailySetType) {
            TODO("Not yet implemented")
        }
    }
    with(DataScope) {
        val dailySetCreateState = dailySetCreateState()
        dailySetCreateState.dialogOpen.value = true
        DailySetCreateDialogCover(dailySetProcessor = processor, dailySetCreateState = dailySetCreateState)
    }

}