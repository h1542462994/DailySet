package org.tty.dailyset.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import org.tty.dailyset.LocalNav
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.bean.entity.DailySetCourse
import org.tty.dailyset.bean.entity.DefaultEntities
import org.tty.dailyset.bean.enums.DailySetClazzAutoViewType
import org.tty.dailyset.bean.lifetime.*
import org.tty.dailyset.bean.lifetime.DailySetClazzAutoPageInfo.Companion.toPageInfoPeriods
import org.tty.dailyset.common.datetime.DateSpan
import org.tty.dailyset.component.common.*
import org.tty.dailyset.component.dailyset.clazzauto.DailySetClazzAutoVM
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
    val dailySetCurrentPageIndex by dailySetClazzAutoVM.dailySetCurrentPageIndex.collectAsState()
    val dailySetClazzAutoPageInfos by dailySetClazzAutoVM.dailySetClazzAutoPageInfos.collectAsState()
    val dailySetTRC by dailySetClazzAutoVM.dailySetTRC.collectAsState()
    val dailySetCourses by dailySetClazzAutoVM.dailySetCourses.collectAsState()
    val dailySetShiftDialogState = dailySetClazzAutoVM.dailySetShiftDialogState

    val nav = LocalNav.current

    fun validPage(): Boolean {
        return dailySetClazzAutoPageInfos.isNotEmpty() && dailySetCurrentPageIndex in dailySetClazzAutoPageInfos.indices
    }

    fun validTRC(): Boolean {
        return dailySetTRC != DefaultEntities.emptyDailySetTRC() && dailySetTRC.dailySetRCs.isNotEmpty()
    }

    var followName = ""
    if (validPage() && dailySetClazzAutoViewTypeState.value == DailySetClazzAutoViewType.Week) {
        val currentPageInfo = dailySetClazzAutoPageInfos[dailySetCurrentPageIndex]
        followName =
            "(${currentPageInfo.year}-${currentPageInfo.year + 1} ${currentPageInfo.periodCode.toDisplayString()})"
    }

    Column {
        TopBar(
            title = {
                Row {
                    Text(dailySetSummary.name)
                    // @Shape: difference by dailySetClazzAutoViewType.
                    if (followName.isNotEmpty()) {
                        Text(followName, style = DailySetTheme.typography.subTitleText)
                    }
                }
            },
            useBack = true,
            onBackPressed = { nav.action.upPress() }
        )
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
                        dailySetClazzAutoPageInfos = dailySetClazzAutoPageInfos,
                        dailySetCurrentPageIndex = dailySetCurrentPageIndex,
                        dailySetCourses = dailySetCourses,
                        dailySetTRC = dailySetTRC,
                        dailySetClazzAutoVM = dailySetClazzAutoVM
                    )
                }
            }
        }
        DailySetClazzAutoBottom(
            dailySetClazzAutoViewTypeState,
            dailySetCurrentPageIndex,
            dailySetClazzAutoPageInfos,
            dailySetClazzAutoVM
        )
    }

    DailySetShiftDialogCover(
        dailySetShiftDialogState = dailySetShiftDialogState,
        dailySetClazzAutoViewType = dailySetClazzAutoViewTypeState.value,
        dailySetPagerInfos = dailySetClazzAutoPageInfos,
        dailySetCurrentPageIndex = dailySetCurrentPageIndex,
        dailySetClazzAutoVM = dailySetClazzAutoVM
    )
}

