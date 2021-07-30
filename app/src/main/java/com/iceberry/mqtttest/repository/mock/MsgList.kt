package com.iceberry.mqtttest.repository.mock

import android.content.res.Resources
import android.util.Log
import com.hyphenate.EMValueCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMUserInfo
import com.hyphenate.exceptions.HyphenateException
import com.iceberry.mqtttest.repository.bean.MsgListItem
import com.iceberry.mqtttest.utils.MsgUtils
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * author: Iceberry
 * email:qddwork@outlook.com
 * date: 2021/7/24
 * desc:
 *
 */
suspend fun msgList(resources: Resources): MutableList<MsgListItem> {
    val mMsgList: MutableList<MsgListItem> = mutableListOf()
    var allContactsFromServer = mutableListOf<String>()

    coroutineScope {
        launch {
            try {
                allContactsFromServer =
                    EMClient.getInstance().contactManager().allContactsFromServer
                allContactsFromServer.forEach {
                    Log.d("mqttA", it)
                }

            } catch (e: HyphenateException) {
                e.printStackTrace()
                Log.e("mqttAE", e.message.toString())
            }
        }

    }

    if (allContactsFromServer.isNullOrEmpty() || allContactsFromServer.size == 0) {
        return mutableListOf()
    }

    val allContactUsername = Array(allContactsFromServer.size) { "" }
    Log.e("mqttA", allContactsFromServer.size.toString())
    for ((index, item) in allContactsFromServer.withIndex()) {
        allContactUsername[index] = item
        //Log.e("mqttA",item)
    }

    val userInfoTypes =
        arrayOf(EMUserInfo.EMUserInfoType.NICKNAME, EMUserInfo.EMUserInfoType.AVATAR_URL)
    EMClient.getInstance().userInfoManager().fetchUserInfoByAttribute(
        allContactUsername,
        userInfoTypes,
        object : EMValueCallBack<Map<String, EMUserInfo>> {
            override fun onSuccess(value: Map<String, EMUserInfo>?) {
                if (value.isNullOrEmpty()) {
                    return
                }
                var id = 0L
                allContactUsername.forEach {
                    val conversation = EMClient.getInstance().chatManager().getConversation(it)
                    try {
                        var messages = conversation.allMessages
                        if (messages.size == 0) {
                            messages = conversation.loadMoreMsgFromDB(it, 100)
                        }
                        val unreadMsgCount = conversation.unreadMsgCount

                        mMsgList.add(
                            MsgListItem(
                                id = id,
                                profileImageUrl = value[it]?.avatarUrl.toString(),
                                name = value[it]?.userId.toString(),
                                newestMsg = MsgUtils.getMsgContent(messages[0].body.toString()),
                                newestMsgTime = MsgUtils.convertToTime(messages[0].msgTime),
                                unReadMsgCount = unreadMsgCount
                            )
                        )
                    } catch (e: NullPointerException) {
                        Log.d("mqttAE", "${e.message}")
                    }
                    id = id.plus(1)
                }
            }

            override fun onError(error: Int, errorMsg: String?) {
            }
        })
    return mMsgList
}