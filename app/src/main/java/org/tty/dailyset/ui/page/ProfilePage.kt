package org.tty.dailyset.ui.page

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.model.entity.Preference
import org.tty.dailyset.model.entity.PreferenceName
import org.tty.dailyset.model.entity.User
import org.tty.dailyset.provider.LocalMainViewModel
import org.tty.dailyset.ui.component.ProfileMenuGroup
import org.tty.dailyset.ui.component.ProfileMenuItem

/**
 * ProfilePage, including user,settings
 */
@UseViewModel
@Composable
fun ProfilePage() {
    val mainViewModel = LocalMainViewModel.current
    val seedVersionPreference by mainViewModel.seedVersionPreference.observeAsState(Preference.default(PreferenceName.SEED_VERSION))
    val seedVersion by mainViewModel.seedVersion.observeAsState(0)

    // state of user..
    val users by mainViewModel.users.observeAsState(listOf())
    val currentUserUid by mainViewModel.currentUserUid.observeAsState(Preference.default(PreferenceName.CURRENT_USER_UID))
    val currentUser = users.find { it.uid == currentUserUid }

    Column(
        modifier = Modifier.scrollable(rememberScrollState(0),Orientation.Vertical)
    ){
        ProfileMenuGroupUser(user = currentUser)
        ProfileMenuGroupUserSettings()
        Text(text = seedVersionPreference.toString())
        Text(text = seedVersion.toString())
    }

}

@Composable
fun ProfileMenuGroupUser(user: User?) {
    //TODO 添加联网功能

    val display = if (user == null) {
        "(null)"
    } else {
        "${user.nickName} ${user.uid}"
    }
    ProfileMenuGroup(title = stringResource(id = R.string.user)) {
        ProfileMenuItem(icon = Icons.Filled.Person, title = display, content = "本地账户", true)
    }
}


@Composable
fun ProfileMenuGroupUserSettings() {
    ProfileMenuGroup(title = stringResource(id = R.string.user_settings)) {
        ProfileMenuItem(icon = Icons.Filled.Build, title = stringResource(id = R.string.time_table), content = "系统默认", true, LocalNav.current.action.toTimeTable)
    }
}


@Preview
@Composable
fun ProfileMenuGroupUserPreview() {
    ProfileMenuGroupUser(user = null)
}