@Composable
fun DailySetClazzAutoBottom(
    dailySetClazzAutoViewTypeState: MutableState<DailySetClazzAutoViewType>,
    dailySetCurrentPageIndex: Int,
    dailySetClazzAutoPageInfos: List<DailySetClazzAutoPageInfo>,
    dailySetClazzAutoVM: DailySetClazzAutoVM
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

            Row(
                modifier = Modifier
                    .weight(1.0f)
                    .wrapContentSize(align = Alignment.Center)
            ) {
                if (dailySetClazzAutoPageInfos.isNotEmpty()) {
                    DailySetClazzAutoBottom(
                        dailySetCurrentPageIndex = dailySetCurrentPageIndex,
                        dailySetClazzAutoPageInfos = dailySetClazzAutoPageInfos,
                        dailySetClazzAutoViewType = dailySetClazzAutoViewType,
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

    val pagerState = rememberPagerState(initialPage = dailySetCurrentPageIndex)
    HorizontalPager(dailySetClazzAutoPageInfos.size, state = pagerState) { page: Int ->
        val currentPageInfo = dailySetClazzAutoPageInfos[page]
        val dailySetCourseCoverage =
            DailySetCourseCoverage(dailySetCourses, currentPageInfo.serialIndex + 1)

        val dailyTableCalc = DailyTableCalc(
            dailySetTRC = dailySetTRC,
            measuredWidth = measuredWidth,
            unit = unit
        )

        Column {
            val dateSpan = DateSpan(currentPageInfo.startDate, currentPageInfo.endDate)
            DailyTablePreviewHeader(dailyTableCalc = dailyTableCalc, dateSpan = dateSpan)
            DailyTablePreviewBody(
                dailyTableCalc = dailyTableCalc,
                dailySetCourseCoverage = dailySetCourseCoverage
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
    dailySetClazzAutoPageInfos: List<DailySetClazzAutoPageInfo>,
    dailySetCurrentPageIndex: Int,
    dailySetCourses: List<DailySetCourse>,
    dailySetTRC: DailySetTRC,
    dailySetClazzAutoVM: DailySetClazzAutoVM
) {
    val measuredWidth = measuredWindowWidth()
    val unit = toPx(dp = 25.dp)
    val dailySetClazzAutoPeriodPages = dailySetClazzAutoPageInfos.toPageInfoPeriods()
    val dailySetClazzAutoCurrentPage = dailySetClazzAutoPageInfos[dailySetCurrentPageIndex]
    val dailySetClazzAutoPeriodPageIndex = dailySetClazzAutoPeriodPages.indexOfFirst {
        it.year == dailySetClazzAutoCurrentPage.year && it.periodCode == dailySetClazzAutoCurrentPage.periodCode
    }

    val pagerState = rememberPagerState(initialPage = dailySetClazzAutoPeriodPageIndex)
    HorizontalPager(count = dailySetClazzAutoPeriodPages.size, state = pagerState) { page ->
        val currentPageIndex = dailySetClazzAutoPeriodPages[page]
        val dailySetCourseCoverage = DailySetCourseCoverage(dailySetCourses, week = null)

        val dailyTableCalc = DailyTableCalc(
            dailySetTRC,
            measuredWidth,
            unit
        )

        Column {
            DailyTablePreviewHeader(dailyTableCalc = dailyTableCalc, dateSpan = null)
            DailyTablePreviewBody(
                dailyTableCalc = dailyTableCalc,
                dailySetCourseCoverage = dailySetCourseCoverage
            )
        }
    }

    LaunchedEffect(key1 = dailySetClazzAutoPeriodPageIndex, key2 = dailySetClazzAutoPeriodPages) {
        if (pagerState.currentPage != dailySetClazzAutoPeriodPageIndex) {
            pagerState.animateScrollToPage(dailySetClazzAutoPeriodPageIndex)
        }
    }
    LaunchedEffect(key1 = pagerState.currentPage) {
        if (!pagerState.isScrollInProgress) {
            dailySetClazzAutoVM.toIndex(pagerState.currentPage)
        }
    }

}


@Composable
fun DailySetClazzAutoBottom(
    dailySetCurrentPageIndex: Int,
    dailySetClazzAutoPageInfos: List<DailySetClazzAutoPageInfo>,
    dailySetClazzAutoViewType: DailySetClazzAutoViewType,
    dailySetClazzAutoVM: DailySetClazzAutoVM
) {
    val dailySetShiftDialogState = dailySetClazzAutoVM.dailySetShiftDialogState

    Row(
        modifier = Modifier
            .wrapContentHeight(align = Alignment.CenterVertically)
    ) {
        if (dailySetCurrentPageIndex > 0) {
            IconClick(painter = ImageResource.left(), useTint = true) {
                dailySetClazzAutoVM.toPrev()
            }
        } else {
            Spacer(modifier = Modifier.width(56.dp))
        }

        val currentPageInfo = dailySetClazzAutoPageInfos[dailySetCurrentPageIndex]

        var displayString = ""
        displayString = if (dailySetClazzAutoViewType == DailySetClazzAutoViewType.Week) {
            "第${currentPageInfo.serialIndex + 1}周"
        } else {
            "${currentPageInfo.year}-${currentPageInfo.year + 1} ${currentPageInfo.periodCode.toDisplayString()}"
        }

        IconText(
            text = displayString
        ) {
            dailySetShiftDialogState.dialogOpen.value = true
        }

        if (dailySetCurrentPageIndex < dailySetClazzAutoPageInfos.size - 1) {
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
    dailySetShiftDialogState: DialogState,
    dailySetClazzAutoViewType: DailySetClazzAutoViewType,
    dailySetPagerInfos: List<DailySetClazzAutoPageInfo>,
    dailySetCurrentPageIndex: Int,
    dailySetClazzAutoVM: DailySetClazzAutoVM
) {
    BottomDialog(dialogState = dailySetShiftDialogState, autoClose = true) {
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
                    ListSelector(
                        data = yearDisplay,
                        height = 192.dp,
                        width = 96.dp,
                        cellHeight = 48.dp,
                        itemIndex = yearIndex,
                        onItemIndexChanged = { yearIndex = it }
                    )
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = "学年",
                        color = DailySetTheme.color.primary
                    )
                    val periodCodes = dailySetPagerInfos
                        .filter { it.year == years[yearIndex] }
                        .map { it.periodCode }
                        .distinct()
                    val periodCodeDisplay = periodCodes.map { it.toDisplayString() }
                    var periodCodeIndex by remember { mutableStateOf(periodCodes.indexOf(periodCode)) }
                    val checkedPeriodCodeIndex = periodCodeIndex.coerceIn(periodCodes.indices)
                    ListSelector(
                        data = periodCodeDisplay,
                        height = 192.dp,
                        width = 96.dp,
                        cellHeight = 48.dp,
                        itemIndex = checkedPeriodCodeIndex,
                        onItemIndexChanged = {
                            periodCodeIndex = it
                            periodCode = periodCodes[periodCodeIndex]
                        }
                    )
                    val weeks = dailySetPagerInfos
                        .filter { it.year == years[yearIndex] && it.periodCode == periodCodes[checkedPeriodCodeIndex] }
                        .map { it.serialIndex }
                    var weekIndex by remember { mutableStateOf(weeks.indexOf(currentSerialIndex)) }
                    val checkedWeekIndex = weekIndex.coerceIn(weeks.indices)
                    if (dailySetClazzAutoViewType == DailySetClazzAutoViewType.Week) {
                        // if the viewType is week, show week change listSelector
                        ListSelector(
                            data = weeks.map { (it + 1).toString() },
                            height = 192.dp,
                            width = 48.dp,
                            cellHeight = 48.dp,
                            itemIndex = checkedWeekIndex,
                            onItemIndexChanged = { weekIndex = it
                                currentSerialIndex = weeks[weekIndex]
                            }
                        )
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
                            dailySetShiftDialogState.dialogOpen.value = false
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = DailySetTheme.color.background2)
                    ) {
                        Text("跳转到今天")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        modifier = Modifier.weight(1.0f),
                        onClick = {
                            val index: Int = if (dailySetClazzAutoViewType == DailySetClazzAutoViewType.Week) {
                                dailySetPagerInfos.indexOfFirst {
                                    it.year == years[yearIndex]
                                            && it.periodCode == periodCode
                                            && it.serialIndex == currentSerialIndex
                                }
                            } else {
                                dailySetPagerInfos.toPageInfoPeriods()
                                    .indexOfFirst {
                                        it.year == years[yearIndex]
                                                && it.periodCode == periodCode
                                    }
                            }
                            dailySetClazzAutoVM.toIndex(index)
                            dailySetShiftDialogState.dialogOpen.value = false
                        }) {
                        Text("确认")
                    }
                }
            }
        }

    }
}