package com.wenubey.pomodoroapp.model

import java.util.*

data class Pomodoro(
    val task_name: String,
    val repeat_count: Int,
    val createdAt: String,
    val workTime: Int,
    val breakTime: Int,
)
