package org.tty.dailyset.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.tty.dailyset.LocalNav
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.bean.enums.DailySetClazzAutoViewType
import org.tty.dailyset.component.common.asMutableState
import org.tty.dailyset.component.dailyset.clazzauto.rememberClazzAutoDailySetVM
import org.tty.dailyset.ui.component.TopBar
import org.tty.dailyset.ui.image.ImageResource
import org.tty.dailyset.ui.theme.DailySetTheme

@Composable
@UseViewModel("/dailyset/clazzauto/:uid")
fun DailySetClazzAutoPage(dailySetUid: String) {

    val clazzAutoDailySetVM = rememberClazzAutoDailySetVM(dailySetUid = dailySetUid)
    val dailySetSummary by clazzAutoDailySetVM.dailySetSummary.collectAsState()
    val dailySetClazzAutoViewTypeState =
        clazzAutoDailySetVM.dailySetClazzAutoViewType.asMutableState()
    val dailySetClazzAutoPageInfos by clazzAutoDailySetVM.dailySetClazzAutoPageInfos.collectAsState()
    val nav = LocalNav.current

    Column {
        TopBar(
            title = dailySetSummary.name,
            useBack = true,
            onBackPressed = { nav.action.upPress() }
        )
        LazyColumn(modifier = Modifier.weight(1.0f)) {
            item {
                Text("hello world! ${dailySetClazzAutoPageInfos.size}")
            }
        }
        DailySetClazzAutoBottom(dailySetClazzAutoViewTypeState)
    }
}

@Composable
fun DailySetClazzAutoBottom(
    dailySetClazzAutoViewTypeState: MutableState<DailySetClazzAutoViewType>
) {
    var dailySetClazzAutoViewType by dailySetClazzAutoViewTypeState
    Surface(
        elevation = BottomNavigationDefaults.Elevation,
        color = DailySetTheme.color.background1
    ) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically)
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .size(56.dp)
                    .wrapContentSize(align = Alignment.Center)
                    .clip(shape = CircleShape)
                    .clickable {
                        dailySetClazzAutoViewType =
                            if (dailySetClazzAutoViewType == DailySetClazzAutoViewType.Term) {
                                DailySetClazzAutoViewType.Week
                            } else {
                                DailySetClazzAutoViewType.Term
                            }
                    }
            ) {
                Icon(
                    painter = ImageResource.shift_round(),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(12.dp)
                        .size(18.dp),
                    tint = if (dailySetClazzAutoViewType == DailySetClazzAutoViewType.Week) {
                        DailySetTheme.color.primary
                    } else {
                        DailySetTheme.color.primaryColor
                    }
                )
            }
        }
    }
}