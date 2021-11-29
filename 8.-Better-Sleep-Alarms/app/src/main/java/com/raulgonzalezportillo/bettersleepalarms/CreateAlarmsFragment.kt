@file:Suppress("SetTextI18n")

package com.raulgonzalezportillo.bettersleepalarms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

class CreateAlarmsFragment : Fragment() {

    private val viewModel: AlarmsViewModel by activityViewModels()

    lateinit var numberPicker: NumberPicker
    lateinit var currentSleepingTimeTextView: TextView
    lateinit var acceptButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_alarms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var sleepingTime: String

        numberPicker = view.findViewById(R.id.numberPicker)
        currentSleepingTimeTextView = view.findViewById(R.id.currentSleepingTimeTextView)
        acceptButton = view.findViewById(R.id.acceptButton)

        numberPicker.minValue = 1
        numberPicker.maxValue = 7

        //Initial sleeping time calculation (before touching numberPicker)
        sleepingTime = viewModel.calculateSleepingTime(numberPicker.value)
        currentSleepingTimeTextView.text = "$sleepingTime hours"

        //Listen to changes in numberPicker and update sleeping time estimates accordingly
        numberPicker.setOnValueChangedListener { _, _, _ ->
            sleepingTime = viewModel.calculateSleepingTime(numberPicker.value)
            currentSleepingTimeTextView.text = "$sleepingTime hours"
        }
        
        acceptButton.setOnClickListener {
            when(viewModel.editingAlarmForDay) {
                1 -> {
                    viewModel.sundaySleepCycles = numberPicker.value
                    viewModel.sundaySleepingTime = sleepingTime
                }
                2 -> {
                    viewModel.mondaySleepCycles = numberPicker.value
                    viewModel.mondaySleepingTime = sleepingTime
                }
                3 -> {
                    viewModel.tuesdaySleepCycles = numberPicker.value
                    viewModel.tuesdaySleepingTime = sleepingTime
                }
                4 -> {
                    viewModel.wednesdaySleepCycles = numberPicker.value
                    viewModel.wednesdaySleepingTime = sleepingTime
                }
                5 -> {
                    viewModel.thursdaySleepCycles = numberPicker.value
                    viewModel.thursdaySleepingTime = sleepingTime
                }
                6 -> {
                    viewModel.fridaySleepCycles = numberPicker.value
                    viewModel.fridaySleepingTime = sleepingTime
                }
                7 -> {
                    viewModel.saturdaySleepCycles = numberPicker.value
                    viewModel.saturdaySleepingTime = sleepingTime
                }
            }
            viewModel.editingAlarmForDay = 0
            viewModel.editingAlarmForHour = 0
            viewModel.editingAlarmForMinute = 0
            viewModel.updateDatabase()
            findNavController().navigate(R.id.action_createAlarmsFragment_to_alarmsFragment)
            Toast.makeText(requireContext(), "Alarm set successfully", Toast.LENGTH_SHORT).show()
        }
    }
}