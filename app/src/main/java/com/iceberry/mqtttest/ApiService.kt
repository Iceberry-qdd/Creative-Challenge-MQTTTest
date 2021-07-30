package com.iceberry.mqtttest

import retrofit2.Call
import retrofit2.http.*

/**
 * author: Iceberry
 * email:qddwork@outlook.com
 * date: 2021/7/22
 * desc:
 *
 */
interface ApiService {
//    companion object{
//        private const val orgName = "1170210721175852"
//        private const val appName = "abc"
//    }

    /**
     * 获取用户token
     */
    @POST("{org_name}/{app_name}/token")
    fun getAppToken(
        @Path("org_name") orgName: String,
        @Path("app_name") appName: String,
        @Body bodyBean: AppTokenBodyBean
    ): Call<String>
//
//    @POST("{org_name}/{app_name}/users")
//    suspend fun registerUser(
//        @Path("org_name") orgName: String,
//        @Path("app_name") appName: String,
//        @Body body: Array<RegisterUserBodyBean>
//    ): BaseResp<BaseRestfulResp<RegisterResp>>

    @GET("{org_name}/{app_name}/users/{owner_username}/contacts/users")
    fun getFriendList(
        @Path("org_name") orgName: String,
        @Path("app_name") appName: String,
        @Path("owner_username") ownerUsername: String,
        @Header("Authorization") token: String
    ): Call<String>
}