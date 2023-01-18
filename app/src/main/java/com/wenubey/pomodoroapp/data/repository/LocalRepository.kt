package com.wenubey.pomodoroapp.data.repository

import com.wenubey.pomodoroapp.model.Pomodoro

interface LocalRepository {

    suspend fun insertAndUpdate(pomodoro: Pomodoro)

    suspend fun delete(pomodoro: Pomodoro)

    suspend fun getAll(): List<Pomodoro>
}