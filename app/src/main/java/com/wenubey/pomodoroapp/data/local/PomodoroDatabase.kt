package com.wenubey.pomodoroapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wenubey.pomodoroapp.model.Pomodoro

@Database(
    entities = [Pomodoro::class],
    version = 1,
)
@TypeConverters(PomodoroTypeConverter::class)
abstract class PomodoroDatabase: RoomDatabase() {
    abstract fun pomodoroDao(): PomodoroDao
}

