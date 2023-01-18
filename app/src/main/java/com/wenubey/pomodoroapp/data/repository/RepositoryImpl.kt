package com.wenubey.pomodoroapp.data.repository

import com.wenubey.pomodoroapp.data.local.PomodoroDao
import com.wenubey.pomodoroapp.model.Pomodoro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val pomodoroDao: PomodoroDao
): LocalRepository {
    override suspend fun insertAndUpdate(pomodoro: Pomodoro) {
        withContext(Dispatchers.IO) {
            pomodoroDao.insertAndUpdate(pomodoro)
        }
    }

    override suspend fun delete(pomodoro: Pomodoro) {
        withContext(Dispatchers.IO) {
            pomodoroDao.delete(pomodoro)
        }
    }

    override suspend fun getAll(): List<Pomodoro> =
        withContext(Dispatchers.IO) {
            pomodoroDao.getAll()
        }

}