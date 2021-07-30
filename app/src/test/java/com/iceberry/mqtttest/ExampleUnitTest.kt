package com.iceberry.mqtttest

import android.util.Log
import com.iceberry.mqtttest.utils.MsgUtils
import org.junit.Test

import org.junit.Assert.*
import kotlin.random.Random

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun avatarNameTest(){
        var avatarName= Random.nextLong().toString(2)
        Log.d("ava",avatarName)
    }

    @Test
    fun timeConvertTest(){
        println(MsgUtils.convertToTime(1527469632314))
    }

    @Test
    fun getMsgContentTest(){
        println(MsgUtils.getMsgContent("txt\"asd\""))
    }
}