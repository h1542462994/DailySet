package org.tty.dailyset.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.ui.theme.DailySetTheme

@Composable
fun UserProfileSmall(user: User) {
    Column {
        Text(user.name, style = DailySetTheme.typography.subTitleText, color = DailySetTheme.color.primary)
        Spacer(modifier = Modifier.height(2.dp))
        Text(user.nickName, style = MaterialTheme.typography.body2, color = DailySetTheme.color.sub)
    }
}

@Composable
fun UserProfile(user: User) {
    Column {
        Text(user.name, style = MaterialTheme.typography.subtitle1, color = DailySetTheme.color.primary)
        Spacer(modifier = Modifier.height(2.dp))
        Text(user.nickName, style = MaterialTheme.typography.body2, color = DailySetTheme.color.sub)
    }
}