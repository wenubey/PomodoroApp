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
import kotlin.math.roundToInt

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel() {

    private var pomodoroList = mutableListOf<Pomodoro>()
    private var barEntries: ArrayList<BarEntry> = arrayListOf()
    private var labels: ArrayList<String> = arrayListOf()

    private var _pomodoroBarEntriesLiveData = MutableLiveData<List<BarEntry>>()
    private var _pomodoroLabelsLiveData = MutableLiveData<List<String>>()
    private var _pomodoroChartData = MediatorLiveData<Pair<List<BarEntry>, List<String>>>()
    val pomodoroChartData: MediatorLiveData<Pair<List<BarEntry>, List<String>>> get() = _pomodoroChartData


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
                barEntries.add(
                    BarEntry(
                        0f,
                        repository.getTotalToday()
                    )
                )
                addDummyPomodoro()
                labels.add(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd")))
                _pomodoroChartData.postValue(Pair(barEntries, labels))
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
                pomodoroList.sortBy {
                    it.createdAt.dayOfYear
                }
                pomodoroList.forEachIndexed { index, pomodoro ->
                    barEntries.add(
                        BarEntry(
                            index.toFloat(),
                            pomodoro.workTime / 1000f
                        )
                    )
                    labels.add(pomodoro.createdAt.format(DateTimeFormatter.ofPattern("MM/dd")))
                }
                _pomodoroChartData.postValue(Pair(barEntries, labels))

            }
        } catch (e: Exception) {
            Log.d(Constants.TAG, "addBarEntriesToday: ${e.message}")
        }
    }

    fun addBarEntriesThisMonth() {
        try {
            viewModelScope.launch(Dispatchers.Main) {
                clearData()
                pomodoroList = repository.getThisMonthPomodoro().toMutableList()
                pomodoroList.forEachIndexed { index, pomodoro ->
                    barEntries.add(
                        BarEntry(
                            index.toFloat(),
                            pomodoro.workTime.toFloat() / 1000f
                        )
                    )
                    labels.add(pomodoro.createdAt.format(DateTimeFormatter.ofPattern("MM/dd")))
                }
            }
        } catch (e: Exception) {
            Log.d(Constants.TAG, "addBarEntriesThisMonth: ${e.message}")
        }
    }

    fun addBarEntriesThisYear() {
        try {
            viewModelScope.launch(Dispatchers.Main) {
                clearData()
                pomodoroList = repository.getThisYearPomodoro().toMutableList()
                pomodoroList.sortBy {
                    it.createdAt.dayOfYear
                }
                pomodoroList.forEachIndexed { index, pomodoro ->
                    barEntries.add(
                        BarEntry(
                            index.toFloat(),
                            pomodoro.workTime.toFloat() / 1000f
                        )
                    )
                    labels.add(pomodoro.createdAt.format(DateTimeFormatter.ofPattern("MM/dd")))
                }
                _pomodoroChartData.postValue(Pair(barEntries, labels))


            }
        } catch (e: Exception) {
            Log.d(Constants.TAG, "addBarEntriesThisYear: ${e.message}")
        }
    }

    private fun clearData() {
        pomodoroList.clear()
        barEntries.clear()
        labels.clear()
    }

    fun addDummyPomodoro() {
        val list = listOf(BarEntry(1f, 0f,), BarEntry(2f, 0f), BarEntry(3f, 0f))
        list.forEach {
            barEntries.add(it)
        }
    }
}