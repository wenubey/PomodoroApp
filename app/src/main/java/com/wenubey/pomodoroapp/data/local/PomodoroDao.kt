package com.wenubey.pomodoroapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.wenubey.pomodoroapp.model.Pomodoro

@Dao
interface PomodoroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndUpdate(pomodoro: Pomodoro)

    @Delete
    suspend fun delete(pomodoro: Pomodoro)



}