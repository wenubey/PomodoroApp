package com.wenubey.pomodoroapp.presentation.viewmodel

import android.content.Context
import android.os.*
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenubey.pomodoroapp.data.repository.RepositoryImpl
import com.wenubey.pomodoroapp.model.Pomodoro
import com.wenubey.pomodoroapp.utils.Constants
import com.wenubey.pomodoroapp.utils.VibratorHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RepositoryImpl,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private var countDownTimer: CountDownTimer? = null
    var remainingTime: Long = 1 * 60 * 100L
    var pausedTime: Long = 0L
    var repeatTime: Int = 0
    private val vibrator = VibratorHelper.from(context)

    private var _pomodoroTextLiveData = MutableLiveData<String>()
    val pomodoroTextLiveData: LiveData<String> get() = _pomodoroTextLiveData

    private var _workTimerFinished = MutableLiveData(false)
    val workTimerFinished: LiveData<Boolean> get() = _workTimerFinished

    private var _pomodoroRepeatTime = MutableLiveData(0)
    val pomodoroRepeatTime: LiveData<Int> get() = _pomodoroRepeatTime



    fun insertAndUpdatePomodoro(pomodoro: Pomodoro) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                repository.insertAndUpdate(pomodoro)
            }
        } catch (e: Exception) {
            Log.d(Constants.TAG, "insertAndUpdatePomodoro: ${e.message}")
        }
    }

    private fun startCountDownTimer(duration: Long) {
        countDownTimer = object : CountDownTimer(duration, Constants.INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                pausedTime = millisUntilFinished
                _pomodoroTextLiveData.postValue(formatTimeValue(millisUntilFinished))
            }

            override fun onFinish() {
                if (duration != Constants.WORK_TIME) {
                    _workTimerFinished.postValue(false)
                } else {
                    _workTimerFinished.postValue(true)
                    repeatTime += 1
                    _pomodoroRepeatTime.postValue(repeatTime)
                }
                vibrate()
            }
        }
        countDownTimer!!.start()
    }




    fun stopCountDownTimer() {
        _workTimerFinished.postValue(false)
        countDownTimer?.cancel()
    }

    fun resumeCountDownTimer() {
        startCountDownTimer(pausedTime)
    }

    fun resetCountDownTimer() {
        countDownTimer?.cancel()
    }

    private fun formatNumber(value: Long): String {
        if (value < 10)
            return "0$value"
        return "$value"
    }

    private fun formatTimeValue(time: Long): String {
        var seconds = time / 1000
        var minutes = seconds / 60
        val hours = minutes / 60
        if (minutes > 0)
            seconds %= 60
        if (hours > 0)
            minutes %= 60
        return formatNumber(hours) + ":" + formatNumber(minutes) + ":" +
                formatNumber(seconds)
    }

    fun startTimerForWork(duration: Long) {
        startCountDownTimer(duration)
    }

    private fun vibrate() {
        vibrator?.vibrate()
    }



}