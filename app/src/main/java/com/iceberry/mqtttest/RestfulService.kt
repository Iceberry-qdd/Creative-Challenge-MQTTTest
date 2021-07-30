package com.iceberry.mqtttest

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * author: Iceberry
 * email:qddwork@outlook.com
 * date: 2021/7/22
 * desc:
 *
 */
class RestfulService {

    companion object {
        private val retrofit: Retrofit
        private val apiService: ApiService
        private const val orgName = "1170210721175852"
        private const val appName = "abc"

        init {
            val baseUrl = "https://a1.easemob.com"

            //val token="YWMtQ0tI5uqmEeumjomajXNUq0Konvpu7USum8bta7ORCQvRFgUQ6dER65BmQz8CzwmQAwMAAAF6zH9hVQBPGgB-a0RqzvpA5z4MSD4TDTpKC9ESyOTFBgie-qTCLSKImQ"
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            apiService = retrofit.create(ApiService::class.java)
            val userBodyBean = RegisterUserBodyBean("abc", "123456")
        }

//        fun registerUser(userBodyBean: RegisterUserBodyBean) {
//            apiService.registerUser(orgName, appName, arrayOf(userBodyBean)).enqueue(object :
//                Callback<String> {
//                override fun onResponse(call: Call<String>, response: Response<String>) {
//                    Log.d("mqttA", "code:${response.code()}")
//                    Log.d("mqttA", "${response.body()}")
//                }
//
//                override fun onFailure(call: Call<String>, t: Throwable) {
//                    Log.d("mqttA", "注册失败")
//                    t.printStackTrace()
//                }
//
//            })
//        }

        fun getFriendList(token: String, ownerUsername: String) {
            apiService.getFriendList(orgName, appName, ownerUsername, token)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Log.d("mqttA", "code:${response.code()}")
                        Log.d("mqttA", "${response.body()}")
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("mqttA", "code:${t.message}")
                        //Log.d("mqttA","${response.body()}")
                        t.printStackTrace()
                    }

                })
        }
    }
}