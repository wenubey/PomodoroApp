package com.wenubey.pomodoroapp.data.local

import androidx.room.*
import com.wenubey.pomodoroapp.model.Pomodoro

@Dao
interface PomodoroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndUpdate(pomodoro: Pomodoro)

    @Query("SELECT * FROM pomodoro_table")
    suspend fun getAll(): List<Pomodoro>

    @Query("DELETE FROM pomodoro_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM pomodoro_table WHERE CREATED_AT BETWEEN :start AND :end ORDER BY CREATED_AT")
    suspend fun getBetweenDate(start: String, end: String): List<Pomodoro>

}