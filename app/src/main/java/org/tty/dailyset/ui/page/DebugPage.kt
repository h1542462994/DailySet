package org.tty.dailyset.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.tty.dailyset.R
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.common.datetime.toDisplayString
import org.tty.dailyset.common.datetime.toStandardString
import org.tty.dailyset.component.common.StatusBarToBackground
import org.tty.dailyset.component.common.nav
import org.tty.dailyset.component.debug.rememberDebugVM
import org.tty.dailyset.ui.component.TopBar
import org.tty.dailyset.ui.theme.DailySetTheme

/**
 * page for debug and test
 */
@Composable
@UseViewModel("/debug")
fun TestPage() {
    StatusBarToBackground()
    val scrollState = rememberScrollState()
    val debugVM = rememberDebugVM()
    val nav = nav()
    val seedVersion by debugVM.seedVersion.collectAsState()
    val currentUserUid by debugVM.currentUserUid.collectAsState()
    val now by debugVM.now.collectAsState()
    val nowDayOfWeek by debugVM.nowDayOfWeek.collectAsState()
    val startDayOfWeek by debugVM.startDayOfWeek.collectAsState()
    val currentUser by debugVM.currentUser.collectAsState()
    val users by debugVM.users.collectAsState()
    val currentHttpServerAddress by debugVM.currentHttpServerAddress.collectAsState()
    val deviceCode by debugVM.deviceCode.collectAsState()

    Column {
        TopBar(
            title = stringResource(id = R.string.debug),
            true,
            onBackPressed = nav::upPress
        )
        LazyColumn(
            modifier = Modifier
                .weight(1.0f)
                .scrollable(scrollState, orientation = Orientation.Vertical)
        ) {
            item {
                DebugLine(
                    title = stringResource(id = R.string.seed_version_description),
                    subTitle = "seedVersion",
                    value = "$seedVersion"
                )
            }
            item {
                DebugLine(
                    title = stringResource(id = R.string.current_user_uid_description),
                    subTitle = "currentUserUid",
                    value = currentUserUid
                )
            }
            item {
                DebugLine(
                    title = stringResource(id = R.string.now_description),
                    subTitle = "now",
                    value = now.toStandardString()
                )
            }
            item {
                DebugLine(
                    title = stringResource(id = R.string.now_day_of_week_description),
                    subTitle = "nowDayOfWeek",
                    value = nowDayOfWeek.toDisplayString()
                )
            }
            item {
                DebugLine(
                    title = "开始星期",
                    subTitle = "startDayOfWeek",
                    value = startDayOfWeek.toDisplayString()
                )

            }
            item {
                DebugLine(
                    title = "当前用户",
                    subTitle = "currentUser",
                    value = "${currentUser.nickName}(${currentUser.userUid})"
                )
            }
            item {
                DebugLine(title = "所有用户", subTitle = "users", value = users.joinToString(
                    separator = ","
                ) { it.userUid })
            }
            item {
                DebugLine(title = "当前服务器地址", subTitle = "currentHttpServerAddress", value = currentHttpServerAddress)
            }
            item {
                DebugLine(title = "设备码", subTitle = "deviceCode", value = deviceCode)
            }
            item {
                Button(onClick = {
                    debugVM.setFirstLoadUser(true)
                }) {
                    Text(text = "退出登录")
                }
            }

        }

    }
}

@Composable
fun DebugLine(title: String, subTitle: String, value: String) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Column(modifier = Modifier.weight(1.0f)) {
                Text(
                    text = title,
                    color = DailySetTheme.color.primary,
                    style = MaterialTheme.typography.subtitle2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subTitle,
                    color = DailySetTheme.color.sub,
                    style = MaterialTheme.typography.body2
                )
            }
            Text(
                text = value,
                modifier = Modifier.align(alignment = Alignment.CenterVertically),
                color = DailySetTheme.color.sub,
                style = MaterialTheme.typography.body2
            )
        }
        Spacer(
            modifier = Modifier
                .background(DailySetTheme.color.background2)
                .height(1.dp)
                .fillMaxWidth()
        )
    }

}

@Preview
@Composable
fun DebugLinePreview() {
    DebugLine(title = "测试", subTitle = "test.key", value = "12345")
}