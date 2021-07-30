package com.iceberry.mqtttest.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.iceberry.mqtttest.MQTTUtils

/**
 * author: Iceberry
 * email:qddwork@outlook.com
 * date: 2021/7/22
 * desc:
 *
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        MQTTUtils.initMQTTSdk(applicationContext)
    }

}