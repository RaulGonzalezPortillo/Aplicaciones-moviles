@file:Suppress("SetTextI18n")

package com.raulgonzalezportillo.bettersleepalarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

private const val TAG = "AlarmsFragment"

class AlarmsFragment : Fragment() {

    private val viewModel: AlarmsViewModel by activityViewModels()

    private lateinit var timePicker: MaterialTimePicker

    //Declare Buttons and TextViews for the different days of the week
    private lateinit var mondayAlarmTextView: TextView
    private lateinit var mondaySleepCyclesTextView: TextView
    private lateinit var mondaySleepingTimeTextView: TextView
    private lateinit var mondayEditButton: Button
    private lateinit var mondayDeleteButton: Button

    private lateinit var tuesdayAlarmTextView: TextView
    private lateinit var tuesdaySleepCyclesTextView: TextView
    private lateinit var tuesdaySleepingTimeTextView: TextView
    private lateinit var tuesdayEditButton: Button
    private lateinit var tuesdayDeleteButton: Button

    private lateinit var wednesdayAlarmTextView: TextView
    private lateinit var wednesdaySleepCyclesTextView: TextView
    private lateinit var wednesdaySleepingTimeTextView: TextView
    private lateinit var wednesdayEditButton: Button
    private lateinit var wednesdayDeleteButton: Button

    private lateinit var thursdayAlarmTextView: TextView
    private lateinit var thursdaySleepCyclesTextView: TextView
    private lateinit var thursdaySleepingTimeTextView: TextView
    private lateinit var thursdayEditButton: Button
    private lateinit var thursdayDeleteButton: Button

    private lateinit var fridayAlarmTextView: TextView
    private lateinit var fridaySleepCyclesTextView: TextView
    private lateinit var fridaySleepingTimeTextView: TextView
    private lateinit var fridayEditButton: Button
    private lateinit var fridayDeleteButton: Button

    private lateinit var saturdayAlarmTextView: TextView
    private lateinit var saturdaySleepCyclesTextView: TextView
    private lateinit var saturdaySleepingTimeTextView: TextView
    private lateinit var saturdayEditButton: Button
    private lateinit var saturdayDeleteButton: Button

    private lateinit var sundayAlarmTextView: TextView
    private lateinit var sundaySleepCyclesTextView: TextView
    private lateinit var sundaySleepingTimeTextView: TextView
    private lateinit var sundayEditButton: Button
    private lateinit var sundayDeleteButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initialize the Buttons and TextViews
        mondayAlarmTextView = view.findViewById(R.id.mondayAlarmTextView)
        mondaySleepCyclesTextView = view.findViewById(R.id.mondaySleepCyclesTextView)
        mondaySleepingTimeTextView = view.findViewById(R.id.mondaySleepingTimeTextView)
        mondayEditButton = view.findViewById(R.id.mondayEditButton)
        mondayDeleteButton = view.findViewById(R.id.mondayDeleteButton)

        tuesdayAlarmTextView = view.findViewById(R.id.tuesdayAlarmTextView)
        tuesdaySleepCyclesTextView = view.findViewById(R.id.tuesdaySleepCyclesTextView)
        tuesdaySleepingTimeTextView = view.findViewById(R.id.tuesdaySleepingTimeTextView)
        tuesdayEditButton = view.findViewById(R.id.tuesdayEditButton)
        tuesdayDeleteButton = view.findViewById(R.id.tuesdayDeleteButton)

        wednesdayAlarmTextView = view.findViewById(R.id.wednesdayAlarmTextView)
        wednesdaySleepCyclesTextView = view.findViewById(R.id.wednesdaySleepCyclesTextView)
        wednesdaySleepingTimeTextView = view.findViewById(R.id.wednesdaySleepingTimeTextView)
        wednesdayEditButton = view.findViewById(R.id.wednesdayEditButton)
        wednesdayDeleteButton = view.findViewById(R.id.wednesdayDeleteButton)

