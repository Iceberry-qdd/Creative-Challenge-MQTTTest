package com.iceberry.mqtttest.viewModel.fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.iceberry.mqtttest.viewModel.SharedViewModel
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    val accountText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()
    val isLogin = MutableLiveData<Boolean>(false)
    val errorMsg = MutableLiveData<String>("")
    val isLoading = MutableLiveData<Boolean>(false)
    val sharedViewModel: SharedViewModel = SharedViewModel()

    fun tryToLogin(username: String, password: String) {
        EMClient.getInstance().login(username, password, object : EMCallBack {
            override fun onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups()
                EMClient.getInstance().chatManager().loadAllConversations()
                viewModelScope.launch {
                    isLogin.value = true
                    sharedViewModel.curLoginUserName.value = username
                }

            }

            override fun onError(code: Int, error: String?) {

                viewModelScope.launch {
                    isLoading.value = false
                    when (code) {
                        204 -> {
                            errorMsg.value = "该用户不存在"
                        }
                        202 -> {
                            errorMsg.value = "用户名或密码错误"
                        }
                        else -> {
                            errorMsg.value = "登陆失败(errorCode:$code error:$error)"
                        }
                    }
                }
                Log.e("mqttA", "${errorMsg.value}")
            }

            override fun onProgress(progress: Int, status: String?) {
            }
        })
    }
}
