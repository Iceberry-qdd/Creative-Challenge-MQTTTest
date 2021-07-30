package com.iceberry.mqtttest

import android.content.Context
import android.util.Log
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMOptions

/**
 * author: Iceberry
 * email:qddwork@outlook.com
 * date: 2021/7/22
 * desc:
 *
 */
class MQTTUtils {
    companion object {
        private const val APP_KEY = "1170210721175852#abc"
        private const val IM_SERVER = "ura0b0.cn1.mqtt.chat"
        private const val IM_PORT = 1883
        //private lateinit var appContext: Context


        /**
         * 初始化环信SDK
         */
        fun initMQTTSdk(context: Context) {
            val options = EMOptions().apply {
                //添加好友时需要验证
                acceptInvitationAlways = false
                //自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载
                autoTransferMessageAttachments = true
                //自动下载附件类消息的缩略图等
                setAutoDownloadThumbnail(true)
                appKey = APP_KEY
                setIMServer(IM_SERVER)
                imPort = IM_PORT
                usingHttpsOnly = true
            }
            //初始化
            EMClient.getInstance().init(context, options)
            //开启debug模式
            EMClient.getInstance().setDebugMode(true)
            Log.d("mqttA", "初始化完成！")
        }

        fun login(username: String, password: String): Boolean {
            var isSuccess = false
            EMClient.getInstance().login(username, password, object : EMCallBack {
                /**
                 * \~chinese
                 * 程序执行成功时执行回调函数。
                 *
                 * \~english
                 *
                 */
                override fun onSuccess() {
                    EMClient.getInstance().groupManager().loadAllGroups()
                    EMClient.getInstance().chatManager().loadAllConversations()
                    Log.d("mqttA", "登录聊天服务器成功！")
                    isSuccess = true
                }

                /**
                 * \~chinese
                 * 发生错误时调用的回调函数  @see EMError
                 *
                 * @param code           错误代码
                 * @param error          包含文本类型的错误描述。
                 *
                 * \~english
                 * Callback when encounter error @see EMError
                 *
                 * @param code           error code
                 * @param error          contain description of error
                 */
                override fun onError(code: Int, error: String?) {
                    Log.d("mqttA", "登录聊天服务器失败！code:$code")
                    Log.d("mqttA", error.toString())
                }

                /**
                 * \~chinese
                 * 刷新进度的回调函数
                 *
                 * @param progress       进度信息
                 * @param status         包含文件描述的进度信息, 如果SDK没有提供，结果可能是"", 或者null。
                 *
                 * \~english
                 * callback for progress update
                 *
                 * @param progress
                 * @param status         contain progress description. May be empty string "" or null.
                 */
                override fun onProgress(progress: Int, status: String?) {
                    Log.d("mqttA", "progress:$progress,status:$status")
                }
            })
            return isSuccess
        }

        fun sendMsg(msg: String, toChatUsername: String) {
            val message = EMMessage.createTxtSendMessage(msg, toChatUsername)
            if (message.chatType == EMMessage.ChatType.GroupChat) {
                message.chatType = EMMessage.ChatType.GroupChat
            }
            message.setMessageStatusCallback(object : EMCallBack {
                override fun onSuccess() {
                    Log.d("mqttA", "发送成功")
                }

                override fun onError(code: Int, error: String?) {
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
}