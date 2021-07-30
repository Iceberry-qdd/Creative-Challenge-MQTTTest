package com.iceberry.mqtttest.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.hyphenate.chat.EMClient
import com.iceberry.mqtttest.R
import com.iceberry.mqtttest.viewModel.SharedViewModel

class SplashActivity : AppCompatActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        skipLogin()
        sharedViewModel.isLogin.observe(this, {
            if (it) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                Log.d("mqttA", "登陆成功")
                startActivity(intent)
                finish()
            }
        })
    }

    private fun skipLogin() {
        if (EMClient.getInstance().isLoggedInBefore) {
            Log.d("mqttA", "登陆成功")
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}