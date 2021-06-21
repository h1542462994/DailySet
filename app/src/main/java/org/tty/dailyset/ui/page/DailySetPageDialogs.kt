package org.tty.dailyset.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.tty.dailyset.R
import org.tty.dailyset.data.processor.DailySetProcessor
import org.tty.dailyset.model.entity.DailySetType
import org.tty.dailyset.model.entity.toImageResource
import org.tty.dailyset.model.lifetime.dailyset.DailySetCreateState
import org.tty.dailyset.ui.component.Badge
import org.tty.dailyset.ui.component.NanoDialog
import org.tty.dailyset.ui.component.RoundBadge
import org.tty.dailyset.ui.image.ImageResource
import org.tty.dailyset.ui.theme.LocalPalette

@Composable
fun DailySetCreateDialogCover(
    dailySetProcessor: DailySetProcessor,
    dailySetCreateState: DailySetCreateState
) {
    var selectIcon by dailySetCreateState.selectIcon
    var icon by dailySetCreateState.icon
    var type by dailySetCreateState.type
    var name by dailySetCreateState.name


    NanoDialog(title = stringResource(R.string.dailyset_list_add), dialogState = dailySetCreateState) {
        Row(
            modifier = Modifier
                .height(64.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
        ) {
            // the created icon
            BoxWithConstraints(
                modifier = Modifier
                    .size(width = 48.dp, height = 64.dp)
                    .wrapContentSize(align = Alignment.Center)
            ) {
                Icon(
                    painter = icon?.toImageResource() ?: ImageResource.set_add_emoji(),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {}
                        .padding(8.dp), // to change the selectIcon
                    tint = LocalPalette.current.primary
                )
            }

            OutlinedTextField(
                value = name,
                modifier = Modifier
                    .weight(1f),
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.dailyset_list_name)) }
            )
        }

        Row(
            modifier = Modifier
                .height(64.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
                .padding(8.dp)
        ) {
            Text(
                "列表类型",
                modifier = Modifier.padding(all = 8.dp),
                fontWeight = FontWeight.Bold
            )
            DailySetType.values().forEach {
                BoxWithConstraints(
                    modifier = Modifier.padding(8.dp).scale(1.2f)
                ) {
                    Badge(
                        readOnly = false,
                        checked = it == type,
                        text = when(it) {
                            DailySetType.Normal -> stringResource(R.string.dailyset_type_normal)
                            DailySetType.Clazz -> stringResource(R.string.dailyset_type_clazz)
                            DailySetType.TaskSpecific -> stringResource(R.string.dailyset_type_task)
                        }
                    ) {
                        // click to change the type
                        type = it
                    }
                }

            }
        }

        Row {
            Text(
                text = when(type) {
                    DailySetType.Normal -> stringResource(R.string.dailyset_type_normal_description)
                    DailySetType.Clazz -> stringResource(R.string.dailyset_type_clazz_description)
                    DailySetType.TaskSpecific -> stringResource(R.string.dailyset_type_task_description)
                },
                color = LocalPalette.current.sub,
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Light,
            )
        }
    }
}