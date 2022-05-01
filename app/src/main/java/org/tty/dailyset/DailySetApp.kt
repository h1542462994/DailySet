package org.tty.dailyset

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import org.tty.dailyset.ui.utils.LocalBackDispatcher

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun DailySetApp(backPressedDispatcher: OnBackPressedDispatcher) {
    CompositionLocalProvider(LocalBackDispatcher provides backPressedDispatcher) {
        NavGraph()
    }
}