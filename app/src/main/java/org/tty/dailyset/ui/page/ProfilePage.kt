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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.bean.entity.UserTicketInfo
import org.tty.dailyset.bean.enums.PlatformState
import org.tty.dailyset.bean.enums.UnicTickStatus
import org.tty.dailyset.component.common.showToast
import org.tty.dailyset.component.profile.rememberProfileVM
import org.tty.dailyset.dailyset_cloud.grpc.TicketBindInfo
import org.tty.dailyset.ui.component.ProfileMenuGroup
import org.tty.dailyset.ui.component.ProfileMenuItem
import org.tty.dailyset.ui.component.TextWithDot
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
    val userTicketInfo by profileVM.userTicketInfo.collectAsState()

    Column(
        modifier = Modifier.verticalScroll(scrollState, enabled = true)
    ) {
        ProfileMenuGroupUser(user = user)
        ProfileMenuGroupUserSettings(userTicketInfo)
        ProfileMenuGroupGlobalSettings()
    }
}

@Composable
fun ProfileMenuGroupUser(user: User) {
    // TODO: 2021/3/21 添加联网功能

    val userStatusShow = UserStatusShow(user)
    ProfileMenuGroup(title = stringResource(id = R.string.user)) {
        ProfileMenuItem(icon = {
            Icon(painter = ImageResource.user(), contentDescription = "", tint = DailySetTheme.color.primary)
        }, next = true, onClick = {}, title = {
            UserProfileSmall(user = user)
        }, content = {
            TextWithDot(text = userStatusShow.describeString, color = userStatusShow.pointColor)
        })
    }
}


@Composable
fun ProfileMenuGroupUserSettings(userTicketInfo: UserTicketInfo) {
    val nav = LocalNav.current

    ProfileMenuGroup(title = stringResource(id = R.string.user_settings)) {
        ProfileMenuItem(
            icon = ImageResource.table(),
            useTint = true,
            title = stringResource(id = R.string.time_table),
            content = "系统默认",
            next = true,
            onClick = { showToast("该功能暂未实现") }
        )

        val ticketStatusShow = TicketStatusShow(userTicketInfo)

        ProfileMenuItem(
            icon = ImageResource.link(),
            useTint = true,
            title = "自动课表",
            content = ticketStatusShow.describeString,
            next = true,
            pointColor = ticketStatusShow.pointColor,
            onClick = {
                when (userTicketInfo.status) {
                    UnicTickStatus.NotBind -> {
                        nav.action.toTicketBind()
                    }
                    UnicTickStatus.Initialized -> {
                        showToast("正在初始化中,请稍后...")
                    }
                    else -> {
                        showToast("该功能暂未实现.")
                    }
                }
            }
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

private data class TicketStatusShow(
    val userTicketInfo: UserTicketInfo
) {
    val pointColor @Composable get() =  when (userTicketInfo.status) {
        UnicTickStatus.NotBind -> DailySetTheme.color.statusGray
        UnicTickStatus.Initialized -> DailySetTheme.color.statusGray
        UnicTickStatus.Checked -> DailySetTheme.color.statusGreen
        UnicTickStatus.UnknownFailure -> DailySetTheme.color.statusOrange
        UnicTickStatus.PasswordFailure -> DailySetTheme.color.statusRed
    }


    val describeString @Composable get() = when (userTicketInfo.status) {
        // TODO: 文本资源
        UnicTickStatus.NotBind -> "未绑定"
        UnicTickStatus.Initialized -> "初始化中"
        UnicTickStatus.Checked -> "正常(${userTicketInfo.studentUid})"
        UnicTickStatus.UnknownFailure -> "未知错误"
        UnicTickStatus.PasswordFailure -> "密码错误"
    }
}

private data class UserStatusShow(
    val user: User
) {
    val pointColor @Composable get() = when (user.state) {
        PlatformState.ALIVE.state -> DailySetTheme.color.statusGreen
        PlatformState.LEAVE.state -> DailySetTheme.color.statusGray
        PlatformState.INVALID.state -> DailySetTheme.color.statusOrange
        PlatformState.BAN.state -> DailySetTheme.color.statusRed
        else -> DailySetTheme.color.statusRed
    }

    val describeString @Composable get() = when (user.state) {
        PlatformState.ALIVE.state -> "在线"
        PlatformState.LEAVE.state -> "离线"
        PlatformState.INVALID.state -> "已失效"
        PlatformState.BAN.state -> "已封禁"
        else -> "未知错误"
    }
}


@Preview
@Composable
fun ProfileMenuGroupUserPreview() {
    ProfileMenuGroupUser(user = User.default())
}

@Preview
@Composable
fun ProfilePagePreview() {
    ProfilePage()
}