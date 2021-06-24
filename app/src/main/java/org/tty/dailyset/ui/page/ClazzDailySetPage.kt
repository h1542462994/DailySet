package org.tty.dailyset.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tty.dailyset.LocalNav
import org.tty.dailyset.data.scope.DataScope
import org.tty.dailyset.model.entity.DailySet
import org.tty.dailyset.model.entity.toImageResource
import org.tty.dailyset.ui.component.TopBar
import org.tty.dailyset.ui.theme.LocalPalette

/**
 * clazz dailySet page.
 */

@Composable
fun ClazzDailySetPage() {
    with(DataScope) {
        val mainViewModel = mainViewModel()
        val service = mainViewModel.service
        val currentDailySet by currentDailySet()
        val nav = LocalNav.current

        Column {
            ClazzDailySetAppBar(dailySet = currentDailySet, onBack = nav.action.upPress)
        }

    }

}

@Composable
fun ClazzDailySetAppBar(dailySet: DailySet, onBack: () -> Unit) {

    @Composable
    fun titleArea(dailySet: DailySet) {
        val icon = dailySet.icon.toImageResource()
        val useTint = dailySet.icon == null

        Row(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(align = Alignment.CenterVertically)
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.padding(8.dp),
                tint = if (useTint) {
                    LocalPalette.current.primary
                } else {
                    Color.Unspecified
                }
            )
            Text(
                text = dailySet.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(alignment = Alignment.CenterVertically)
            )
        }
    }

    TopBar(title = { titleArea(dailySet) }, useBack = true, onBackPressed = onBack, onTitleClick = {})
}