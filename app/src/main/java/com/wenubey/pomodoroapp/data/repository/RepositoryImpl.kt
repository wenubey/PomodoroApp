package com.wenubey.pomodoroapp.data.repository

import com.wenubey.pomodoroapp.data.local.PomodoroDao
import com.wenubey.pomodoroapp.model.Pomodoro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.SimpleTimeZone
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val pomodoroDao: PomodoroDao
) : LocalRepository {
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

    override suspend fun getAll(): List<Pomodoro> = withContext(Dispatchers.IO) {
        pomodoroDao.getAll()
    }


    override suspend fun getTotalToday(): Float =
        withContext(Dispatchers.IO) {
            pomodoroDao.getAll().filter { pomodoro ->
                val currentDate = pomodoro.createdAt
                currentDate.dayOfYear == LocalDateTime.now().dayOfYear
            }.sumOf {
                it.workTime
            } / 1000f
        }

    override suspend fun getThisWeekPomodoro(): List<Pomodoro> =
        withContext(Dispatchers.IO) {
            val currentDay = LocalDateTime.now()
            val shiftLeft = currentDay.dayOfWeek.value - 1
            val shiftRight = 7 - currentDay.dayOfWeek.value
            val endOfWeek = LocalDateTime.now().plusDays(shiftRight.toLong())
            val startOfWeek = LocalDateTime.now().minusDays(shiftLeft.toLong())
            pomodoroDao.getBetweenDate(startOfWeek.toString(), endOfWeek.toString())
        }

    override suspend fun getThisMonthPomodoro(): List<Pomodoro> =
        withContext(Dispatchers.IO) {
            pomodoroDao.getAll().filter {
                it.createdAt.month == LocalDateTime.now().month && it.createdAt.year == LocalDateTime.now().year
            }
        }

    override suspend fun getThisYearPomodoro(): List<Pomodoro> =
        withContext(Dispatchers.IO) {
            pomodoroDao.getAll().filter {
                it.createdAt.year == LocalDateTime.now().year
            }
        }


}