package org.tty.dailyset.ui.page

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import org.tty.dailyset.data.processor.ClazzDailySetProcessor
import org.tty.dailyset.data.scope.DataScope
import org.tty.dailyset.event.DailySetCreateDurationAndBindingEventArgs
import org.tty.dailyset.event.DailySetEventType
import org.tty.dailyset.model.entity.*
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailyDurationCreateState
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailySetCursor
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailySetState
import org.tty.dailyset.model.lifetime.dailytable.DailyTableCalc
import org.tty.dailyset.model.lifetime.dailytable.DailyTablePreviewState
import org.tty.dailyset.ui.component.IconText
import org.tty.dailyset.ui.component.TopBar
import org.tty.dailyset.ui.image.ImageResource
import org.tty.dailyset.ui.theme.LocalPalette
import org.tty.dailyset.ui.utils.StatusBarToBackground1
import org.tty.dailyset.ui.utils.measuredWidth
import org.tty.dailyset.ui.utils.toPx
import java.time.LocalDate
import java.util.*

/**
 * clazz dailySet page.
 */

@Composable
fun ClazzDailySetPage() {
    StatusBarToBackground1()

    with(DataScope) {
        val mainViewModel = mainViewModel()
        val service = mainViewModel.service
        //val dailySetDurations by currentDailySetDurations()
        val dailyTableSummaries by dailyTableSummaries()
        val userState by currentUserState()
        val clazzDailySetState by currentClazzDailySetState()

        LaunchedEffect(key1 = clazzDailySetState, block = {
            Log.d("ClazzDailySetPage", clazzDailySetState.toString())
        })

        /**
         * not include dailyDurations current.
         */
        val notIncludedDailyDurations by currentNotIncludedDurations(type = DailyDurationType.Clazz)

        LaunchedEffect(key1 = notIncludedDailyDurations, block = {
            Log.d("ClazzDailySetPage", "notIncludedDailyDurations: $notIncludedDailyDurations")
        })

        //region dialogStates
        val clazzDailyDurationCreateState = clazzDailyDurationCreateState()
        //endregion

        //region processor
        val clazzDailySetProcessor = object: ClazzDailySetProcessor {
            override fun onCreateClazzDurationAndBinding(
                name: String,
                uid: String?,
                startDate: LocalDate,
                endDate: LocalDate,
                periodCode: PeriodCode,
                bindingDailyTableUid: String
            ) {
                val uuid = uid ?: UUID.randomUUID().toString()
                performProcess(service, DailySetEventType.CreateDurationAndBinding, DailySetCreateDurationAndBindingEventArgs(
                    dailyDurationUid = uuid,
                    dailySetUid = clazzDailySetState.dailySet.uid,
                    name = name,
                    ownerUid = userState.currentUserUid,
                    startDate = startDate,
                    endDate = endDate,
                    periodCode = periodCode,
                    bindingDailyTableUid = bindingDailyTableUid
                ), onBefore = {

                }, onCompletion = {
                    clazzDailyDurationCreateState.dialogOpen.value = false
                })
            }

            override fun onBindingClazzDuration(
                dailySetUid: String,
                dailyDurationUid: String,
                bindingDailyTableUid: String
            ) {
                TODO("Not yet implemented")
            }

        }
        //endregion

        val nav = LocalNav.current

        Column {
            ClazzDailySetAppBar(dailySet = clazzDailySetState.dailySet, dailyDuration = clazzDailySetState.currentDailyDuration, onBack = nav.action.upPress)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                ClazzDailySetCenter(clazzDailySetState = clazzDailySetState)
            }
            val cursor = clazzDailySetState.cursor
            ClazzDailySetBottom(
                dailyDurations = clazzDailySetState.durations,
                clazzDailyDurationCreateState = clazzDailyDurationCreateState,
                clazzDailySetCursor = cursor
            )
        }

        ClazzDailySetClazzDurationCreateDialogCover(
            clazzDailyDurationCreateState = clazzDailyDurationCreateState,
            notIncludedDailyDurations = notIncludedDailyDurations,
            dailyTableSummaries = dailyTableSummaries,
            userState = userState,
            clazzDailySetProcessor = clazzDailySetProcessor
        )
    }

}

/**
 * clazz dailySetPage .appBar
 */
@Composable
fun ClazzDailySetAppBar(dailySet: DailySet, dailyDuration: DailyDuration?, onBack: () -> Unit) {

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.CenterStart)
            ) {
                Text(
                    text = dailySet.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                if (dailyDuration != null) {
                    Text(
                        text = dailyDuration.name,
                        color = LocalPalette.current.sub,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )

                }
            }


        }
    }

    TopBar(title = { titleArea(dailySet) }, useBack = true, onBackPressed = onBack, onTitleClick = {})
}

@Composable
fun ClazzDailySetCenter(
    clazzDailySetState: ClazzDailySetState
) {
    if (clazzDailySetState.durations.isEmpty()) {
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
        val measuredWidth = measuredWidth()
        val unit = toPx(dp = 25.dp)

        val dailyTableCalc = DailyTableCalc(
            dailyTRC = clazzDailySetState.dailyTableState2.dailyTRC,
            measuredWidth = measuredWidth,
            unit = unit
        )

        val dailyTablePreviewState = clazzDailySetState.previewState

        // TODO: 2021/6/24 完成实现
        DailyTablePreviewHeader(dailyTableCalc = dailyTableCalc, dailyTablePreviewState = dailyTablePreviewState)
        DailyTablePreviewBody(dailyTableCalc = dailyTableCalc, dailyTablePreviewState = dailyTablePreviewState)
    }
}

@Composable
fun ClazzDailySetBottom(
    dailyDurations: List<DailyDuration>,
    clazzDailyDurationCreateState: ClazzDailyDurationCreateState,
    clazzDailySetCursor: ClazzDailySetCursor?
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
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                        .weight(1f)
                ) {
                    ClazzDailySetBottomShiftButton(clazzDailySetCursor!!)
                }
                ClazzDailySetBottomShiftRightAddIcon(
                    clazzDailyDurationCreateState = clazzDailyDurationCreateState
                )
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
fun ClazzDailySetBottomShiftButton(
    clazzDailySetCursor: ClazzDailySetCursor
) {
    IconText(painter = ImageResource.shift(), scale = 0.8f, text = "第${clazzDailySetCursor.index + 1}周") {
        // TODO: 2021/6/25 添加处理逻辑.
    }
}

@Composable
fun ClazzDailySetBottomShiftRightAddIcon(
    clazzDailyDurationCreateState: ClazzDailyDurationCreateState
) {
    BoxWithConstraints(
        modifier = Modifier
            .size(56.dp)
            .wrapContentSize(align = Alignment.Center)
            .clip(shape = CircleShape)
            .clickable { clazzDailyDurationCreateState.dialogOpen.value = true }
    ) {
        Icon(
            painter = ImageResource.add(),
            contentDescription = null,
            modifier = Modifier
                .padding(12.dp)
                .size(18.dp),
            tint = Color.Unspecified
        )
    }
}