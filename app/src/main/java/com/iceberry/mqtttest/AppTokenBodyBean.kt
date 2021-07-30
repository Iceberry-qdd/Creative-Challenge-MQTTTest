package com.iceberry.mqtttest

/**
 * author: Iceberry
 * email:qddwork@outlook.com
 * date: 2021/7/22
 * desc:
 *
 */
data class AppTokenBodyBean(
    val grant_type: String,
    val client_id: String,
    val client_secret: String
)
