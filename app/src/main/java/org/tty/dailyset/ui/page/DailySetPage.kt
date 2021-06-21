package org.tty.dailyset.ui.page

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.tty.dailyset.R
import org.tty.dailyset.ui.component.ProfileMenuItem
import org.tty.dailyset.ui.component.TitleSpace
import org.tty.dailyset.ui.image.ImageResource

@Composable
fun DailySetPage() {
    val scrollState = rememberScrollState()
    Column (
        modifier = Modifier.scrollable(state = scrollState, orientation = Orientation.Vertical)
    ) {
        DailySetAddPart()
        TitleSpace()
        DailySetAutoPart()
        TitleSpace()
        DailySetUserPart()
    }
}

/**
 * the add set part for dailySet.
 * .add
 */
@Composable
fun DailySetAddPart() {
    ProfileMenuItem(icon = ImageResource.add(), next = false, title = stringResource(R.string.add_list), content = "")
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
