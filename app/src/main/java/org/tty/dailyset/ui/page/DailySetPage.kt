package org.tty.dailyset.ui.page

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.tty.dailyset.R
import org.tty.dailyset.data.processor.DailySetProcessor
import org.tty.dailyset.data.scope.DataScope
import org.tty.dailyset.model.entity.DailySetIcon
import org.tty.dailyset.model.entity.DailySetType
import org.tty.dailyset.model.lifetime.dailyset.DailySetCreateState
import org.tty.dailyset.ui.component.ProfileMenuItem
import org.tty.dailyset.ui.component.TitleSpace
import org.tty.dailyset.ui.image.ImageResource

@Composable
fun DailySetPage() {
    val scrollState = rememberScrollState()
    with(DataScope) {
        val mainViewModel = mainViewModel()
        val service = mainViewModel.service
        val dailySetCreateState = dailySetCreateState()
        val dailySetProcessor = object: DailySetProcessor {
            override fun onCreate(dailySetName: String, icon: DailySetIcon?, type: DailySetType) {
                TODO("Not yet implemented")
            }

        }

        Column (
            modifier = Modifier.scrollable(state = scrollState, orientation = Orientation.Vertical)
        ) {
            DailySetAddPart(dailySetCreateState = dailySetCreateState)
            TitleSpace()
            DailySetAutoPart()
            TitleSpace()
            DailySetUserPart()
        }

        DailySetCreateDialogCover(
            dailySetProcessor = dailySetProcessor ,
            dailySetCreateState = dailySetCreateState)
    }


}

/**
 * the add set part for dailySet.
 * .add
 */
@Composable
fun DailySetAddPart(
    dailySetCreateState: DailySetCreateState
) {
    ProfileMenuItem(
        icon = ImageResource.add(),
        next = false,
        title = stringResource(R.string.add_list),
        content = "") {
        dailySetCreateState.dialogOpen.value = true
    }
}

/**
 * the auto generated set part for dailySet.
 * .auto
 */
@Composable
fun DailySetAutoPart() {
    Column {
        ProfileMenuItem(icon = ImageResource.set_auto(), next = true, title = stringResource(R.string.class_table), content = "")
        ProfileMenuItem(icon = ImageResource.set_duration(), next = true, title = stringResource(R.string.time_duration), content = "")
        ProfileMenuItem(icon = ImageResource.set_star(), next = true, title = stringResource(R.string.important), content = "")
    }
}

/**
 * the user set part for dailySet
 * .user
 */
@Composable
fun DailySetUserPart() {
    Column {


    }
}
