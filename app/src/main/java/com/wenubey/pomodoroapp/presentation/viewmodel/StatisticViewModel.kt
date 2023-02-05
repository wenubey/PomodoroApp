package com.wenubey.pomodoroapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.wenubey.pomodoroapp.data.repository.RepositoryImpl
import com.wenubey.pomodoroapp.model.Pomodoro
import com.wenubey.pomodoroapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel() {

    private var pomodoroList = mutableListOf<Pomodoro>()
    private var barEntries: ArrayList<BarEntry> = arrayListOf()
    private var labels: ArrayList<String> = arrayListOf()

    private var _pomodoroListLiveData = MutableLiveData<List<Pomodoro>>()
    val pomodoroListLiveData: LiveData<List<Pomodoro>> get() = _pomodoroListLiveData

    private var _pomodoroBarEntriesLiveData = MutableLiveData<List<BarEntry>>()

    private var _pomodoroLabelsLiveData = MutableLiveData<List<String>>()

    val pomodoroChartData = MediatorLiveData<Pair<List<BarEntry>, List<String>>>()

    val pomodoroData = MutableLiveData<Pair<List<BarEntry>, List<String>>>()

    init {
        pomodoroChartData.addSource(_pomodoroBarEntriesLiveData) { value ->
            pomodoroChartData.value = Pair(value, labels)
        }
        pomodoroChartData.addSource(_pomodoroLabelsLiveData) { value ->
            pomodoroChartData.value = Pair(barEntries, value)
        }
    }
    fun deleteAllPomodoro() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                repository.deleteAll()
            }
        } catch (e: Exception) {
            Log.d(Constants.TAG, "deleteAllPomodoro: ${e.message}")
        }
    }

    fun addBarEntriesToday() {
        try {
            viewModelScope.launch(Dispatchers.Main) {
                clearData()
                pomodoroList = repository.getTodayPomodoro().toMutableList()
                pomodoroList.forEachIndexed { index, pomodoro ->
                    barEntries.add(
                        BarEntry(
                            index.toFloat(),
                            pomodoro.workTime.toFloat()
                        )
                    )
                    labels.add(pomodoro.createdAt.format(DateTimeFormatter.ofPattern("MM/dd")))
                }
                pomodoroChartData.postValue(Pair(barEntries, labels))
                pomodoroData.postValue(Pair(barEntries, labels))
            }
        } catch (e: Exception) {
            Log.d(Constants.TAG, "addBarEntriesToday: ${e.message}")
        }
    }

    fun addBarEntriesThisWeek() {
        try {
            viewModelScope.launch(Dispatchers.Main) {
                clearData()
                pomodoroList = repository.getThisWeekPomodoro().toMutableList()
                pomodoroList.forEachIndexed { index, pomodoro ->
                    barEntries.add(
                        BarEntry(
                            index.toFloat(),
                            pomodoro.workTime.toFloat()
                        )
                    )
                    labels.add(pomodoro.createdAt.format(DateTimeFormatter.ofPattern("MM/dd")))
                }
                pomodoroChartData.postValue(Pair(barEntries, labels))

            }
        } catch (e: Exception) {
            Log.d(Constants.TAG, "addBarEntriesToday: ${e.message}")
        }
    }
    fun addBarEntriesThisYear() {
        try {
            viewModelScope.launch(Dispatchers.Main) {
                clearData()
                pomodoroList = repository.getThisYearPomodoro().toMutableList()
                pomodoroList.forEachIndexed { index, pomodoro ->
                    barEntries.add(
                        BarEntry(
                            index.toFloat(),
                            pomodoro.workTime.toFloat()
                        )
                    )
                    labels.add(pomodoro.createdAt.format(DateTimeFormatter.ofPattern("MM/dd")))
                }
                pomodoroChartData.postValue(Pair(barEntries, labels))

            }
        } catch (e: Exception) {
            Log.d(Constants.TAG, "addBarEntriesToday: ${e.message}")
        }
    }

    private fun clearData() {
        pomodoroList.clear()
        barEntries.clear()
        labels.clear()
    }
    fun addDummyPomodoro() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                repository.insertAndUpdate(
                    Pomodoro(
                        task_name = "Read Book",
                        createdAt = LocalDateTime.now().plusDays(1),
                        workTime = 1000
                    )
                )
                repository.insertAndUpdate(
                    Pomodoro(
                        task_name = "Read Comic",
                        createdAt = LocalDateTime.now().plusMonths(1),
                        workTime = 2000
                    )
                )
                repository.insertAndUpdate(
                    Pomodoro(
                        task_name = "Read Newspaper",
                        createdAt = LocalDateTime.now(),
                        workTime = 2500
                    )
                )
                repository.insertAndUpdate(
                    Pomodoro(
                        task_name = "Read Newspaper",
                        createdAt = LocalDateTime.now().minusDays(1),
                        workTime = 2500
                    )
                )
                repository.insertAndUpdate(
                    Pomodoro(
                        task_name = "Read Newspaper",
                        createdAt = LocalDateTime.now().minusDays(2),
                        workTime = 2500
                    )
                )

            }
        } catch (e: Exception) {
            Log.d(Constants.TAG, "insertAndUpdatePomodoro: ${e.message}")
        }
    }
}