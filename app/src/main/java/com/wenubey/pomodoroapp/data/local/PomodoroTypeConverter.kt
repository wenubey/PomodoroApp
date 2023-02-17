package com.wenubey.pomodoroapp.data.local

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDateTime

@TypeConverters
class PomodoroTypeConverter {

    @TypeConverter
    fun toDate(dateString: String?): LocalDateTime? {
        return if (dateString == null) {
            null
        } else {
            LocalDateTime.parse(dateString)
        }
    }

    @TypeConverter
    fun toDateString(date: LocalDateTime?): String? {
        return date?.toString()
    }

}