package org.tty.dailyset

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.newCoroutineContext
import org.tty.dailyset.component.common.DailySetApplication
import org.tty.dailyset.provider.LocalServiceProvider
import org.tty.dailyset.ui.theme.DailySetTheme
import org.tty.dailyset.ui.theme.LocalPalette
import org.tty.dailyset.component.common.MainViewModel
import org.tty.dailyset.component.common.MainViewModelFactory
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.provider.LocalSharedComponents

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as SharedComponents))
    }

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedComponents = application as SharedComponents

        super.onCreate(savedInstanceState)

        LocalSharedComponents.provides(sharedComponents)

        setContent {
            DailySetTheme {

                window.statusBarColor = LocalPalette.current.background1.toArgb()

                // enable the service.
                LocalServiceProvider(sharedComponents = sharedComponents, mainViewModel = mainViewModel, window = window) {
                    //MainPage()
                    DailySetApp(onBackPressedDispatcher)
                }
            }
        }




        mainViewModel.init()
        //ComponentViewModel provides mainViewModel
        sharedComponents.useActivityScope {
            this.lifecycleScope
        }
        sharedComponents.useLifecycle { this.lifecycle }
        sharedComponents.useActivityContext { this }
        sharedComponents.useWindow { window }
    }
}

