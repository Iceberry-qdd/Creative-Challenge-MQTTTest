package com.iceberry.mqtttest.repository.mock

import android.content.res.Resources
import android.util.Log
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.iceberry.mqtttest.Config
import com.iceberry.mqtttest.repository.bean.Conversation
import com.iceberry.mqtttest.utils.MsgUtils


/**
 * author: Iceberry
 * email:qddwork@outlook.com
 * date: 2021/7/25
 * desc:
 *
 */

fun conversationList(resources: Resources): List<Conversation> {
    val imgUrl = "https://api.multiavatar.com/1234565161616.png"

    Log.d("mqttAQ", "发给${Config.receiverName}")
    val conversation = EMClient.getInstance().chatManager().getConversation(Config.receiverName)
    var messages = mutableListOf<EMMessage>()
    try {
        messages = conversation.allMessages
        Log.d("mqttA", "消息数量为：${messages.size}")
        val messagesFromDb = conversation.loadMoreMsgFromDB(Config.receiverName, 100)
        messages.addAll(messagesFromDb)
        Log.d("mqttA", "从数据库加载后的消息数量为：${messages.size}")
    } catch (e: NullPointerException) {
        e.printStackTrace()
        Log.e("mqttAE", e.message.toString())
    }


    if (messages.size == 0) {
        return mutableListOf()
    }
    //根据会话插入消息
    //根据会话插入消息

    messages.forEach {
        val isSucceed = conversation.insertMessage(it)
        Log.e("mqttAI", if (isSucceed) "插入成功" else "插入失败")
    }

    val conversationList = MutableList(0) { Conversation() }

    messages.forEach {
        val type = if (it.to == Config.receiverName) {
            Conversation.SELF
        } else {
            Conversation.OTHER
        }
        conversationList.add(
            Conversation(
                from = it.from,
                to = it.to,
                content = MsgUtils.getMsgContent(it.body.toString()),
                dateTime = MsgUtils.convertToTime(it.msgTime),
                profileImgUrl = imgUrl,
                type = type
            )
        )

    }
    Log.d("mqttAC", "${conversationList.size}")
    return conversationList
}