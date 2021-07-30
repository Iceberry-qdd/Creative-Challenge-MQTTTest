package com.iceberry.mqtttest.repository.bean

/**
 * author: Iceberry
 * email:qddwork@outlook.com
 * date: 2021/7/25
 * desc:
 *
 */
data class Conversation(
    val type: Int = 1,
    val profileImgUrl: String = "",
    val from: String = "",
    val to: String = "",
    val content: String = "",
    val dateTime: String = ""
) {
    companion object {
        const val SELF = 0
        const val OTHER = 1
    }
}
