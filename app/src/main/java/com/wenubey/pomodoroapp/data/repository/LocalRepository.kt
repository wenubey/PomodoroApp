package com.wenubey.pomodoroapp.data.repository

import com.wenubey.pomodoroapp.model.Pomodoro

interface LocalRepository {

    suspend fun insertAndUpdate(pomodoro: Pomodoro)

    suspend fun deleteAll()

    suspend fun getAll(): List<Pomodoro>

    suspend fun getTodayPomodoro(): List<Pomodoro>

    suspend fun getThisWeekPomodoro(): List<Pomodoro>

    suspend fun getThisYearPomodoro(): List<Pomodoro>
}