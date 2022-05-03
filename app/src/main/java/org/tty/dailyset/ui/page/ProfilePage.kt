package org.tty.dailyset.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.component.common.showToast
import org.tty.dailyset.component.profile.rememberProfileVM
import org.tty.dailyset.ui.component.ProfileMenuGroup
import org.tty.dailyset.ui.component.ProfileMenuItem
import org.tty.dailyset.ui.component.UserProfileSmall
import org.tty.dailyset.ui.image.ImageResource
import org.tty.dailyset.ui.theme.DailySetTheme

/**
 * ProfilePage, including user,settings
 */
@UseViewModel("/profile")
@Composable
fun ProfilePage() {
    val scrollState = rememberScrollState()
    val profileVM = rememberProfileVM()
    val user by profileVM.currentUser.collectAsState()

    Column(
        modifier = Modifier.verticalScroll(scrollState, enabled = true)
    ) {
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
        ProfileMenuItem(icon = {
            Icon(painter = ImageResource.user(), contentDescription = "", tint = DailySetTheme.color.primary)
        }, next = true, onClick = {}, title = {
            user?.let {
                UserProfileSmall(user = it)
            }
        }, content = {
            Text(text = "???")
        })
    }
}


@Composable
fun ProfileMenuGroupUserSettings() {
    val nav = LocalNav.current

    ProfileMenuGroup(title = stringResource(id = R.string.user_settings)) {
        ProfileMenuItem(
            icon = ImageResource.table(),
            useTint = true,
            title = stringResource(id = R.string.time_table),
            content = "系统默认",
            next = true,
            onClick = nav.action::toTimeTable
        )

        ProfileMenuItem(
            icon = ImageResource.link(),
            useTint = true,
            title = "自动课表",
            content = "未绑定",
            next = true,
            onClick = { showToast("未开发完成") }
        )
    }
}

@Composable
fun ProfileMenuGroupGlobalSettings() {
    val nav = LocalNav.current

    ProfileMenuGroup(title = stringResource(R.string.global_settings)) {
        ProfileMenuItem(
            icon = ImageResource.scan(),
            useTint = true,
            title = stringResource(R.string.debug),
            content = "",
            next = true,
            onClick = nav.action::toDebug
        )
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