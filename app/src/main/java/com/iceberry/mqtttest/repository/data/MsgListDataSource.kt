package com.iceberry.mqtttest.repository.data

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iceberry.mqtttest.repository.bean.MsgListItem
import com.iceberry.mqtttest.repository.mock.msgList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * author: Iceberry
 * email:qddwork@outlook.com
 * date: 2021/7/24
 * desc:
 *
 */
class MsgListDataSource(val resources: Resources) {
    companion object {
        private var INSTANCE: MsgListDataSource? = null

        fun getDataSource(resources: Resources): MsgListDataSource {
            return synchronized(MsgListDataSource::class) {
                val newInstance = INSTANCE ?: MsgListDataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }

    private val initialMsgList = getList(resources)

    private fun getList(resources: Resources): List<MsgListItem> {
        var list: MutableList<MsgListItem>
        runBlocking {
            list = withContext(Dispatchers.Default) {
                msgList(resources = resources)
            }

        }

        return list
    }

    private val msgListLiveData = MutableLiveData(initialMsgList)

    fun updateMsgList() {
        msgListLiveData.postValue(getList(resources = resources))

    }

    fun addMsgListItem(msgListItem: MsgListItem) {
        val currentList = msgListLiveData.value
        if (currentList == null) {
            msgListLiveData.postValue(listOf(msgListItem))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, msgListItem)
            msgListLiveData.postValue(updatedList)
        }
    }

    fun removeMsgListItem(msgListItem: MsgListItem) {
        val currentMsgList = msgListLiveData.value
        if (currentMsgList != null) {
            val updatedList = currentMsgList.toMutableList()
            updatedList.remove(msgListItem)
            msgListLiveData.postValue(updatedList)
        }
    }

    fun getMsgListItemId(id: Long): MsgListItem? {
        msgListLiveData.value?.let { list ->
            return list.firstOrNull { it.id == id }
        }
        return null
    }

    fun getMsgList(): LiveData<List<MsgListItem>> {
        return msgListLiveData
    }
}