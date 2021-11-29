package com.raulgonzalezportillo.bettersleepalarms

import android.app.AlarmManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import java.util.*

private const val TAG = "SleepingFragment"

class SleepingFragment : Fragment() {

    private val viewModel: AlarmsViewModel by activityViewModels()

    private lateinit var upcomingAlarmTextView: TextView
    private lateinit var currentTimeTextClock: TextClock
    private lateinit var cancelButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sleeping, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upcomingAlarmTextView = view.findViewById(R.id.upcomingAlarmTextView)
        currentTimeTextClock = view.findViewById(R.id.currentTimeTextClock)
        cancelButton = view.findViewById(R.id.cancelButton)

        //Cancel current alarm
        viewModel.alarmManager.cancel(viewModel.currentPendingIntent)

        val differenceInMinutes = viewModel.differenceFromCurrentTime(viewModel.currentIdealTimeToSleep, Calendar.getInstance())
        Log.d(TAG, "${Calendar.getInstance()}")
        Log.d(TAG, "$differenceInMinutes")

        val offset: Long

        if(differenceInMinutes == 0L) {
            Toast.makeText(requireContext(), "Alarm set! Have a good night ^^", Toast.LENGTH_SHORT).show()
            offset = 0L
        } else {
            offset = if(differenceInMinutes < -15L) {
                Toast.makeText(requireContext(), "Alarm adjusted by -15 minutes, Have a good night ^^", Toast.LENGTH_SHORT).show()
                -900000L
            } else {
                if(differenceInMinutes in -15..15) {
                    Toast.makeText(requireContext(), "Alarm adjusted by $differenceInMinutes minutes, Have a good night ^^", Toast.LENGTH_SHORT).show()
                    differenceInMinutes*60000
                } else {
                    Toast.makeText(requireContext(), "Alarm adjusted by 15 minutes, Have a good night ^^", Toast.LENGTH_SHORT).show()
                    900000L
                }
            }
        }

        val newAlarm: Calendar = Calendar.getInstance()
        newAlarm.timeInMillis = viewModel.currentAlarm.timeInMillis + offset

        val newAlarmClockInfo: AlarmManager.AlarmClockInfo =
            AlarmManager.AlarmClockInfo(newAlarm.timeInMillis, viewModel.currentPendingIntent)
        viewModel.alarmManager.setAlarmClock(
            newAlarmClockInfo,
            viewModel.currentPendingIntent
        )

        upcomingAlarmTextView.text = formatTo12H(newAlarm[Calendar.HOUR_OF_DAY], newAlarm[Calendar.MINUTE])

        cancelButton.setOnClickListener {

            //Restore previous alarm
            val restoreAlarmClockInfo: AlarmManager.AlarmClockInfo =
                AlarmManager.AlarmClockInfo(viewModel.currentAlarm.timeInMillis, viewModel.currentPendingIntent)
            viewModel.alarmManager.setAlarmClock(
                restoreAlarmClockInfo,
                viewModel.currentPendingIntent
            )

            findNavController().navigate(R.id.action_sleepingFragment_to_sleepFragment)
        }

    }

    //Formats a given hour a minute from 24H to 12H
    private fun formatTo12H(hours: Int, minutes: Int): String {
        lateinit var formattedString: String
        when {
            hours > 12 -> {
                formattedString =
                    String.format("%02d", hours - 12) + ":" + String.format("%02d", minutes) + " PM"
            }
            hours == 12 -> {
                formattedString =
                    String.format("%02d", hours) + ":" + String.format("%02d", minutes) + " PM"
            }
            hours < 12 -> {
                formattedString =
                    String.format("%02d", hours) + ":" + String.format("%02d", hours) + " AM"
            }
        }
        return formattedString
    }
}