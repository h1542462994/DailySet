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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.data.processor.ClazzDailySetProcessor
import org.tty.dailyset.data.scope.DataScope
import org.tty.dailyset.event.DailySetCreateDurationAndBindingEventArgs
import org.tty.dailyset.event.DailySetEventType
import org.tty.dailyset.model.entity.*
import org.tty.dailyset.model.lifetime.PagerInfo
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailyDurationCreateState
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailySetState
import org.tty.dailyset.model.lifetime.dailytable.DailyTableCalc
import org.tty.dailyset.ui.component.IconClick
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ClazzDailySetPage() {
    StatusBarToBackground1()

    with(DataScope) {
        val mainViewModel = mainViewModel()
        val service = mainViewModel.service
        val dailyTableSummaries by dailyTableSummaries()
        val userState by currentUserState()
        //val clazzDailySetState by currentClazzDailySetState()
        val pagerInfo by clazzDailySetPagerInfo()

        val clazzDailySetState by clazzDailySetStateOfIndex(index = pagerInfo.pageIndex)


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
                ClazzDailySetCenter(pagerInfo = pagerInfo)
            }
            ClazzDailySetBottom(
                dailyDurations = clazzDailySetState.durations,
                clazzDailyDurationCreateState = clazzDailyDurationCreateState,
                clazzDailySetState = clazzDailySetState
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ClazzDailySetCenter(
     pagerInfo: PagerInfo
) {
    if (pagerInfo.size == 0) {
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

        // cache strategy: the change of the initial page will not due to the recreation of the pagerState.
        val pagerState = rememberPagerState(pageCount = pagerInfo.size, initialPage = pagerInfo.pageIndex)

        // if the pageIndex is changed, you should launched the effect to animate scroll to page.
        LaunchedEffect(key1 = pagerInfo) {
            if (pagerInfo.pageIndex != pagerState.currentPage) {
                pagerState.animateScrollToPage(pagerInfo.pageIndex)
            }
        }

        // if the current page is changed, submit the change to the liveData. this will be transmit to the viewmodel receiver.
        // problem: @snapshot the current page will not be updated if the animation is in progress.
        LaunchedEffect(key1 = pagerState.currentPage) {
           ClazzDailySetState.toIndexedPage(pagerState.currentPage)
        }

        val measuredWidth = measuredWidth()
        val unit = toPx(dp = 25.dp)

        with(DataScope) {
            HorizontalPager(state = pagerState) { page: Int ->
                val currentState by clazzDailySetStateOfIndex(index = page)
                val dailyTableCalc = DailyTableCalc(
                    dailyTRC = currentState.dailyTableState2.dailyTRC,
                    measuredWidth = measuredWidth,
                    unit = unit
                )
                val previewState = currentState.previewState()

                Column {
                    DailyTablePreviewHeader(dailyTableCalc = dailyTableCalc, dailyTablePreviewState = previewState)
                    DailyTablePreviewBody(dailyTableCalc = dailyTableCalc, dailyTablePreviewState = previewState)
                }

            }
        }
    }
}

@Composable
fun ClazzDailySetBottom(
    dailyDurations: List<DailyDuration>,
    clazzDailyDurationCreateState: ClazzDailyDurationCreateState,
    clazzDailySetState: ClazzDailySetState
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
                    ClazzDailySetBottomShiftButton(clazzDailySetState)
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
    clazzDailySetState: ClazzDailySetState
) {
    Row(
        modifier = Modifier
            .wrapContentHeight(align = Alignment.CenterVertically)
    ) {
        if (clazzDailySetState.hasPrevCursor) {
            IconClick(painter = ImageResource.left(), useTint = true) {
                clazzDailySetState.toPrevCursor()
            }
        } else {
            Spacer(modifier = Modifier.width(56.dp))
        }

        IconText(painter = ImageResource.shift(), scale = 0.8f, text = "第${clazzDailySetState.cursor.index + 1}周") {
            // TODO: 2021/6/25 添加处理逻辑.
        }

        if (clazzDailySetState.hasNextCursor) {
            IconClick(painter = ImageResource.right(), useTint = true) {
                clazzDailySetState.toNextCursor()
            }
        } else {
            Spacer(modifier = Modifier.width(56.dp))
        }

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