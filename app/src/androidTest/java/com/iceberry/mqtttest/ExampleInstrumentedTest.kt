package com.iceberry.mqtttest

import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.hyphenate.EMMessageListener
import com.hyphenate.EMValueCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMUserInfo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    lateinit var appContext: Context
    var token = ""

    @Before
    fun useAppContext() {
        // Context of the app under test.

        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        //assertEquals("com.iceberry.mqtttest", appContext.packageName)
        Looper.prepare()
        //MQTTUtils.initMQTTSdk(appContext)

    }

    @Test
    fun receiveMsgTest() {
        val msgListener = object : EMMessageListener {
            /**
             * \~chinese
             * 接受消息接口，在接受到文本消息，图片，视频，语音，地理位置，文件这些消息体的时候，会通过此接口通知用户。
             *
             * \~english
             * messages received, message body can be text, image, video, voice, location, file
             */
            override fun onMessageReceived(messages: MutableList<EMMessage>) {
                for (msg in messages) {
                    Log.d("mqttA", msg.toString())
                }
            }

            /**
             * \~chinese
             * 区别于[EMMessageListener.onMessageReceived], 这个接口只包含命令的消息体，包含命令的消息体通常不对用户展示。
             *
             * \~english
             * command messages received. Please refer to [EMMessageListener.onMessageReceived]
             */
            override fun onCmdMessageReceived(messages: MutableList<EMMessage>) {

            }

            /**
             * \~chinese
             * 接受到消息体的已读回执, 消息的接收方已经阅读此消息。
             *
             * \~english
             * received message read ack by recipient as message had been read
             *
             */
            override fun onMessageRead(messages: MutableList<EMMessage>) {

            }

            /**
             * \~chinese
             * 收到消息体的发送回执，消息体已经成功发送到对方设备。
             *
             * \~english
             * received message delivered ack as message delivered to recipient successfully
             */
            override fun onMessageDelivered(messages: MutableList<EMMessage>) {

            }

            /**
             * \~chinese
             * 收到消息体的撤回回调，消息体已经成功撤回。
             *
             * \~english
             * sender has recall the messages.
             */
            override fun onMessageRecalled(messages: MutableList<EMMessage>) {

            }

            /**
             * \~chinese
             * 接受消息发生改变的通知，包括消息ID的改变。消息是改变后的消息。
             * @param message    发生改变的消息
             * @param change
             *
             * \~english
             * received message change event, including message ID change
             *
             * @param message   message value updated
             * @param change
             */
            override fun onMessageChanged(message: EMMessage?, change: Any) {

            }

        }
        EMClient.getInstance().chatManager().addMessageListener(msgListener)

        EMClient.getInstance().chatManager().removeMessageListener(msgListener)
    }

    @Test
    fun sendMsgTest() {
        MQTTUtils.sendMsg("你好呀", "test")
        //EMClient.getInstance().logout(true)
    }

    @Test
    fun updateOwnInfo() {
        val userInfo = EMUserInfo()
        userInfo.userId = EMClient.getInstance().currentUser
//        userInfo.nickName = "jian"
        userInfo.avatarUrl = "https://api.multiavatar.com/1561515615615615.png"
//        userInfo.birth = "2000.10.10"
//        userInfo.signature = "坚持就是胜利"
//        userInfo.phoneNumber = "1343444"
//        userInfo.email = "9892029@qq.com"
//        userInfo.gender = 1
        EMClient.getInstance().userInfoManager()
            .updateOwnInfo(userInfo, object : EMValueCallBack<String?> {
                override fun onSuccess(value: String?) {}
                override fun onError(error: Int, errorMsg: String) {}
            })
    }

    @Test
    fun loginTest() {
        MQTTUtils.login("abc", "123456")
    }

    @Test
    fun getMsgList() {
        val conversation = EMClient.getInstance().chatManager().getConversation("test")

        val messages = conversation.allMessages

        //val messages = conversation.loadMoreMsgFromDB(startMsgId, pagesize)
        messages.forEach {
            Log.d("mqttB", it.body.toString())
        }
    }

    @Test
    fun saveToDb() {
        //根据会话插入消息
        //根据会话插入消息
        val conversation = EMClient.getInstance().chatManager().getConversation("test")
        val messages = conversation.allMessages
        messages.forEach {
            conversation.insertMessage(it)
            Log.d("mqttB", "插入数据库成功")
        }

    }

    @Test
    fun logoutTest() {
        EMClient.getInstance().logout(true)
    }

    @Test
    fun getFriendList() {
        val token: String =
            "Bearer YWMt8gDXvu9iEeuk0VswG6AZ7UKonvpu7USum8bta7ORCQvpNokA6s4R64f6qwda7h3hAwMAAAF664rQ4gBPGgCtrhHjxJrv3jCieUV72BslIKSYWoY7WA54n1a9EYuVZA"
        val ownerUsername: String = "abc"
        RestfulService.getFriendList(token, ownerUsername)
    }

    @Test
    fun registerTest() {
//        val baseUrl="https://a1.easemob.com"
//        val orgName="1170210721175852"
//        val appName="abc"
//        val username="test"
        //val token="YWMtQ0tI5uqmEeumjomajXNUq0Konvpu7USum8bta7ORCQvRFgUQ6dER65BmQz8CzwmQAwMAAAF6zH9hVQBPGgB-a0RqzvpA5z4MSD4TDTpKC9ESyOTFBgie-qTCLSKImQ"
//        val retrofit=Retrofit.Builder()
//            .baseUrl(baseUrl)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        val apiService=retrofit.create(ApiService::class.java)
        val userBodyBean = RegisterUserBodyBean("abcd", "123456")
        //RestfulService.registerUser(userBodyBean)
        MQTTUtils.register(userBodyBean)
//        apiService.registerUser(orgName,appName, arrayOf(userBodyBean)).enqueue(object:Callback<HashMap<String, Objects>>{
//            override fun onResponse(
//                call: Call<HashMap<String, Objects>>,
//                response: Response<HashMap<String, Objects>>
//            ) {
//                Log.d("mqttA","注册信息：code:${response.code()}")
//            }
//
//            override fun onFailure(call: Call<HashMap<String, Objects>>, t: Throwable) {
//                Log.d("mqttA","注册失败")
//                t.printStackTrace()
//            }
//
//        })

//        val userTokenBodyBean=AppTokenBodyBean("password","realme_A11@ura0b0","YXA6Zj0SDb0lq5DqKWbH3j3_2RwrsbE")
//        apiService.getAppToken(orgName,appName,userTokenBodyBean).enqueue(object:Callback<String>{
//
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                token= response.body().toString()
//
//                Log.d("mqttA",token)
//                Log.d("mqttA","${response.code()}")
//                //MQTTUtils.login(username,token)
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Log.d("mqttA","失败")
//            }
//
//        })


        //Log.d("mqttA",token)
    }

}