        thursdayAlarmTextView = view.findViewById(R.id.thursdayAlarmTextView)
        thursdaySleepCyclesTextView = view.findViewById(R.id.thursdaySleepCyclesTextView)
        thursdaySleepingTimeTextView = view.findViewById(R.id.thursdaySleepingTimeTextView)
        thursdayEditButton = view.findViewById(R.id.thursdayEditButton)
        thursdayDeleteButton = view.findViewById(R.id.thursdayDeleteButton)

        fridayAlarmTextView = view.findViewById(R.id.fridayAlarmTextView)
        fridaySleepCyclesTextView = view.findViewById(R.id.fridaySleepCyclesTextView)
        fridaySleepingTimeTextView = view.findViewById(R.id.fridaySleepingTimeTextView)
        fridayEditButton = view.findViewById(R.id.fridayEditButton)
        fridayDeleteButton = view.findViewById(R.id.fridayDeleteButton)

        saturdayAlarmTextView = view.findViewById(R.id.saturdayAlarmTextView)
        saturdaySleepCyclesTextView = view.findViewById(R.id.saturdaySleepCyclesTextView)
        saturdaySleepingTimeTextView = view.findViewById(R.id.saturdaySleepingTimeTextView)
        saturdayEditButton = view.findViewById(R.id.saturdayEditButton)
        saturdayDeleteButton = view.findViewById(R.id.saturdayDeleteButton)

        sundayAlarmTextView = view.findViewById(R.id.sundayAlarmTextView)
        sundaySleepCyclesTextView = view.findViewById(R.id.sundaySleepCyclesTextView)
        sundaySleepingTimeTextView = view.findViewById(R.id.sundaySleepingTimeTextView)
        sundayEditButton = view.findViewById(R.id.sundayEditButton)
        sundayDeleteButton = view.findViewById(R.id.sundayDeleteButton)

        //Adds Listeners to Edit and Delete buttons in the Alarms screen
        mondayEditButton.setOnClickListener {
            showTimePicker(2, mondayAlarmTextView, viewModel.mondayPendingIntent)
        }
        tuesdayEditButton.setOnClickListener {
            showTimePicker(3, tuesdayAlarmTextView, viewModel.tuesdayPendingIntent)
        }
        wednesdayEditButton.setOnClickListener {
            showTimePicker(4, wednesdayAlarmTextView, viewModel.wednesdayPendingIntent)
        }
        thursdayEditButton.setOnClickListener {
            showTimePicker(5, thursdayAlarmTextView, viewModel.thursdayPendingIntent)
        }
        fridayEditButton.setOnClickListener {
            showTimePicker(6, fridayAlarmTextView, viewModel.fridayPendingIntent)
        }
        saturdayEditButton.setOnClickListener {
            showTimePicker(7, saturdayAlarmTextView, viewModel.saturdayPendingIntent)
        }
        sundayEditButton.setOnClickListener {
            showTimePicker(1, sundayAlarmTextView, viewModel.sundayPendingIntent)
        }

