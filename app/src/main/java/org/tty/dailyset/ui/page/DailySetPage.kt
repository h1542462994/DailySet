package org.tty.dailyset.ui.page

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.tty.dailyset.R
import org.tty.dailyset.ui.component.ProfileMenuGroup
import org.tty.dailyset.ui.component.ProfileMenuItem
import org.tty.dailyset.ui.component.TitleSpace
import org.tty.dailyset.ui.image.ImageResource

@Composable
fun DailySetPage() {
    val scrollState = rememberScrollState()
    Column (
        modifier = Modifier.scrollable(state = scrollState, orientation = Orientation.Vertical)
    ) {
        AddSetPart()
        TitleSpace()
        AutoSetPart()
        TitleSpace()
        UserSetPart()
    }
}

/**
 * the add set part for dailySet.
 * .add
 */
@Composable
fun AddSetPart() {
    ProfileMenuItem(icon = ImageResource.add(), next = false, title = stringResource(R.string.add_list), content = "")
}

/**
 * the auto generated set part for dailySet.
 * .auto
 */
@Composable
fun AutoSetPart() {
    Column {
        ProfileMenuItem(icon = ImageResource.set_auto(), next = true, title = stringResource(R.string.class_table), content = "")
        ProfileMenuItem(icon = ImageResource.set_star(), next = true, title = stringResource(R.string.important), content = "")
    }
}

/**
 * the user set part for dailySet
 * .user
 */
@Composable
fun UserSetPart() {
    Column {


    }
}