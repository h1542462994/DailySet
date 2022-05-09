package org.tty.dailyset.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import org.tty.dailyset.LocalNav
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.bean.entity.DailySetCourse
import org.tty.dailyset.bean.entity.DefaultEntities
import org.tty.dailyset.bean.enums.DailySetClazzAutoViewType
import org.tty.dailyset.bean.lifetime.DailySetClazzAutoPageInfo
import org.tty.dailyset.bean.lifetime.DailySetCourseCoverage
import org.tty.dailyset.bean.lifetime.DailySetTRC
import org.tty.dailyset.bean.lifetime.DailyTableCalc
import org.tty.dailyset.common.datetime.DateSpan
import org.tty.dailyset.component.common.asMutableState
import org.tty.dailyset.component.common.measuredWidth
import org.tty.dailyset.component.common.toPx
import org.tty.dailyset.component.dailyset.clazzauto.DailySetClazzAutoVM
import org.tty.dailyset.component.dailyset.clazzauto.rememberClazzAutoDailySetVM
import org.tty.dailyset.ui.component.IconClick
import org.tty.dailyset.ui.component.IconText
import org.tty.dailyset.ui.component.TopBar
import org.tty.dailyset.ui.image.ImageResource
import org.tty.dailyset.ui.theme.DailySetTheme
import kotlin.math.absoluteValue

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

    val nav = LocalNav.current

    var followName = ""
    if (dailySetClazzAutoPageInfos.isNotEmpty()) {
        val currentPageInfo = dailySetClazzAutoPageInfos[dailySetCurrentPageIndex]
        followName = "(${currentPageInfo.year}-${currentPageInfo.year + 1} ${currentPageInfo.periodCode.toDisplayString()})"
    }

    Column {
        TopBar(
            title = {
                Row {
                    Text(dailySetSummary.name)
                    Text(followName, style = DailySetTheme.typography.subTitleText)
                }
            },
            useBack = true,
            onBackPressed = { nav.action.upPress() }
        )
        Column(
            modifier = Modifier.weight(1.0f)
        ) {
            DailySetClazzAutoCenter(
                dailySetCurrentPageIndex = dailySetCurrentPageIndex,
                dailySetClazzAutoPageInfos = dailySetClazzAutoPageInfos,
                dailySetTRC = dailySetTRC,
                dailySetCourses = dailySetCourses,
                dailySetClazzAutoVM = dailySetClazzAutoVM
            )
        }
        DailySetClazzAutoBottom(
            dailySetClazzAutoViewTypeState,
            dailySetCurrentPageIndex,
            dailySetClazzAutoPageInfos,
            dailySetClazzAutoVM
        )
    }
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
fun DailySetClazzAutoCenter(
    dailySetCurrentPageIndex: Int,
    dailySetClazzAutoPageInfos: List<DailySetClazzAutoPageInfo>,
    dailySetTRC: DailySetTRC,
    dailySetCourses: List<DailySetCourse>,
    dailySetClazzAutoVM: DailySetClazzAutoVM
) {
    if (dailySetClazzAutoPageInfos.isNotEmpty()) {
        val pagerState = rememberPagerState(initialPage = dailySetCurrentPageIndex)

        val measuredWidth = measuredWidth()
        val unit = toPx(dp = 25.dp)

        HorizontalPager(dailySetClazzAutoPageInfos.size, state = pagerState) { page: Int ->
            val currentPageInfo = dailySetClazzAutoPageInfos[page]
            val dailySetCourseCoverage = DailySetCourseCoverage(dailySetCourses, currentPageInfo.serialIndex + 1)

            if (dailySetTRC != DefaultEntities.emptyDailySetTRC()) {
                val dailyTableCalc = DailyTableCalc(
                    dailySetTRC = dailySetTRC,
                    measuredWidth = measuredWidth,
                    unit = unit
                )

                Column {
                    val dateSpan = DateSpan(currentPageInfo.startDate, currentPageInfo.endDate)
                    DailyTablePreviewHeader(dailyTableCalc = dailyTableCalc, dataSpan = dateSpan)
                    DailyTablePreviewBody(dailyTableCalc = dailyTableCalc, dailySetCourseCoverage = dailySetCourseCoverage)
                }
            } else {
                Text(text = "empty")
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


}


@Composable
fun DailySetClazzAutoBottom(
    dailySetCurrentPageIndex: Int,
    dailySetClazzAutoPageInfos: List<DailySetClazzAutoPageInfo>,
    dailySetClazzAutoVM: DailySetClazzAutoVM
) {
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
        IconText(
            painter = ImageResource.shift(),
            scale = 0.8f,
            text = "第${currentPageInfo.serialIndex + 1}周"
        ) {
            // TODO: 2021/6/25 添加处理逻辑.
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