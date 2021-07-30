package com.iceberry.mqtttest.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.iceberry.mqtttest.R
import com.iceberry.mqtttest.viewModel.SharedViewModel

class MainActivity : AppCompatActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedViewModel.isLogin.observe(this, {
            if (!it) {
                val intent = Intent(applicationContext, SplashActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }
}