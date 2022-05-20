package org.tty.dailyset.ui.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.bean.entity.DailySetCourse
import org.tty.dailyset.bean.entity.DefaultEntities
import org.tty.dailyset.bean.enums.DailySetClazzAutoViewType
import org.tty.dailyset.bean.enums.DailySetIcon
import org.tty.dailyset.bean.enums.toImageResource
import org.tty.dailyset.bean.lifetime.*
import org.tty.dailyset.bean.lifetime.DailySetClazzAutoPageInfo.Companion.toPageInfoPeriods
import org.tty.dailyset.common.datetime.DateSpan
import org.tty.dailyset.component.common.DialogVM
import org.tty.dailyset.component.common.asMutableState
import org.tty.dailyset.component.common.measuredWindowWidth
import org.tty.dailyset.component.common.toPx
import org.tty.dailyset.component.dailyset.clazzauto.DailySetClazzAutoVM
import org.tty.dailyset.component.dailyset.clazzauto.DailySetRenameDialogVM
import org.tty.dailyset.component.dailyset.clazzauto.rememberClazzAutoDailySetVM
import org.tty.dailyset.ui.component.*
import org.tty.dailyset.ui.image.ImageResource
import org.tty.dailyset.ui.theme.DailySetTheme

@Composable
@UseViewModel("/dailyset/clazzauto/:uid")
fun DailySetClazzAutoPage(dailySetUid: String) {

    val dailySetClazzAutoVM = rememberClazzAutoDailySetVM(dailySetUid = dailySetUid)
    val dailySetSummary by dailySetClazzAutoVM.dailySetSummary.collectAsState()
    val dailySetClazzAutoViewTypeState =
        dailySetClazzAutoVM.dailySetClazzAutoViewType.asMutableState()
    val dailySetClazzAutoPageInfos by dailySetClazzAutoVM.dailySetClazzAutoPageInfos.collectAsState()
    val dailySetCurrentPageIndex by dailySetClazzAutoVM.dailySetCurrentPageIndex.collectAsState()
    val dailySetClazzAutoPageInfosPeriod by dailySetClazzAutoVM.dailySetClazzAutoPageInfosPeriod.collectAsState()
    val dailySetCurrentPageIndexPeriod by dailySetClazzAutoVM.dailySetCurrentPageIndexPeriod.collectAsState()
    val dailySetTRC by dailySetClazzAutoVM.dailySetTRC.collectAsState()
    val dailySetCourses by dailySetClazzAutoVM.dailySetCourses.collectAsState()
    val dailySetShiftDialogVM = dailySetClazzAutoVM.dailySetShiftDialogVM
    val dailySetRenameDialogVM = dailySetClazzAutoVM.dailySetRenameDialogVM

    fun validPage(): Boolean {
        return dailySetClazzAutoPageInfos.isNotEmpty() && dailySetCurrentPageIndex in dailySetClazzAutoPageInfos.indices
    }

    fun validTRC(): Boolean {
        return dailySetTRC != DefaultEntities.emptyDailySetTRC() && dailySetTRC.dailySetRCs.isNotEmpty()
    }

    Column {
        if (validPage()) {
            val currentPageInfo = dailySetClazzAutoPageInfos[dailySetCurrentPageIndex]
            DailySetClazzAutoTopBar(
                dailySetClazzAutoViewType = dailySetClazzAutoViewTypeState.value,
                dailySetSummary = dailySetSummary,
                currentPageInfo = currentPageInfo,
                dailySetClazzAutoVM = dailySetClazzAutoVM
            )
        }

        Column(
            modifier = Modifier
                .weight(1.0f)
                .fillMaxWidth()
        ) {
            if (validPage() && validTRC()) {
                if (dailySetClazzAutoViewTypeState.value == DailySetClazzAutoViewType.Week) {
                    DailySetClazzAutoCenterWeek(
                        dailySetClazzAutoPageInfos = dailySetClazzAutoPageInfos,
                        dailySetCurrentPageIndex = dailySetCurrentPageIndex,
                        dailySetCourses = dailySetCourses,
                        dailySetTRC = dailySetTRC,
                        dailySetClazzAutoVM = dailySetClazzAutoVM
                    )
                } else {
                    DailySetClazzAutoCenterPeriod(
                        dailySetClazzAutoPageInfosPeriod = dailySetClazzAutoPageInfosPeriod,
                        dailySetCurrentPageIndexPeriod = dailySetCurrentPageIndexPeriod,
                        dailySetCourses = dailySetCourses,
                        dailySetTRC = dailySetTRC,
                        dailySetClazzAutoVM = dailySetClazzAutoVM
                    )
                }
            }
        }
        DailySetClazzAutoBottom(
            dailySetClazzAutoViewTypeState = dailySetClazzAutoViewTypeState,
            dailySetClazzAutoPageInfos = dailySetClazzAutoPageInfos,
            dailySetCurrentPageIndex = dailySetCurrentPageIndex,
            dailySetClazzAutoPageInfosPeriod = dailySetClazzAutoPageInfosPeriod,
            dailySetCurrentPageIndexPeriod = dailySetCurrentPageIndexPeriod,
            dailySetClazzAutoVM = dailySetClazzAutoVM
        )
    }

    DailySetShiftDialogCover(
        dailySetClazzAutoViewType = dailySetClazzAutoViewTypeState.value,
        dailySetPagerInfos = dailySetClazzAutoPageInfos,
        dailySetCurrentPageIndex = dailySetCurrentPageIndex,
        dailySetShiftDialogVM = dailySetShiftDialogVM,
        dailySetClazzAutoVM = dailySetClazzAutoVM
    )

    DailySetRenameDialogCover(
        dailySetRenameDialogVM = dailySetRenameDialogVM
    )
}

