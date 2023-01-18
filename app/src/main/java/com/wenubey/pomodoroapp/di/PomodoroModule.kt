package com.wenubey.pomodoroapp.di

import android.content.Context
import androidx.room.Room
import com.wenubey.pomodoroapp.data.local.PomodoroDao
import com.wenubey.pomodoroapp.data.local.PomodoroDatabase
import com.wenubey.pomodoroapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PomodoroModule {

    @Singleton
    @Provides
    fun providePomodoroDatabase(
        @ApplicationContext context: Context
    ): PomodoroDatabase =
        Room.databaseBuilder(context, PomodoroDatabase::class.java, Constants.DB_NAME)
            .build()

    @Singleton
    @Provides
    fun providePomodoroDao(db: PomodoroDatabase): PomodoroDao = db.pomodoroDao()
}