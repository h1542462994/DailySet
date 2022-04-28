package org.tty.dailyset.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.database.scope.DataScope
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.common.observable.collectAsState
import org.tty.dailyset.component.profile.profileVM
import org.tty.dailyset.ui.component.ProfileMenuGroup
import org.tty.dailyset.ui.component.ProfileMenuItem
import org.tty.dailyset.ui.image.ImageResource

/**
 * ProfilePage, including user,settings
 */
@UseViewModel("/profile")
@Composable
fun ProfilePage() {
    val scrollState = rememberScrollState()
    val profileVM = profileVM()
    val user by profileVM.currentUser.collectAsState()

    Column(
        modifier = Modifier.verticalScroll(scrollState, enabled = true)
    ){
        ProfileMenuGroupUser(user = user)
        ProfileMenuGroupUserSettings()
        ProfileMenuGroupGlobalSettings()
    }
}

@Composable
fun ProfileMenuGroupUser(user: User?) {
    // TODO: 2021/3/21 添加联网功能

    val display = if (user == null) {
        "(null)"
    } else {
        "${user.nickName} ${user.userUid}"
    }
    ProfileMenuGroup(title = stringResource(id = R.string.user)) {
        ProfileMenuItem(icon = ImageResource.user(), useTint = true, title = display, content = "本地账户", next = true)
    }
}


@Composable
fun ProfileMenuGroupUserSettings() {
    val nav = LocalNav.current

    ProfileMenuGroup(title = stringResource(id = R.string.user_settings)) {
        ProfileMenuItem(icon = ImageResource.table(), useTint = true, title = stringResource(id = R.string.time_table), content = "系统默认", next = true, onClick = nav.action::toTimeTable)
    }
}

@Composable
fun ProfileMenuGroupGlobalSettings() {
    val nav = LocalNav.current

    ProfileMenuGroup(title = stringResource(R.string.global_settings)) {
        ProfileMenuItem(icon = ImageResource.scan(), useTint = true, title = stringResource(R.string.debug), content = "", next = true, onClick = nav.action::toDebug)
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