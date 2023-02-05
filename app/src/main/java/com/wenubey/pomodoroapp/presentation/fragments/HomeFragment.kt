package com.wenubey.pomodoroapp.presentation.fragments


import android.content.Context
import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.os.VibratorManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wenubey.pomodoroapp.databinding.FragmentHomeBinding
import com.wenubey.pomodoroapp.model.Pomodoro
import com.wenubey.pomodoroapp.presentation.viewmodel.HomeViewModel
import com.wenubey.pomodoroapp.utils.Constants
import com.wenubey.pomodoroapp.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pomodoroStartStopClickListener()
        addPomodoroToDb()
        pomodoroResetClickListener()
        goToStatisticsFragmentListener()

    }

    private fun goToStatisticsFragmentListener() {
        binding.buttonStats.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToStatisticsFragment()
            findNavController().navigate(action)

        }
    }

    private fun editTextNullCheck(): Boolean {
        return if (binding.taskNameEditText.text.isNullOrBlank()) {
            Toast.makeText(context, "You have to enter task name", Toast.LENGTH_SHORT)
                .show()
            true
        } else {
            false
        }
    }

    private fun pomodoroStartStopClickListener() {
        binding.pomodoroStartStopButton.setOnClickListener {
            viewModel.workTimerFinished.observe(viewLifecycleOwner) {
                lifecycleScope.launch {
                    if (it == true) {
                        binding.timerTextWorkBreak.text = "BREAK"
                        binding.pomodoroStartStopButton.text = "START"
                        delay(3000L)
                        viewModel.startTimerForWork(Constants.BREAK_TIME)
                    }
                }

            }
            when (binding.pomodoroStartStopButton.text) {
                "START" -> {
                    if (editTextNullCheck()) {
                        return@setOnClickListener
                    } else {
                        viewModel.startTimerForWork(Constants.WORK_TIME)
                        viewModel.pomodoroTextLiveData.observe(viewLifecycleOwner) {
                            binding.timerText.text = it
                        }
                        binding.timerTextWorkBreak.text = "WORK"
                        binding.pomodoroStartStopButton.text = "STOP"
                    }

                }
                "STOP" -> {
                    viewModel.stopCountDownTimer()
                    binding.pomodoroStartStopButton.text = "RESUME"
                }
                "RESUME" -> {
                    if (editTextNullCheck()) {
                        return@setOnClickListener
                    } else {
                        viewModel.resumeCountDownTimer()
                        binding.pomodoroStartStopButton.text = "STOP"
                    }
                }
            }
        }

    }

    private fun pomodoroResetClickListener() {
        binding.pomodoroResetButton.setOnClickListener {
            editTextNullCheck()
            viewModel.resetCountDownTimer()
            binding.pomodoroStartStopButton.text = "START"
            binding.timerTextWorkBreak.text = "WORK"
            binding.timerText.text = "00:25:00"
        }
    }

    private fun addPomodoroToDb() {
        viewModel.pomodoroRepeatTime.observe(viewLifecycleOwner) {
            if (it != 0) {
                makeToast(context = requireContext(), message = "You have finished $it times")
            }
            if (it == 4) {
                viewModel.insertAndUpdatePomodoro(
                    Pomodoro(
                        task_name = binding.taskNameEditText.text.toString(),
                        createdAt = LocalDateTime.now(),
                        workTime = Constants.WORK_TIME
                    )
                )
            }
        }
    }


}