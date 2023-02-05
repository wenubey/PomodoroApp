package com.wenubey.pomodoroapp.data.repository

import com.wenubey.pomodoroapp.data.local.PomodoroDao
import com.wenubey.pomodoroapp.model.Pomodoro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val pomodoroDao: PomodoroDao
): LocalRepository {
    override suspend fun insertAndUpdate(pomodoro: Pomodoro) {
        withContext(Dispatchers.IO) {
            pomodoroDao.insertAndUpdate(pomodoro)
        }
    }

    override suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            pomodoroDao.deleteAll()
        }
    }

    override suspend fun getAll(): List<Pomodoro> =
        withContext(Dispatchers.IO) {
            pomodoroDao.getAll()
        }

    override suspend fun getTodayPomodoro(): List<Pomodoro> =
        withContext(Dispatchers.IO) {
            pomodoroDao.getAll().filter { pomodoro ->
                val currentDate = pomodoro.createdAt
                currentDate.dayOfYear == LocalDateTime.now().dayOfYear
            }
        }

    override suspend fun getThisWeekPomodoro(): List<Pomodoro> {
        val currentWeekPomodoro = mutableListOf<Pomodoro>()
        withContext(Dispatchers.IO) {
            val list: List<Pomodoro> = pomodoroDao.getAll()
            val currentDay = LocalDateTime.now()
            val shiftLeft = currentDay.dayOfWeek.value - 1
            val shiftRight = 7 - currentDay.dayOfWeek.value
            val endOfWeek = LocalDateTime.now().plusDays(shiftRight.toLong())
            val startOfWeek = LocalDateTime.now().minusDays(shiftLeft.toLong())

            list.forEach {
                if(startOfWeek <= it.createdAt && it.createdAt <= endOfWeek){
                    currentWeekPomodoro.add(it)
                    println("CURRENT WEEK ITEMS: ${it.task_name}")
                }
            }
        }
        return currentWeekPomodoro
    }

    override suspend fun getThisYearPomodoro(): List<Pomodoro> {
        val currentYearPomodoro = mutableListOf<Pomodoro>()
        withContext(Dispatchers.IO) {
            val list: List<Pomodoro> = pomodoroDao.getAll()
            val currentDate = LocalDateTime.now()
            list.forEach {
                if (currentDate.year == it.createdAt.year) {
                    currentYearPomodoro.add(it)
                }
            }
        }
        return currentYearPomodoro
    }
}