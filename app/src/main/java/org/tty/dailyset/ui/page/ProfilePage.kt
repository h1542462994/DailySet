package org.tty.dailyset.ui.page

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.model.entity.Preference
import org.tty.dailyset.model.entity.PreferenceName
import org.tty.dailyset.model.entity.User
import org.tty.dailyset.provider.LocalMainViewModel
import org.tty.dailyset.ui.component.ProfileMenuGroup
import org.tty.dailyset.ui.component.ProfileMenuItem


@UseViewModel
@Composable
fun ProfilePage() {
    val mainViewModel = LocalMainViewModel.current
    val seedVersionPreference by mainViewModel.seedVersionPreference.observeAsState(Preference.default(PreferenceName.SEED_VERSION))
    val seedVersion by mainViewModel.seedVersion.observeAsState(0)
    val user by mainViewModel.currentUser.observeAsState(User.default())
    Column {
        ProfileMenuGroupUser(user = user)
        Text("ProfilePage")
        Text(text = seedVersionPreference.toString())
        Text(text = seedVersion.toString())
    }

}

@Composable
fun ProfileMenuGroupUser(user: User?) {
    ProfileMenuGroup(title = "用户设置") {
        ProfileMenuItem(icon = Icons.Filled.Person, title = "用户", content = user?.name?:"(null)")
    }
}