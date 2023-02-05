package com.wenubey.pomodoroapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Entity(tableName = "pomodoro_table")
@Parcelize
data class Pomodoro(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val task_name: String,
    val createdAt: LocalDateTime,
    val workTime: Long,
):Parcelable
