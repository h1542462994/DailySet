package org.tty.dailyset.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.data.scope.DataScope
import org.tty.dailyset.model.entity.DailyDuration
import org.tty.dailyset.model.entity.DailySet
import org.tty.dailyset.model.entity.toImageResource
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailyDurationCreateState
import org.tty.dailyset.ui.component.IconText
import org.tty.dailyset.ui.component.TopBar
import org.tty.dailyset.ui.image.ImageResource
import org.tty.dailyset.ui.theme.LocalPalette
import org.tty.dailyset.ui.utils.StatusBarToBackground1

/**
 * clazz dailySet page.
 */

@Composable
fun ClazzDailySetPage() {
    StatusBarToBackground1()

    with(DataScope) {
        val mainViewModel = mainViewModel()
        val service = mainViewModel.service
        val dailySetDurations by currentDailySetDurations()

        //region dialogStates
        val clazzDailyDurationCreateState = clazzDailyDurationCreateState()
        //endregion

        val nav = LocalNav.current

        Column {
            ClazzDailySetAppBar(dailySet = dailySetDurations.dailySet, onBack = nav.action.upPress)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                ClazzDailySetCenter(dailyDurations = dailySetDurations.durations)
            }
            ClazzDailySetBottom(
                dailyDurations = dailySetDurations.durations,
                clazzDailyDurationCreateState = clazzDailyDurationCreateState,
            )
        }

        ClazzDailySetClazzDurationCreateDialogCover(
            clazzDailyDurationCreateState = clazzDailyDurationCreateState
        )
    }

}

/**
 * clazz dailySetPage .appBar
 */
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

@Composable
fun ClazzDailySetCenter(dailyDurations: List<DailyDuration>) {
    if (dailyDurations.isEmpty()) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.Center)
        ) {
            Icon(
                painter = ImageResource.blank(),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    } else {
        // TODO: 2021/6/24 完成实现
        Text("hello world")
    }
}

@Composable
fun ClazzDailySetBottom(
    dailyDurations: List<DailyDuration>,
    clazzDailyDurationCreateState: ClazzDailyDurationCreateState
) {
    Surface(
        elevation = BottomNavigationDefaults.Elevation,
        color = LocalPalette.current.background1
    ) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically)
        ) {
            if (dailyDurations.isNotEmpty()) {
                BoxWithConstraints(
                    modifier = Modifier
                        .size(56.dp)
                        .wrapContentSize(align = Alignment.Center)
                        .clip(shape = CircleShape)
                        .clickable { }
                ) {
                    Icon(
                        painter = ImageResource.shift_round(),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(12.dp)
                            .size(18.dp),
                        tint = LocalPalette.current.primary
                    )
                }
                BoxWithConstraints(
                    modifier = Modifier
                        .padding(end = 56.dp)
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                        .weight(1f)
                ) {
                    ClazzDailySetBottomShiftButton()
                }
            } else {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                ) {
                    ClazzDailySetBottomAddButton(
                        clazzDailyDurationCreateState = clazzDailyDurationCreateState
                    )
                }
            }
        }
    }

}

@Composable
fun ClazzDailySetBottomAddButton(
    clazzDailyDurationCreateState: ClazzDailyDurationCreateState
) {
    IconText(painter = ImageResource.add(), text = stringResource(id = R.string.dailyset_add_term)) {
        // open the dialog
        clazzDailyDurationCreateState.dialogOpen.value = true
    }
}

@Composable
fun ClazzDailySetBottomShiftButton() {
    IconText(painter = ImageResource.shift(), scale = 0.8f, text = "第8周") {
        // TODO: 2021/6/25 添加处理逻辑.
    }
}