package org.tty.dailyset.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tty.dailyset.R
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.bean.enums.PlatformState
import org.tty.dailyset.bean.lifetime.DialogState
import org.tty.dailyset.bean.lifetime.UserStatusShow
import org.tty.dailyset.component.common.DialogVM
import org.tty.dailyset.component.common.asMutableState
import org.tty.dailyset.component.user.UserShiftDialogVM
import org.tty.dailyset.component.user.UserVM
import org.tty.dailyset.component.user.rememberUserVM
import org.tty.dailyset.ui.component.*
import org.tty.dailyset.ui.image.ImageResource
import org.tty.dailyset.ui.theme.DailySetTheme
import org.tty.dailyset.ui.theme.LocalPalette

@Composable
@UseViewModel("/user")
fun UserPage() {
    val userVM = rememberUserVM()
    val users by userVM.users.collectAsState()
    val currentUser by userVM.currentUser.collectAsState()
    val userShiftDialogVM = userVM.userShiftDialogVM
    val userLogoutDialogVM = userVM.userLogoutDialogVM
    val nav = userVM.nav

    Column {
        TopBar(title = stringResource(id = R.string.user), useBack = true, onBackPressed = {
            nav.upPress()
        })

        LazyColumn(modifier = Modifier.weight(1.0f)) {
            item {
                UserCurrent(user = currentUser)
            }
            item {
                UserAll(
                    users = users,
                    currentUser = currentUser,
                    userVM = userVM,
                    userShiftDialogVM = userShiftDialogVM
                )
            }
        }



        OutlinedButton(
            onClick = {
                userLogoutDialogVM.dialogOpen.value = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = DailySetTheme.color.error
            ),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(stringResource(R.string.logout), style = DailySetTheme.typography.buttonText)
        }
    }

    UserShiftDialogCover(userShiftDialogVM = userShiftDialogVM, userVM = userVM)
    UserLogoutDialogCover(userLogoutDialogVM = userLogoutDialogVM, userVM = userVM)
}

@Composable
private fun UserCurrent(user: User) {
    ProfileMenuGroup(title = stringResource(id = R.string.user_current)) {
        ProfileMenuItem(title = stringResource(id = R.string.user_name), content = user.userUid)
        ProfileMenuItem(
            title = stringResource(id = R.string.user_nickname),
            content = user.nickName
        )
        ProfileMenuItem(title = stringResource(id = R.string.user_email), content = user.email)
    }
}

@Composable
private fun UserAll(
    users: List<User>,
    currentUser: User,
    userVM: UserVM,
    userShiftDialogVM: UserShiftDialogVM
) {
    ProfileMenuGroup(title = stringResource(id = R.string.user_all)) {
        users.sortedBy { it.userUid }.forEach { user ->
            if (user.userUid == currentUser.userUid) {
                UserProfileMenuItemActive(user = user, userVM = userVM)
            } else {
                UserProfileMenuItemNotActive(user = user, userShiftDialogVM = userShiftDialogVM)
            }
        }
    }
}

@Composable
private fun UserProfileMenuItemActive(user: User, userVM: UserVM) {
    val userStatusShow = UserStatusShow(user)
    ProfileMenuItem(icon = {
        Icon(
            painter = ImageResource.done(),
            contentDescription = "",
            tint = DailySetTheme.color.primary
        )
    }, onClick = {
        if (userStatusShow.user.state == PlatformState.ALIVE.state) {
            // skip
        } else {
            userVM.reLogin(userStatusShow.user)
        }
    }, title = {
        UserProfileSmall(user = user)
    }, content = {
        TextWithDot(text = userStatusShow.describeString, color = userStatusShow.pointColor)
    })
}

@Composable
private fun UserProfileMenuItemNotActive(
    user: User,
    userShiftDialogVM: UserShiftDialogVM
) {
    ProfileMenuItem(icon = {
        // not active user has no icon.
    }, title = {
        UserProfileSmall(user = user)
    }, onClick = {
        // open the dialog to shift the user.
        userShiftDialogVM.shiftToUser.value = user
        userShiftDialogVM.dialogOpen.value = true
    }, content = {
        Text(
            text = stringResource(R.string.user_shift_click),
            color = LocalPalette.current.sub,
            fontSize = 16.sp
        )
    })
}

@Composable
private fun UserShiftDialogCover(
    userShiftDialogVM: UserShiftDialogVM,
    userVM: UserVM
) {
    val shiftToUser by userShiftDialogVM.shiftToUser.collectAsState()
    val dialogOpen = userShiftDialogVM.dialogOpen.asMutableState()

    NanoDialog(
        title = stringResource(R.string.user_shift),
        dialogVM = userShiftDialogVM,
        autoClose = true
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.user_shift_confirm_1, shiftToUser.userUid)
        )
        NanoDialogButton(text = stringResource(id = R.string.confirm)) {
            userVM.changeCurrentUser(shiftToUser)
            dialogOpen.value = false
        }
    }
}

@Composable
private fun UserLogoutDialogCover(
    userLogoutDialogVM: DialogVM,
    userVM: UserVM
) {
    NanoDialog(title = stringResource(id = R.string.logout), dialogVM = userLogoutDialogVM) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.user_logout_confirm)
        )
        NanoDialogButton(text = stringResource(id = R.string.confirm), error = true) {
            userVM.logout()
            userLogoutDialogVM.dialogOpen.value = false
        }
    }
}