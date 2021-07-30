package com.iceberry.mqtttest.repository.data

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iceberry.mqtttest.repository.bean.Conversation
import com.iceberry.mqtttest.repository.mock.conversationList

/**
 * author: Iceberry
 * email:qddwork@outlook.com
 * date: 2021/7/24
 * desc:
 *
 */
class ConversationListDataSource(resources: Resources) {
    companion object {
        private var INSTANCE: ConversationListDataSource? = null

        fun getDataSource(resources: Resources): ConversationListDataSource {
            return synchronized(ConversationListDataSource::class) {
                val newInstance = INSTANCE ?: ConversationListDataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }

    private val initialConversationList = conversationList(resources)
    private val conversationListLiveData = MutableLiveData(initialConversationList)

    fun addConversation(conversation: Conversation) {

        val currentList = conversationListLiveData.value
        if (currentList == null) {
            conversationListLiveData.postValue(listOf(conversation))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(currentList.size, conversation)
            conversationListLiveData.postValue(updatedList)
        }
    }

    fun removeConversationListItem(conversation: Conversation) {
        val currentConversationList = conversationListLiveData.value
        if (currentConversationList != null) {
            val updatedList = currentConversationList.toMutableList()
            updatedList.remove(conversation)
            conversationListLiveData.postValue(updatedList)
        }
    }

    fun getConversationListItemTime(dateTime: String): Conversation? {
        conversationListLiveData.value?.let { list ->
            return list.firstOrNull { it.dateTime == dateTime }
        }
        return null
    }

    fun getConversationList(): LiveData<List<Conversation>> {
        return conversationListLiveData
    }
}