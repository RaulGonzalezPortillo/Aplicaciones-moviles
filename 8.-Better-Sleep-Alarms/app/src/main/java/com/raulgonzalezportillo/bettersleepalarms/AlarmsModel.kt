package com.raulgonzalezportillo.bettersleepalarms

class AlarmsModel {

    fun calculateSleepingTime(minutesToSleep: Int, sleepCycles: Int): String {
        var sleepingTime = ""
        var hours: Int = 0
        var minutes: Int = 0

        //increment accounts for the linear increase in length of each sleep cycle
        var increment: Int = 0
        for (i in 1..sleepCycles) {
            increment += 2 * i
        }

        //adds the minutes it takes for the user to fall asleep
        minutes += minutesToSleep
        //starts at the lower end of the length of a sleep cycle, 85 minutes
        minutes += ((sleepCycles * 85) + increment)

        //Rounds to the closes multiple of 5
        minutes = 5 * (minutes / 5)

        //Gets the hours and minutes from the total minutes calculated
        hours = minutes / 60
        minutes %= 60

        //Formats a String as HH:MM and returns it
        sleepingTime = String.format("%02d", hours) + ":" + String.format("%02d", minutes)

        return sleepingTime
    }
}