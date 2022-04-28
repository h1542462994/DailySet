package org.tty.dailyset.ui.page

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import org.tty.dailyset.common.datetime.toShortString
import org.tty.dailyset.common.datetime.toStandardString
import org.tty.dailyset.provider.LocalServices
import java.time.LocalDateTime

@Composable
fun SummaryPage() {
    val service = LocalServices.current
    val now by service.dataSourceCollection.runtimeDataSource.now.collectAsState(initial = LocalDateTime.now())

    Text(text = now.toStandardString())

}