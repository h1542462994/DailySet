package org.tty.dailyset.data.scope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import org.tty.dailyset.viewmodel.MainViewModel
import org.tty.dailyset.provider.LocalMainViewModel

@Immutable
interface GlobalScope  {
    @Composable
    fun mainViewModel(): MainViewModel {
        return LocalMainViewModel.current
    }

    //companion object: GlobalScope
}

