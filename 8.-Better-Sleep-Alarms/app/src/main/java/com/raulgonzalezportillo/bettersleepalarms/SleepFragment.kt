package com.raulgonzalezportillo.bettersleepalarms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class SleepFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sleep, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sleepNowButton = view.findViewById(R.id.cancelButton) as Button
        sleepNowButton.setOnClickListener {
            findNavController().navigate(R.id.action_sleepFragment_to_sleepingFragment)
        }
    }
}