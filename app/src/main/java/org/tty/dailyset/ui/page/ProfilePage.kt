package org.tty.dailyset.ui.page

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.model.entity.Preference
import org.tty.dailyset.model.entity.PreferenceName
import org.tty.dailyset.provider.LocalMainViewModel
import org.tty.dailyset.ui.component.ProfileMenuGroup
import org.tty.dailyset.ui.component.ProfileMenuItem

@UseViewModel
@Composable
fun ProfilePage() {
    val mainViewModel = LocalMainViewModel.current
    val seedVersionPreference by mainViewModel.seedVersionPreference.observeAsState(Preference.default(PreferenceName.SEED_VERSION))
    val seedVersion by mainViewModel.seedVersion.observeAsState(0)
    Column {
        ProfileMenuGroup(title = "用户") {
            ProfileMenuItem(icon = Icons.Filled.Phone, title = "123", content = "123", next = true)
        }
        Text("ProfilePage")
        Text(text = seedVersionPreference.toString())
        Text(text = seedVersion.toString())
    }

}