package org.tty.dailyset.ui.page

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.tty.dailyset.data.processor.DailySetProcessor
import org.tty.dailyset.data.scope.DataScope
import org.tty.dailyset.model.entity.DailySetIcon
import org.tty.dailyset.model.entity.DailySetType
import org.tty.dailyset.model.lifetime.dailyset.DailySetCreateState

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
    DailySetUserPart()
}

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