@file:Suppress("SetTextI18n")

package com.raulgonzalezportillo.bettersleepalarms

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class SleepFragment : Fragment() {

    private val viewModel: AlarmsViewModel by activityViewModels()

    private lateinit var sleepNowButton: Button
    private lateinit var nextAlarmTextView: TextView
    private lateinit var nextAlarmDayTextView: TextView
    private lateinit var idealTimeToSleepTextView: TextView
    private lateinit var nextAlarmCalendar: Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sleep, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sleepNowButton = view.findViewById(R.id.sleepNowButton)
        nextAlarmTextView = view.findViewById(R.id.nextAlarmTextView)
        nextAlarmDayTextView = view.findViewById(R.id.nextAlarmDayTextView)
        idealTimeToSleepTextView = view.findViewById(R.id.idealTimeToSleepTextView)

        nextAlarmCalendar = Calendar.getInstance()

        if(viewModel.alarmManager.nextAlarmClock != null) {
            nextAlarmCalendar.timeInMillis = viewModel.alarmManager.nextAlarmClock.triggerTime

            nextAlarmTextView.text = formatTo12H(nextAlarmCalendar[Calendar.HOUR_OF_DAY], nextAlarmCalendar[Calendar.MINUTE])

            //Retrieve upcoming alarm info
            when(nextAlarmCalendar[Calendar.DAY_OF_WEEK]) {
                1 -> {
                    nextAlarmDayTextView.text = "Sunday"
                    idealTimeToSleepTextView.text = idealTimeToSleep(viewModel.sundayAlarm, viewModel.sundaySleepingTime)
                    viewModel.currentPendingIntent = viewModel.sundayPendingIntent
                    viewModel.currentAlarm = viewModel.sundayAlarm!!
                    viewModel.currentSleepingTime = viewModel.sundaySleepingTime
                }
                2 -> {
                    nextAlarmDayTextView.text = "Monday"
                    idealTimeToSleepTextView.text = idealTimeToSleep(viewModel.mondayAlarm, viewModel.mondaySleepingTime)
                    viewModel.currentPendingIntent = viewModel.mondayPendingIntent
                    viewModel.currentAlarm = viewModel.mondayAlarm!!
                    viewModel.currentSleepingTime = viewModel.mondaySleepingTime
                }
                3 -> {
                    nextAlarmDayTextView.text = "Tuesday"
                    idealTimeToSleepTextView.text = idealTimeToSleep(viewModel.tuesdayAlarm, viewModel.tuesdaySleepingTime)
                    viewModel.currentPendingIntent = viewModel.tuesdayPendingIntent
                    viewModel.currentAlarm = viewModel.tuesdayAlarm!!
                    viewModel.currentSleepingTime = viewModel.tuesdaySleepingTime
                }
                4 -> {
                    nextAlarmDayTextView.text = "Wednesday"
                    idealTimeToSleepTextView.text = idealTimeToSleep(viewModel.wednesdayAlarm, viewModel.wednesdaySleepingTime)
                    viewModel.currentPendingIntent = viewModel.wednesdayPendingIntent
                    viewModel.currentAlarm = viewModel.wednesdayAlarm!!
                    viewModel.currentSleepingTime = viewModel.wednesdaySleepingTime
                }
                5 -> {
                    nextAlarmDayTextView.text = "Thursday"
                    idealTimeToSleepTextView.text = idealTimeToSleep(viewModel.thursdayAlarm, viewModel.thursdaySleepingTime)
                    viewModel.currentPendingIntent = viewModel.thursdayPendingIntent
                    viewModel.currentAlarm = viewModel.thursdayAlarm!!
                    viewModel.currentSleepingTime = viewModel.thursdaySleepingTime
                }
                6 -> {
                    nextAlarmDayTextView.text = "Friday"
                    idealTimeToSleepTextView.text = idealTimeToSleep(viewModel.fridayAlarm, viewModel.fridaySleepingTime)
                    viewModel.currentPendingIntent = viewModel.fridayPendingIntent
                    viewModel.currentAlarm = viewModel.fridayAlarm!!
                    viewModel.currentSleepingTime = viewModel.fridaySleepingTime
                }
                7 -> {
                    nextAlarmDayTextView.text = "Saturday"
                    idealTimeToSleepTextView.text = idealTimeToSleep(viewModel.saturdayAlarm, viewModel.saturdaySleepingTime)
                    viewModel.currentPendingIntent = viewModel.saturdayPendingIntent
                    viewModel.currentAlarm = viewModel.saturdayAlarm!!
                    viewModel.currentSleepingTime = viewModel.saturdaySleepingTime
                }
            }
            viewModel.currentIdealTimeToSleep = idealTimeToSleepTextView.text.toString().dropLast(3)
        } else {
            nextAlarmTextView.text = "--:--"
            idealTimeToSleepTextView.text = "--:--"
        }

        sleepNowButton.setOnClickListener {
            if (viewModel.alarmManager.nextAlarmClock != null) {
                sleepNow()
            }
            else {
                Toast.makeText(requireContext(), "Go to alarms and set an alarm first :D", Toast.LENGTH_SHORT).show()
            }
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
                    String.format("%02d", hours) + ":" + String.format("%02d", minutes) + " AM"
            }
        }
        return formattedString
    }

    private fun sleepNow() {
        findNavController().navigate(R.id.action_sleepFragment_to_sleepingFragment)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun idealTimeToSleep(alarmCalendar: Calendar?, sleepingTime: String): String {
        val alarmHour = alarmCalendar?.get(Calendar.HOUR_OF_DAY)
        val alarmMinute = alarmCalendar?.get(Calendar.MINUTE)

        val parsedSleepingTime = LocalTime.parse(sleepingTime, DateTimeFormatter.ofPattern("HH:mm"))
        val sleepingTimeHour = parsedSleepingTime.hour
        val sleepingTimeMinute = parsedSleepingTime.minute

        var idealHourToSleep = alarmHour?.minus(sleepingTimeHour)
        if (idealHourToSleep!! < 0)
            idealHourToSleep += 24

        var idealMinutesToSleep = alarmMinute?.minus(sleepingTimeMinute)
        if (idealMinutesToSleep!! < 0) {
            idealMinutesToSleep += 60
            idealHourToSleep - 1
        }

        return formatTo12H(idealHourToSleep, idealMinutesToSleep)
    }
}