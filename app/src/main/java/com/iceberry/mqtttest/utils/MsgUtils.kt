package com.iceberry.mqtttest.utils

import java.text.SimpleDateFormat
import java.util.*


/**
 * author: Iceberry
 * email:qddwork@outlook.com
 * date: 2021/7/26
 * desc:
 *
 */
class MsgUtils {
    companion object {
        fun convertToTime(timeMillis: Long): String {

            val perFormatDate = Date(timeMillis)
            val format = SimpleDateFormat("yyyyMMddHH:mm:ss", Locale.CHINA).format(perFormatDate)

            val year = format.substring(0, 4)
            val month = format.substring(4, 6)
            val day = format.substring(6, 8)
            val time = format.substring(8, 13)

            val curTimeMillis = System.currentTimeMillis()
            val format1 = SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(curTimeMillis)
            val curYear = format1.substring(0, 4)
            val curMonth = format1.substring(4, 6)
            val curDay = format1.substring(6, 8)

            return if ("$year$month$day" == "$curYear$curMonth$curDay") {
                time
            } else {
                "${month}月${day}日"
            }
        }

        fun getMsgContent(msg: String): String {
            return try {
                msg.split("\"")[1]
            } catch (e: IndexOutOfBoundsException) {
                msg
            }
        }
    }
}