        mondayDeleteButton.setOnClickListener {
            deleteAlarm(2)
        }
        tuesdayDeleteButton.setOnClickListener {
            deleteAlarm(3)
        }
        wednesdayDeleteButton.setOnClickListener {
            deleteAlarm(4)
        }
        thursdayDeleteButton.setOnClickListener {
            deleteAlarm(5)
        }
        fridayDeleteButton.setOnClickListener {
            deleteAlarm(6)
        }
        saturdayDeleteButton.setOnClickListener {
            deleteAlarm(7)
        }
        sundayDeleteButton.setOnClickListener {
            deleteAlarm(1)
        }
        updateAllTextViews()
    }

    //Asks the viewModel to delete an alarm in a given day and refreshes the view
    private fun deleteAlarm(day: Int) {
        viewModel.deleteAlarm(day)
        updateAllTextViews()
    }

    //Updates all TextViews with the stored alarms info
    private fun updateAllTextViews() {
        updateTextView(mondayAlarmTextView, mondaySleepCyclesTextView, mondaySleepingTimeTextView, viewModel.mondayAlarm, viewModel.mondaySleepCycles, viewModel.mondaySleepingTime)
        updateTextView(tuesdayAlarmTextView, tuesdaySleepCyclesTextView, tuesdaySleepingTimeTextView, viewModel.tuesdayAlarm, viewModel.tuesdaySleepCycles, viewModel.tuesdaySleepingTime)
        updateTextView(wednesdayAlarmTextView, wednesdaySleepCyclesTextView, wednesdaySleepingTimeTextView, viewModel.wednesdayAlarm, viewModel.wednesdaySleepCycles, viewModel.wednesdaySleepingTime)
        updateTextView(thursdayAlarmTextView, thursdaySleepCyclesTextView, thursdaySleepingTimeTextView, viewModel.thursdayAlarm, viewModel.thursdaySleepCycles, viewModel.thursdaySleepingTime)
        updateTextView(fridayAlarmTextView, fridaySleepCyclesTextView, fridaySleepingTimeTextView, viewModel.fridayAlarm, viewModel.fridaySleepCycles, viewModel.fridaySleepingTime)
        updateTextView(saturdayAlarmTextView, saturdaySleepCyclesTextView, saturdaySleepingTimeTextView, viewModel.saturdayAlarm, viewModel.saturdaySleepCycles, viewModel.saturdaySleepingTime)
        updateTextView(sundayAlarmTextView, sundaySleepCyclesTextView, sundaySleepingTimeTextView, viewModel.sundayAlarm, viewModel.sundaySleepCycles, viewModel.sundaySleepingTime)
    }

    //Retrieves info on all the alarms set and updates the TextViews
    private fun updateTextView(alarmTextView: TextView, sleepCyclesTextView: TextView, sleepingTimeTextView: TextView, alarm: Calendar?, sleepCycles: Int, sleepingTime: String) {
        //If there's an alarm for a given day
        if(alarm != null) {
            alarmTextView.setTextColor(Color.parseColor("#FFFFFFFF"))
            //Sets hour and minute of the alarm into the TextView with the format HH:MM AM/PM
            alarmTextView.text = formatTo12H(alarm[Calendar.HOUR_OF_DAY], alarm[Calendar.MINUTE])

            if(sleepCycles > 1) {
                sleepCyclesTextView.text = ("$sleepCycles cycles")
            } else {
                sleepCyclesTextView.text = ("$sleepCycles cycle")
            }

            sleepingTimeTextView.text = sleepingTime
        }
        //If there isn't
        else {
            //Put placeholders in the TextViews
            alarmTextView.text = getString(R.string.alarm_text_view_placeholder)
            alarmTextView.setTextColor(Color.parseColor("#66FFFFFF"))
            sleepCyclesTextView.text = ""
            sleepingTimeTextView.text = ""
        }
    }

    private fun showTimePicker(day: Int, alarmTextView: TextView, pendingIntent: PendingIntent) {
        timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select the time to wake up")
            .build()

        timePicker.show(parentFragmentManager, "bettersleepalarms")

        //When the user clicks accept in the hour picker
        timePicker.addOnPositiveButtonClickListener {
            //Sets hour and minute of the alarm into the TextView with the format HH:MM AM/PM
            alarmTextView.text = formatTo12H(timePicker.hour, timePicker.minute)

            //Stores the alarm info
            viewModel.setAlarm(day, timePicker.hour, timePicker.minute)

            //Sets the alarm with the given day, hour and minute
            val alarmClockInfo: AlarmManager.AlarmClockInfo = AlarmManager.AlarmClockInfo(viewModel.tempAlarm.timeInMillis, pendingIntent)
            viewModel.alarmManager.setAlarmClock(
                alarmClockInfo,
                pendingIntent
            )

            Log.d(TAG, "Received ${viewModel.tempAlarm}")

            //Send the current info to ViewModel to share with CreateAlarmsFragment
            viewModel.editingAlarmForDay = day
            viewModel.editingAlarmForHour = timePicker.hour
            viewModel.editingAlarmForMinute = timePicker.minute

            findNavController().navigate(R.id.action_alarmsFragment_to_createAlarmsFragment)
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