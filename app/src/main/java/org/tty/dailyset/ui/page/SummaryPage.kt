package org.tty.dailyset.ui.page

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.tty.dailyset.common.datetime.toStandardString
import org.tty.dailyset.provider.LocalSharedComponents0
import java.time.LocalDateTime

@Composable
fun SummaryPage() {
    val service = LocalSharedComponents0.current
    val now by service.dataSourceCollection.runtimeDataSource.now.collectAsState(initial = LocalDateTime.now())

    Text(text = now.toStandardString())

}