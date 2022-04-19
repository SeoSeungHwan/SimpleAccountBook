package com.soft.simpleaccountbook.util

import android.os.Build
import android.util.Log
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class TimeUtil {
    fun timeStampToString(timestamp: Timestamp) : String{
        val date = timestamp.toDate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
            val dateTimeFormatter = DateTimeFormatter.ofPattern("dd일(E) a HH시 mm분")
            return dateTimeFormatter.format(localDateTime)
        }else{
            val simpleDateTimeFormatter = SimpleDateFormat("dd일(E) a HH시 mm분")
            return simpleDateTimeFormatter.format(date)
        }
    }

    fun dateToTimeStamp(year: Int, month: Int, day: Int, hour: Int, minute: Int) : Timestamp{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val localDateTime = LocalDateTime.of(
                year,
                month + 1,
                day,
                hour,
                minute
            )
            val zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"))
            val localDateTimeseconds = zonedDateTime.toEpochSecond()
            return Timestamp(localDateTimeseconds, 0)
        }else{
            return Timestamp(
                Date(year,
                    month + 1,
                    day,
                    hour,
                    minute)
            )
        }
    }
}