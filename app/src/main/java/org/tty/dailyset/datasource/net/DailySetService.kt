package org.tty.dailyset.datasource.net

import org.tty.dailyset.bean.Responses
import org.tty.dailyset.bean.entity.DailySet
import org.tty.dailyset.bean.req.DailySetUpdateReq
import org.tty.dailyset.bean.resp.DailySetUpdateRawResult
import retrofit2.http.Body
import retrofit2.http.POST

interface DailySetService {
    @POST("/dailyset/info")
    suspend fun dailySetInfo(): Responses<List<DailySet>>

    @POST("/dailyset/update")
    suspend fun dailySetUpdate(@Body dailySetUpdateReq: DailySetUpdateReq): Responses<DailySetUpdateRawResult>
}