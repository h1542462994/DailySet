package org.tty.dailyset.datasource.net

import org.tty.dailyset.bean.Responses
import org.tty.dailyset.bean.entity.DailySet
import retrofit2.http.POST

interface DailySetService {
    @POST("/dailyset/info")
    suspend fun dailySetInfo(): Responses<List<DailySet>>
}