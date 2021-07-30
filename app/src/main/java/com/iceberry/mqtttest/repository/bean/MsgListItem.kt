package com.iceberry.mqtttest.repository.bean

/**
 * author: Iceberry
 * email:qddwork@outlook.com
 * date: 2021/7/24
 * desc:
 *
 */
data class MsgListItem(
    val id: Long,
    val profileImageUrl: String,
    val name: String,
    val newestMsg: String,
    val newestMsgTime: String,
    val unReadMsgCount: Int
)
