package org.tty.dailyset

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import org.tty.dailyset.provider.DailySetApplication
import org.tty.dailyset.provider.LocalServiceProvider
import org.tty.dailyset.provider.LocalServices
import org.tty.dailyset.ui.page.MainPage
import org.tty.dailyset.ui.theme.DailySetTheme
import org.tty.dailyset.ui.utils.LocalBackDispatcher

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as DailySetApplication))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val seedVersion = mainViewModel.seedVersion.observeAsState()
            DailySetTheme {
                // enable the service.
                LocalServiceProvider(application = application, mainViewModel = mainViewModel) {
                    //MainPage()
                    DailySetApp(onBackPressedDispatcher)
                }
            }
        }
    }
}

