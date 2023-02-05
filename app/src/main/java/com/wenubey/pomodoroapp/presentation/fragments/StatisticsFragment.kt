package com.wenubey.pomodoroapp.presentation.fragments


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.wenubey.pomodoroapp.R
import com.wenubey.pomodoroapp.databinding.FragmentStatisticsBinding
import com.wenubey.pomodoroapp.presentation.viewmodel.StatisticViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsBinding

    private val viewModel: StatisticViewModel by viewModels()

    private lateinit var barDataSet: BarDataSet
    private lateinit var barData: BarData
    private lateinit var barChart: BarChart
    private var action: String = "TODAY"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false)

        initBarChart()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todayButtonClickListener()
    }

    private fun todayButtonClickListener() {
        binding.todayBtn.setOnClickListener {
            barChart.invalidate()
            viewModel.addBarEntriesToday()
            action = "TODAY"
            println("CLICKED TODAY: $action")

        }
        binding.thisWeekBtn.setOnClickListener {
            barChart.invalidate()
            viewModel.addBarEntriesThisWeek()
            action = "THIS WEEK"
            println("CLICKED THIS WEEK: $action")
        }
        binding.thisYearBtn.setOnClickListener {
            barChart.invalidate()
            viewModel.addBarEntriesThisYear()
            action = "THIS YEAR"
            println("CLICKED THIS YEAR: $action")
        }

    }


    private fun initBarChart() {

        barChart = binding.barChart
        viewModel.pomodoroChartData.observe(viewLifecycleOwner) {
            barDataSet = BarDataSet(it.first, " ")
            println("1ST LIST SIZE: ${it.first.size}")
            println("2ND LIST SIZE: ${it.second.size}")
            barData = BarData(barDataSet)
            barChart.data = barData
            barDataSet.valueTextColor = Color.BLACK
            barDataSet.color = Color.RED
            barDataSet.valueTextSize = 16f
            barChart.description.isEnabled = false
            barChart.axisRight.setDrawLabels(false)
            barChart.axisRight.setDrawGridLines(false)
            barChart.axisLeft.setDrawLabels(false)
            barChart.axisLeft.setDrawGridLines(false)
            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(it.second)
            barChart.xAxis.setDrawGridLines(false)
            barChart.xAxis.setDrawAxisLine(false)
            barChart.legend.isEnabled = false
            barChart.animateXY(500, 500)
            barChart.invalidate()
        }
    }



}