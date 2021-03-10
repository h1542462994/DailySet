package org.tty.dailyset.ui.page

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import org.tty.dailyset.MainViewModel
import org.tty.dailyset.model.entity.Preference
import org.tty.dailyset.model.entity.PreferenceName
import org.tty.dailyset.provider.LocalMainViewModel
import org.tty.dailyset.provider.LocalServices

@Composable
fun ProfilePage() {
    val seedVersion by LocalMainViewModel.current.seedVersion.observeAsState(Preference.default(PreferenceName.SEED_VERSION))
    Column {

        Text("ProfilePage")
        Text(text = seedVersion?.value ?: "0")
    }

}