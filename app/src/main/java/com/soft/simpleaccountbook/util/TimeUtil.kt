package com.soft.simpleaccountbook.util

import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TimeUtil {
    fun timeStampToString(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd일(E) a HH시 mm분")
        return dateTimeFormatter.format(localDateTime)
    }

    fun dateToTimeStamp(year: Int, month: Int, day: Int, hour: Int, minute: Int): Timestamp {
        val localDateTime = LocalDateTime.of(
            year,
            month,
            day,
            hour,
            minute
        )
        val zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"))
        val localDateTimeseconds = zonedDateTime.toEpochSecond()
        return Timestamp(localDateTimeseconds, 0)

    }

    fun LocalDateToTimeStamp(localDateTime: LocalDateTime): Timestamp {
        val zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"))
        val localDateTimeseconds = zonedDateTime.toEpochSecond()
        return Timestamp(localDateTimeseconds, 0)
    }
}