package org.tty.dailyset

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.graphics.toArgb
import org.tty.dailyset.provider.LocalServiceProvider
import org.tty.dailyset.ui.theme.DailySetTheme
import org.tty.dailyset.ui.theme.LocalPalette
import org.tty.dailyset.viewmodel.MainViewModel
import org.tty.dailyset.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as DailySetApplication))
    }

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DailySetTheme {

                window.statusBarColor = LocalPalette.current.background1.toArgb()

                // enable the service.
                LocalServiceProvider(application = application, mainViewModel = mainViewModel, window = window) {

                    //MainPage()
                    DailySetApp(onBackPressedDispatcher)
                }
            }
        }

        mainViewModel.init()
        //ComponentViewModel provides mainViewModel

    }
}

