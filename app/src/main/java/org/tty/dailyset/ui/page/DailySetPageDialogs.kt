package org.tty.dailyset.ui.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.tty.dailyset.R
import org.tty.dailyset.data.processor.DailySetProcessor
import org.tty.dailyset.model.entity.DailySetIcon
import org.tty.dailyset.model.entity.DailySetType
import org.tty.dailyset.model.entity.toImageResource
import org.tty.dailyset.model.lifetime.dailyset.DailySetCreateState
import org.tty.dailyset.ui.component.Badge
import org.tty.dailyset.ui.component.NanoDialog
import org.tty.dailyset.ui.component.NanoDialogButton
import org.tty.dailyset.ui.component.RoundBadge
import org.tty.dailyset.ui.image.ImageResource
import org.tty.dailyset.ui.theme.LocalPalette

@ExperimentalFoundationApi
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
                        .clickable {
                            // to change the selectIcon to another area.
                            selectIcon = !selectIcon
                        }
                        .padding(8.dp), // to change the selectIcon
                    tint = if (icon == null) LocalPalette.current.primary else Color.Unspecified
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

        if (!selectIcon) {
            // if not selectIcon, show select dailySetType area.
            Row(
                modifier = Modifier
                    .height(64.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .padding(8.dp)
            ) {
                Text(
                    "????????????",
                    modifier = Modifier.padding(all = 8.dp),
                    fontWeight = FontWeight.Bold
                )
                DailySetType.values().forEach {
                    BoxWithConstraints(
                        modifier = Modifier
                            .padding(8.dp)
                            .scale(1.2f)
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

            // TODO: 2021/6/24 ??????TaskSpecific???????????????
            val enabled = name.length in 2..10 && type != DailySetType.TaskSpecific
            NanoDialogButton(text = stringResource(id = R.string.dailyset_list_add), enabled = enabled) {
                dailySetProcessor.onCreate(name, icon, type)
            }
        } else {
            BoxWithConstraints(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                // show selectIcon area.
                LazyVerticalGrid(
                    cells = GridCells.Fixed(6)) {
                    items(
                        items = DailySetIcon.values()
                    ) {
                        BoxWithConstraints(
                            modifier =
                            Modifier
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
                // delete the emoji icon
                icon = null
            }
        }


    }
}