package org.tty.dailyset.ui.page

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import org.tty.dailyset.model.entity.Preference
import org.tty.dailyset.model.entity.PreferenceName
import org.tty.dailyset.provider.LocalMainViewModel

@Composable
fun ProfilePage() {
    val seedVersionPreference by LocalMainViewModel.current.seedVersionPreference.observeAsState(Preference.default(PreferenceName.SEED_VERSION))
    val seedVersion by LocalMainViewModel.current.seedVersion.observeAsState(0)
    Column {

        Text("ProfilePage")
        Text(text = seedVersionPreference.toString())
        Text(text = seedVersion.toString())
    }

}