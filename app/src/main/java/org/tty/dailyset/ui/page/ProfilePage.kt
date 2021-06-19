package org.tty.dailyset.ui.page

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.data.scope.DataScope
import org.tty.dailyset.model.entity.User
import org.tty.dailyset.ui.component.ProfileMenuGroup
import org.tty.dailyset.ui.component.ProfileMenuItem
import org.tty.dailyset.ui.image.ImageResource

/**
 * ProfilePage, including user,settings
 */
@UseViewModel
@Composable
fun ProfilePage() {
    with(DataScope) {
        val seedVersionPreference by seedVersionPreference()
        val seedVersion by seedVersion()


        // state of user..
        val currentUser by currentUser()

        Column(
            modifier = androidx.compose.ui.Modifier.scrollable(
                rememberScrollState(0),
                androidx.compose.foundation.gestures.Orientation.Vertical
            )
        ){
            ProfileMenuGroupUser(user = currentUser)
            ProfileMenuGroupUserSettings()
            //Text(text = seedVersionPreference.toString())
            //Text(text = seedVersion.toString())
        }
    }


}

@Composable
fun ProfileMenuGroupUser(user: User?) {
    // TODO: 2021/3/21 添加联网功能

    val display = if (user == null) {
        "(null)"
    } else {
        "${user.nickName} ${user.uid}"
    }
    ProfileMenuGroup(title = stringResource(id = R.string.user)) {
        ProfileMenuItem(icon = ImageResource.user(), title = display, content = "本地账户", next = true)
    }
}


@Composable
fun ProfileMenuGroupUserSettings() {
    ProfileMenuGroup(title = stringResource(id = R.string.user_settings)) {
        ProfileMenuItem(icon = ImageResource.table(), title = stringResource(id = R.string.time_table), content = "系统默认", next = true, onClick = LocalNav.current.action.toTimeTable)
        ProfileMenuItem(icon = ImageResource.scan(), title = stringResource(R.string.debug), content = "", next = true, onClick = LocalNav.current.action.toTest)
    }
}


@Preview
@Composable
fun ProfileMenuGroupUserPreview() {
    ProfileMenuGroupUser(user = null)
}

@Preview
@Composable
fun ProfilePagePreview() {
    ProfilePage()
}