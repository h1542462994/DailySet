package org.tty.dailyset.datasource.net

import org.tty.dailyset.bean.Responses
import org.tty.dailyset.bean.req.UserAutoLoginReq
import org.tty.dailyset.bean.req.UserLoginReq
import org.tty.dailyset.bean.req.UserRegisterReq
import org.tty.dailyset.bean.resp.UserLoginResp
import org.tty.dailyset.bean.resp.UserRegisterResp
import org.tty.dailyset.bean.resp.UserStateResp
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/user/register")
    suspend fun register(@Body userRegisterReq: UserRegisterReq): Responses<UserRegisterResp>

    @POST("/user/login")
    suspend fun login(@Body userLoginReq: UserLoginReq): Responses<UserLoginResp>

    @POST("/user/autoLogin")
    suspend fun autoLogin(@Body userAutoLoginReq: UserAutoLoginReq): Responses<UserStateResp>

    @POST("/user/state")
    suspend fun state(): Responses<UserStateResp>
}