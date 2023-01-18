package com.wenubey.pomodoroapp.data.local

import androidx.room.*
import com.wenubey.pomodoroapp.model.Pomodoro

@Dao
interface PomodoroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndUpdate(pomodoro: Pomodoro)

    @Delete
    suspend fun delete(pomodoro: Pomodoro)

    @Query("SELECT * FROM pomodoro_table")
    suspend fun getAll(): List<Pomodoro>

}