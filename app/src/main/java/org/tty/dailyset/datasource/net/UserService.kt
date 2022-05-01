package org.tty.dailyset.datasource.net

import org.tty.dailyset.bean.Responses
import org.tty.dailyset.bean.req.UserLoginReq
import org.tty.dailyset.bean.req.UserRegisterReq
import org.tty.dailyset.bean.resp.UserLoginResp
import org.tty.dailyset.bean.resp.UserRegisterResp
import org.tty.dailyset.bean.resp.UserStateResp
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.QueryName

interface UserService {
    @POST("/user/register")
    suspend fun register(userRegisterReq: UserRegisterReq): Responses<UserRegisterResp>

    @POST("/user/login")
    suspend fun login(@Body userLoginReq: UserLoginReq): Responses<UserLoginResp>

    @POST("/user/state")
    suspend fun state(): Responses<UserStateResp>
}