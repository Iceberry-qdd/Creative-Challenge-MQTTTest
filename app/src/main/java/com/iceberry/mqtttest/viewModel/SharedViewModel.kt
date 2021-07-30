package com.iceberry.mqtttest.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * author: Iceberry
 * email:qddwork@outlook.com
 * date: 2021/7/26
 * desc:
 *
 */
class SharedViewModel : ViewModel() {
    val receiverName = MutableLiveData<String>()
    val isLogin = MutableLiveData<Boolean>()
    val curLoginUserName = MutableLiveData<String>()
}