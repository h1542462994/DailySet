package org.tty.dailyset

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import org.tty.dailyset.ui.page.MainPage
import org.tty.dailyset.ui.theme.DailySetTheme
import org.tty.dailyset.ui.utils.LocalBackDispatcher

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            DailySetTheme {
                //MainPage()
                DailySetApp(onBackPressedDispatcher)

            }
        }
    }
}

