package com.iceberry.mqtttest.viewModel.fragment

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hyphenate.chat.EMClient
import com.iceberry.mqtttest.repository.bean.MsgListItem
import com.iceberry.mqtttest.repository.data.MsgListDataSource

class HomeViewModel(val msgListDataSource: MsgListDataSource) : ViewModel() {
    val msgListLiveData = msgListDataSource.getMsgList()
    val isRefreshing = MutableLiveData(false)

    fun insertMsgListItem(newMsgListItem: MsgListItem) {
        msgListDataSource.addMsgListItem(newMsgListItem)
    }

    fun logout() {
        EMClient.getInstance().logout(true)
    }

    fun upDateList() {
        msgListDataSource.updateMsgList()
    }
}

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(
                msgListDataSource = MsgListDataSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}