@Composable
fun DailySetClazzAutoTopBar(
    dailySetClazzAutoViewType: DailySetClazzAutoViewType,
    dailySetSummary: DailySetSummary,
    currentPageInfo: DailySetClazzAutoPageInfo,
    dailySetClazzAutoVM: DailySetClazzAutoVM
) {
    val nav = LocalNav.current
    TopBar(title = {
        Row {
            IconText(
                painter = dailySetSummary.icon.toImageResource(),
                text = dailySetSummary.name,
                useTint = dailySetSummary.icon == null
            )
            // @Shape: difference by dailySetClazzAutoViewType.
            if (dailySetClazzAutoViewType == DailySetClazzAutoViewType.Week) {
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = currentPageInfo.toDisplayString(),
                    modifier = Modifier.align(Alignment.CenterVertically),
                    style = DailySetTheme.typography.subTitleText
                )
            }
        }
    }, useBack = true, onBackPressed = { nav.action.upPress() }, onTitleClick = {
        dailySetClazzAutoVM.openRenameDialog()
    })
}

@Composable
fun DailySetClazzAutoBottom(
    dailySetClazzAutoViewTypeState: MutableState<DailySetClazzAutoViewType>,
    dailySetClazzAutoPageInfos: List<DailySetClazzAutoPageInfo>,
    dailySetCurrentPageIndex: Int,
    dailySetClazzAutoPageInfosPeriod: List<DailySetClazzAutoPageInfoPeriod>,
    dailySetCurrentPageIndexPeriod: Int,
    dailySetClazzAutoVM: DailySetClazzAutoVM
) {
    var dailySetClazzAutoViewType by dailySetClazzAutoViewTypeState
    Surface(
        elevation = BottomNavigationDefaults.Elevation, color = DailySetTheme.color.background1
    ) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically)
        ) {
            BoxWithConstraints(modifier = Modifier
                .size(56.dp)
                .wrapContentSize(align = Alignment.Center)
                .clip(shape = CircleShape)
                .clickable {
                    dailySetClazzAutoViewType =
                        if (dailySetClazzAutoViewType == DailySetClazzAutoViewType.Period) {
                            DailySetClazzAutoViewType.Week
                        } else {
                            DailySetClazzAutoViewType.Period
                        }
                }) {
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

            Row(
                modifier = Modifier
                    .weight(1.0f)
                    .wrapContentSize(align = Alignment.Center)
            ) {
                if (dailySetClazzAutoPageInfos.isNotEmpty()) {
                    DailySetClazzAutoBottomCenter(
                        dailySetClazzAutoPageInfos = dailySetClazzAutoPageInfos,
                        dailySetCurrentPageIndex = dailySetCurrentPageIndex,
                        dailySetClazzAutoPageInfosPeriod = dailySetClazzAutoPageInfosPeriod,
                        dailySetCurrentPageIndexPeriod = dailySetCurrentPageIndexPeriod,
                        dailySetClazzAutoViewType = dailySetClazzAutoViewType,
                        dailySetShiftDialogVM = dailySetClazzAutoVM.dailySetShiftDialogVM,
                        dailySetClazzAutoVM = dailySetClazzAutoVM
                    )
                }
            }

            Row(modifier = Modifier.size(56.dp)) {

            }
        }


    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun DailySetClazzAutoCenterWeek(
    dailySetClazzAutoPageInfos: List<DailySetClazzAutoPageInfo>,
    dailySetCurrentPageIndex: Int,
    dailySetCourses: List<DailySetCourse>,
    dailySetTRC: DailySetTRC,
    dailySetClazzAutoVM: DailySetClazzAutoVM
) {
    val measuredWidth = measuredWindowWidth()
    val unit = toPx(dp = 25.dp)
    val nowDate by dailySetClazzAutoVM.nowDate.collectAsState()
    val currentDayOfWeekState = dailySetClazzAutoVM.selectDayOfWeek.asMutableState()

    val pagerState = rememberPagerState(initialPage = dailySetCurrentPageIndex)
    HorizontalPager(dailySetClazzAutoPageInfos.size, state = pagerState) { page: Int ->
        val currentPageInfo = dailySetClazzAutoPageInfos[page]
        val dailySetCourseCoverage =
            DailySetCourseCoverage(dailySetCourses, currentPageInfo.serialIndex + 1)

        val dailyTableCalc = DailyTableCalc(
            dailySetTRC = dailySetTRC, measuredWidth = measuredWidth, unit = unit
        )

        Column {
            val dateSpan = DateSpan(currentPageInfo.startDate, currentPageInfo.endDate)
            DailyTablePreviewHeader(
                dailyTableCalc = dailyTableCalc,
                dateSpan = dateSpan,
                nowDate = nowDate,
                currentDayOfWeekState = currentDayOfWeekState
            )
            DailyTablePreviewBody(
                dailyTableCalc = dailyTableCalc,
                dailySetCourseCoverage = dailySetCourseCoverage,
                currentDayOfWeek = currentDayOfWeekState.value
            )
        }
    }
    LaunchedEffect(key1 = dailySetCurrentPageIndex, key2 = dailySetClazzAutoPageInfos) {
        if (pagerState.currentPage != dailySetCurrentPageIndex) {
            pagerState.animateScrollToPage(dailySetCurrentPageIndex)
        }
    }
    LaunchedEffect(key1 = pagerState.currentPage) {
        if (!pagerState.isScrollInProgress) {
            dailySetClazzAutoVM.toIndex(pagerState.currentPage)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DailySetClazzAutoCenterPeriod(
    dailySetClazzAutoPageInfosPeriod: List<DailySetClazzAutoPageInfoPeriod>,
    dailySetCurrentPageIndexPeriod: Int,
    dailySetCourses: List<DailySetCourse>,
    dailySetTRC: DailySetTRC,
    dailySetClazzAutoVM: DailySetClazzAutoVM
) {
    val measuredWidth = measuredWindowWidth()
    val unit = toPx(dp = 25.dp)
    val nowDate by dailySetClazzAutoVM.nowDate.collectAsState()
    val currentDayOfWeekState = dailySetClazzAutoVM.selectDayOfWeek.asMutableState()

    val pagerState = rememberPagerState(initialPage = dailySetCurrentPageIndexPeriod)
    HorizontalPager(count = dailySetClazzAutoPageInfosPeriod.size, state = pagerState) { page ->
        val dailySetCourseCoverage = DailySetCourseCoverage(dailySetCourses, week = null)

        val dailyTableCalc = DailyTableCalc(
            dailySetTRC, measuredWidth, unit
        )

        Column {
            DailyTablePreviewHeader(
                dailyTableCalc = dailyTableCalc,
                dateSpan = null,
                nowDate = nowDate,
                currentDayOfWeekState = currentDayOfWeekState
            )
            DailyTablePreviewBody(
                dailyTableCalc = dailyTableCalc,
                dailySetCourseCoverage = dailySetCourseCoverage,
                currentDayOfWeek = currentDayOfWeekState.value
            )
        }
    }

    LaunchedEffect(key1 = dailySetCurrentPageIndexPeriod, key2 = dailySetClazzAutoPageInfosPeriod) {
        if (pagerState.currentPage != dailySetCurrentPageIndexPeriod) {
            pagerState.animateScrollToPage(dailySetCurrentPageIndexPeriod)
        }
    }
    LaunchedEffect(key1 = pagerState.currentPage) {
        if (!pagerState.isScrollInProgress) {
            dailySetClazzAutoVM.toIndex(pagerState.currentPage)
        }
    }

}


@Composable
fun DailySetClazzAutoBottomCenter(
    dailySetClazzAutoPageInfos: List<DailySetClazzAutoPageInfo>,
    dailySetCurrentPageIndex: Int,
    dailySetClazzAutoPageInfosPeriod: List<DailySetClazzAutoPageInfoPeriod>,
    dailySetCurrentPageIndexPeriod: Int,
    dailySetClazzAutoViewType: DailySetClazzAutoViewType,
    dailySetShiftDialogVM: DialogVM,
    dailySetClazzAutoVM: DailySetClazzAutoVM
) {
    val dailySetShiftDialogState = dailySetShiftDialogVM.dialogOpen.asMutableState()
    val hasPrev by derivedStateOf {
        if (dailySetClazzAutoViewType == DailySetClazzAutoViewType.Week) {
            dailySetCurrentPageIndex > 0
        } else {
            dailySetCurrentPageIndexPeriod > 0
        }
    }
    val hasNext by derivedStateOf {
        if (dailySetClazzAutoViewType == DailySetClazzAutoViewType.Week) {
            dailySetCurrentPageIndex < dailySetClazzAutoPageInfos.size - 1
        } else {
            dailySetCurrentPageIndexPeriod < dailySetClazzAutoPageInfosPeriod.size - 1
        }
    }

    Row(
        modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically)
    ) {
        if (hasPrev) {
            IconClick(painter = ImageResource.left(), useTint = true) {
                dailySetClazzAutoVM.toPrev()
            }
        } else {
            Spacer(modifier = Modifier.width(56.dp))
        }

        val currentPageInfo = dailySetClazzAutoPageInfos[dailySetCurrentPageIndex]

        var displayString = ""
        displayString = if (dailySetClazzAutoViewType == DailySetClazzAutoViewType.Week) {
            stringResource(
                R.string.dailyset_clazz_week_1, (currentPageInfo.serialIndex + 1).toString()
            )
        } else {
            currentPageInfo.toDisplayString()
        }

        IconText(
            text = displayString
        ) {
            dailySetShiftDialogState.value = true
        }

        if (hasNext) {
            IconClick(painter = ImageResource.right(), useTint = true) {
                dailySetClazzAutoVM.toNext()
            }
        } else {
            Spacer(modifier = Modifier.width(56.dp))
        }

    }

}

@Composable
fun DailySetShiftDialogCover(
    dailySetClazzAutoViewType: DailySetClazzAutoViewType,
    dailySetPagerInfos: List<DailySetClazzAutoPageInfo>,
    dailySetCurrentPageIndex: Int,
    dailySetShiftDialogVM: DialogVM,
    dailySetClazzAutoVM: DailySetClazzAutoVM
) {
    val dailySetShiftDialogState = dailySetShiftDialogVM.dialogOpen.asMutableState()
    BottomDialog(dialogVM = dailySetShiftDialogVM, autoClose = true) {
        if (dailySetPagerInfos.isNotEmpty() && dailySetCurrentPageIndex in dailySetPagerInfos.indices) {
            val years = dailySetPagerInfos.map { it.year }.distinct()
            val yearDisplay = years.map { year -> "${year}-${year + 1}" }
            val currentPagerInfo = dailySetPagerInfos[dailySetCurrentPageIndex]
            var yearIndex by remember { mutableStateOf(years.indexOf(currentPagerInfo.year)) }
            var periodCode by remember { mutableStateOf(currentPagerInfo.periodCode) }
            var currentSerialIndex by remember { mutableStateOf(currentPagerInfo.serialIndex) }
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(align = Alignment.Center)
                ) {
                    ListSelector(data = yearDisplay,
                        height = 192.dp,
                        width = 96.dp,
                        cellHeight = 48.dp,
                        itemIndex = yearIndex,
                        onItemIndexChanged = { yearIndex = it })
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = "学年",
                        color = DailySetTheme.color.primary
                    )
                    val periodCodes = dailySetPagerInfos.filter { it.year == years[yearIndex] }
                        .map { it.periodCode }.distinct()
                    val periodCodeDisplay = periodCodes.map { it.toDisplayString() }
                    var periodCodeIndex by remember { mutableStateOf(periodCodes.indexOf(periodCode)) }
                    val checkedPeriodCodeIndex = periodCodeIndex.coerceIn(periodCodes.indices)
                    ListSelector(data = periodCodeDisplay,
                        height = 192.dp,
                        width = 96.dp,
                        cellHeight = 48.dp,
                        itemIndex = checkedPeriodCodeIndex,
                        onItemIndexChanged = {
                            periodCodeIndex = it
                            periodCode = periodCodes[periodCodeIndex]
                        })
                    val weeks =
                        dailySetPagerInfos.filter { it.year == years[yearIndex] && it.periodCode == periodCodes[checkedPeriodCodeIndex] }
                            .map { it.serialIndex }
                    var weekIndex by remember { mutableStateOf(weeks.indexOf(currentSerialIndex)) }
                    val checkedWeekIndex = weekIndex.coerceIn(weeks.indices)
                    if (dailySetClazzAutoViewType == DailySetClazzAutoViewType.Week) {
                        // if the viewType is week, show week change listSelector
                        ListSelector(data = weeks.map { (it + 1).toString() },
                            height = 192.dp,
                            width = 48.dp,
                            cellHeight = 48.dp,
                            itemIndex = checkedWeekIndex,
                            onItemIndexChanged = {
                                weekIndex = it
                                currentSerialIndex = weeks[weekIndex]
                            })
                        Text(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            text = "周",
                            color = DailySetTheme.color.primary
                        )
                    }

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1.0f),
                        onClick = {
                            dailySetClazzAutoVM.toNow()
                            dailySetShiftDialogState.value = false
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = DailySetTheme.color.background2)
                    ) {
                        Text("跳转到今天")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(modifier = Modifier.weight(1.0f), onClick = {
                        val index: Int =
                            if (dailySetClazzAutoViewType == DailySetClazzAutoViewType.Week) {
                                dailySetPagerInfos.indexOfFirst {
                                    it.year == years[yearIndex] && it.periodCode == periodCode && it.serialIndex == currentSerialIndex
                                }
                            } else {
                                dailySetPagerInfos.toPageInfoPeriods().indexOfFirst {
                                    it.year == years[yearIndex] && it.periodCode == periodCode
                                }
                            }
                        dailySetClazzAutoVM.toIndex(index)
                        dailySetShiftDialogState.value = false
                    }) {
                        Text("确认")
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DailySetRenameDialogCover(
    dailySetRenameDialogVM: DailySetRenameDialogVM
) {
    var selectIcon by dailySetRenameDialogVM.selectIcon.asMutableState()
    var icon by dailySetRenameDialogVM.icon.asMutableState()
    var name by dailySetRenameDialogVM.name.asMutableState()

    NanoDialog(
        title = stringResource(R.string.dailyset_clazz_auto_rename),
        dialogVM = dailySetRenameDialogVM
    ) {
        Row {
            val painter = icon?.toImageResource() ?: ImageResource.set_add_emoji()

            IconClick(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                painter = painter,
                useTint = icon == null
            ) {
                selectIcon = !selectIcon
            }

            OutlinedTextField(value = name,
                modifier = Modifier
                    .weight(1.0f)
                    .align(Alignment.CenterVertically),
                onValueChange = { name = it },
                label = { Text(stringResource(id = R.string.dailyset_list_name_clazz_auto)) })
        }

        if (selectIcon) {
            // icon change area.
            BoxWithConstraints(
                modifier = Modifier.padding(8.dp)
            ) {
                // show selectIcon area.
                LazyVerticalGrid(cells = GridCells.Fixed(6)) {
                    items(DailySetIcon.values()) {
                        BoxWithConstraints(
                            modifier = Modifier
                                .clickable { icon = it }
                                .padding(8.dp)
                                .wrapContentSize(align = Alignment.Center)
                        ) {
                            Icon(
                                painter = it.toImageResource(),
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    }
                }
            }
            NanoDialogButton(text = stringResource(R.string.dailyset_delete_emoji)) {
                icon = null
            }
        } else {

        }
    }
}