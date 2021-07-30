package com.iceberry.mqtttest.viewModel.fragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.iceberry.mqtttest.repository.bean.Conversation
import com.iceberry.mqtttest.repository.data.ConversationListDataSource
import kotlinx.coroutines.launch


class ConversationViewModel(val conversationListDataSource: ConversationListDataSource) :
    ViewModel() {
    val conversationLiveData = conversationListDataSource.getConversationList()
    val receiver = MutableLiveData<String>()
    val succeedSend = MutableLiveData<Boolean>()

    fun insertConversation(id: Long?) {
        val imgUrl = "https://api.multiavatar.com/1234565161616.png"
        val imgUrl2 = "https://api.multiavatar.com/1234565156216.png"
        if (id == null) return

        val newConversation = Conversation()
        conversationListDataSource.addConversation(newConversation)
    }

    fun addConversation(newConversation: Conversation) {
        conversationListDataSource.addConversation(newConversation)
    }

    fun sendMsg(msg: String, toChatUsername: String) {
        val message = EMMessage.createTxtSendMessage(msg, toChatUsername)
        if (message.chatType == EMMessage.ChatType.GroupChat) {
            message.chatType = EMMessage.ChatType.GroupChat
        }
        message.setMessageStatusCallback(object : EMCallBack {
            override fun onSuccess() {
                viewModelScope.launch {
                    succeedSend.value = true
                }

                Log.d("mqttA", "发送成功")
            }

            override fun onError(code: Int, error: String?) {
                viewModelScope.launch {
                    succeedSend.value = false
                }

                Log.d("mqttA", "发送失败 代码：${code}")
                Log.d("mqttA", error.toString())
            }

            override fun onProgress(progress: Int, status: String?) {
                Log.d("mqttA", "发送进度：${progress} 发送状态：${status}")
            }
        })
        EMClient.getInstance().chatManager().sendMessage(message)
    }
}

class ConversationModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConversationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConversationViewModel(
                conversationListDataSource = ConversationListDataSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}