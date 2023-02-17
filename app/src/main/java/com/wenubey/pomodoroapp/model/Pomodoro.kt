package com.wenubey.pomodoroapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Entity(tableName = "pomodoro_table")
@Parcelize
data class Pomodoro(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val id: Int? = null,
    @ColumnInfo(name = "TASK_NAME")
    val task_name: String,
    @ColumnInfo(name = "CREATED_AT")
    val createdAt: LocalDateTime,
    @ColumnInfo(name = "WORK_TIME")
    val workTime: Long,
):Parcelable
