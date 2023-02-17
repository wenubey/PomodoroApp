package com.wenubey.pomodoroapp.presentation.fragments


import android.app.AlertDialog
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
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.wenubey.pomodoroapp.databinding.FragmentStatisticsBinding
import com.wenubey.pomodoroapp.presentation.viewmodel.StatisticViewModel
import com.wenubey.pomodoroapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsBinding

    private val viewModel: StatisticViewModel by viewModels()

    private lateinit var barDataSet: BarDataSet
    private lateinit var barData: BarData
    private lateinit var barChart: BarChart
    private lateinit var legend: Legend

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
        deleteFabButtonClickListener()
    }

    private fun deleteFabButtonClickListener() {
        binding.deleteFabButton.setOnClickListener {
            buildAlertDialog()
        }
    }

    private fun todayButtonClickListener() {
        binding.todayBtn.setOnClickListener {
            viewModel.addBarEntriesToday()
        }
        binding.thisWeekBtn.setOnClickListener {
            viewModel.addBarEntriesThisWeek()
        }
        binding.thisMonthBtn.setOnClickListener {
            viewModel.addBarEntriesThisMonth()
        }
        binding.thisYearBtn.setOnClickListener {
            viewModel.addBarEntriesThisYear()
        }
    }

    private fun initBarChart() {
        barChart = binding.barChart
        setLegend()
        viewModel.pomodoroChartData.observe(viewLifecycleOwner) {
            barDataSet = BarDataSet(it.first, "minute")
            barData = BarData(barDataSet)
            barData.setDrawValues(false)

            barChart.data = barData
            barDataSet.valueTextColor = Color.BLACK
            barDataSet.color = Color.RED
            barDataSet.valueTextSize = 16f
            barChart.description.isEnabled = false
            barChart.axisRight.setDrawLabels(false)
            barChart.axisRight.setDrawGridLines(false)
            barChart.axisLeft.setDrawLabels(true)
            barChart.axisLeft.axisMinimum = 0f
            barChart.axisLeft.setDrawGridLines(false)
            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(it.second)
            barChart.xAxis.setDrawGridLines(false)
            barChart.xAxis.setDrawAxisLine(false)
            barChart.animateXY(500, 500)
            barChart.invalidate()
        }
    }

    private fun setLegend() {
        legend = barChart.legend
        val list = mutableListOf<LegendEntry>()
        val legendEntry = LegendEntry()
        legendEntry.label = "Minute"
        legendEntry.formColor = Constants.PRIMARY_RED.toInt()
        list.add(
            legendEntry
        )
        legend.setCustom(list)
    }

    private fun buildAlertDialog(){
        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setMessage("Are you sure to delete all data ?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteAllPomodoro()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        dialogBuilder.setTitle("Delete all")
        dialogBuilder.show()
    }

}