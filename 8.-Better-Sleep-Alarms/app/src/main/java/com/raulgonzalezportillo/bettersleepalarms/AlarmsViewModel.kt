@file:Suppress("InlinedApi", "MemberVisibilityCanBePrivate", "MemberVisibilityCanBePrivate",
    "MemberVisibilityCanBePrivate"
)

package com.raulgonzalezportillo.bettersleepalarms

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import io.paperdb.Paper
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

private const val TAG = "AlarmsViewModel"

class AlarmsViewModel(application: Application): AndroidViewModel(application) {

    private val context by lazy { getApplication<Application>().applicationContext }

    private val alarmsModel = AlarmsModel()

    var alarmManager: AlarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

    var mondayIntent: Intent = Intent(context, AlarmReceiver::class.java)
    var mondayPendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 2, mondayIntent, PendingIntent.FLAG_MUTABLE)

    var tuesdayIntent: Intent = Intent(context, AlarmReceiver::class.java)
    var tuesdayPendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 3, tuesdayIntent, PendingIntent.FLAG_MUTABLE)

    var wednesdayIntent: Intent = Intent(context, AlarmReceiver::class.java)
    var wednesdayPendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 4, wednesdayIntent, PendingIntent.FLAG_MUTABLE)

    var thursdayIntent: Intent = Intent(context, AlarmReceiver::class.java)
    var thursdayPendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 5, thursdayIntent, PendingIntent.FLAG_MUTABLE)

    var fridayIntent: Intent = Intent(context, AlarmReceiver::class.java)
    var fridayPendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 6, fridayIntent, PendingIntent.FLAG_MUTABLE)

    var saturdayIntent: Intent = Intent(context, AlarmReceiver::class.java)
    var saturdayPendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 7, saturdayIntent, PendingIntent.FLAG_MUTABLE)

    var sundayIntent: Intent = Intent(context, AlarmReceiver::class.java)
    var sundayPendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 1, sundayIntent, PendingIntent.FLAG_MUTABLE)

    lateinit var tempAlarm: Calendar
    lateinit var currentPendingIntent: PendingIntent
    lateinit var currentAlarm: Calendar
    lateinit var currentSleepingTime: String
    lateinit var currentIdealTimeToSleep: String

    var editingAlarmForDay = 0
    var editingAlarmForHour = 0
    var editingAlarmForMinute = 0

    var minutesToSleep = 15

    var mondayAlarm: Calendar? = null
    var mondaySleepCycles = 0
    var mondaySleepingTime = ""

    var tuesdayAlarm: Calendar? = null
    var tuesdaySleepCycles = 0
    var tuesdaySleepingTime = ""

    var wednesdayAlarm: Calendar? = null
    var wednesdaySleepCycles = 0
    var wednesdaySleepingTime = ""

    var thursdayAlarm: Calendar? = null
    var thursdaySleepCycles = 0
    var thursdaySleepingTime = ""

    var fridayAlarm: Calendar? = null
    var fridaySleepCycles = 0
    var fridaySleepingTime = ""

    var saturdayAlarm: Calendar? = null
    var saturdaySleepCycles = 0
    var saturdaySleepingTime = ""

    var sundayAlarm: Calendar? = null
    var sundaySleepCycles = 0
    var sundaySleepingTime = ""

    //Initialize PaperDB and retrieve info from the database
    fun init() {
        Paper.init(context)
        updateVariables()
    }

    //Retrieves updated variables from the database
    fun updateVariables() {
        minutesToSleep = Paper.book().read("minutesToSleep", minutesToSleep)

        mondayAlarm = Paper.book().read("mondayAlarm", mondayAlarm)
        mondaySleepCycles = Paper.book().read("mondaySleepCycles", mondaySleepCycles)
        mondaySleepingTime = Paper.book().read("mondaySleepingTime", mondaySleepingTime)

        tuesdayAlarm = Paper.book().read("tuesdayAlarm", tuesdayAlarm)
        tuesdaySleepCycles = Paper.book().read("tuesdaySleepCycles", tuesdaySleepCycles)
        tuesdaySleepingTime = Paper.book().read("tuesdaySleepingTime", tuesdaySleepingTime)

        wednesdayAlarm = Paper.book().read("wednesdayAlarm", wednesdayAlarm)
        wednesdaySleepCycles = Paper.book().read("wednesdaySleepCycles", wednesdaySleepCycles)
        wednesdaySleepingTime = Paper.book().read("wednesdaySleepingTime", wednesdaySleepingTime)

        thursdayAlarm = Paper.book().read("thursdayAlarm", thursdayAlarm)
        thursdaySleepCycles = Paper.book().read("thursdaySleepCycles", thursdaySleepCycles)
        thursdaySleepingTime = Paper.book().read("thursdaySleepingTime", thursdaySleepingTime)

        fridayAlarm = Paper.book().read("fridayAlarm", fridayAlarm)
        fridaySleepCycles = Paper.book().read("fridaySleepCycles", fridaySleepCycles)
        fridaySleepingTime = Paper.book().read("fridaySleepingTime", fridaySleepingTime)

        saturdayAlarm = Paper.book().read("saturdayAlarm", saturdayAlarm)
        saturdaySleepCycles = Paper.book().read("saturdaySleepCycles", saturdaySleepCycles)
        saturdaySleepingTime = Paper.book().read("saturdaySleepingTime", saturdaySleepingTime)

        sundayAlarm = Paper.book().read("sundayAlarm", sundayAlarm)
        sundaySleepCycles = Paper.book().read("sundaySleepCycles", sundaySleepCycles)
        sundaySleepingTime = Paper.book().read("sundaySleepingTime", sundaySleepingTime)
    }

    //Dumps current variables into the database
    fun updateDatabase() {
        Paper.book().write("minutesToSleep", minutesToSleep)
        if(mondayAlarm != null) {
            Paper.book().write("mondayAlarm", mondayAlarm)
            Paper.book().write("mondaySleepCycles", mondaySleepCycles)
            Paper.book().write("mondaySleepingTime", mondaySleepingTime)
        }
        //PaperDB doesn't support saving nulls, so if it receives one, it will remove that field
        else { Paper.book().delete("mondayAlarm") }
        if(tuesdayAlarm != null) {
            Paper.book().write("tuesdayAlarm", tuesdayAlarm)
            Paper.book().write("tuesdaySleepCycles", tuesdaySleepCycles)
            Paper.book().write("tuesdaySleepingTime", tuesdaySleepingTime)
        } else { Paper.book().delete("tuesdayAlarm") }
        if(wednesdayAlarm != null) {
            Paper.book().write("wednesdayAlarm", wednesdayAlarm)
            Paper.book().write("wednesdaySleepCycles", wednesdaySleepCycles)
            Paper.book().write("wednesdaySleepingTime", wednesdaySleepingTime)
        } else { Paper.book().delete("wednesdayAlarm") }
        if(thursdayAlarm != null) {
            Paper.book().write("thursdayAlarm", thursdayAlarm)
            Paper.book().write("thursdaySleepCycles", thursdaySleepCycles)
            Paper.book().write("thursdaySleepingTime", thursdaySleepingTime)
        } else { Paper.book().delete("thursdayAlarm") }
        if(fridayAlarm != null) {
            Paper.book().write("fridayAlarm", fridayAlarm)
            Paper.book().write("fridaySleepCycles", fridaySleepCycles)
            Paper.book().write("fridaySleepingTime", fridaySleepingTime)
        } else { Paper.book().delete("fridayAlarm") }
        if(saturdayAlarm != null) {
            Paper.book().write("saturdayAlarm", saturdayAlarm)
            Paper.book().write("saturdaySleepCycles", saturdaySleepCycles)
            Paper.book().write("saturdaySleepingTime", saturdaySleepingTime)
        } else { Paper.book().delete("saturdayAlarm") }
        if(sundayAlarm != null) {
            Paper.book().write("sundayAlarm", sundayAlarm)
            Paper.book().write("sundaySleepCycles", sundaySleepCycles)
            Paper.book().write("sundaySleepingTime", sundaySleepingTime)
        } else { Paper.book().delete("sundayAlarm") }
    }

    //Initializes a Calendar temporary variable with a given day, hour and minutes, then stores it
    fun setAlarm(day: Int, hour: Int, minute: Int) {
        tempAlarm = Calendar.getInstance()
        Log.d(TAG, "Current time: $tempAlarm, today is: ${tempAlarm[Calendar.DAY_OF_WEEK]}")
        tempAlarm[Calendar.DAY_OF_WEEK] = day
        tempAlarm[Calendar.HOUR_OF_DAY] = hour
        tempAlarm[Calendar.MINUTE] = minute
        tempAlarm[Calendar.SECOND] = 0
        tempAlarm[Calendar.MILLISECOND] = 0
        when(day) {
            2 -> mondayAlarm = tempAlarm
            3 -> tuesdayAlarm = tempAlarm
            4 -> wednesdayAlarm = tempAlarm
            5 -> thursdayAlarm = tempAlarm
            6 -> fridayAlarm = tempAlarm
            7 -> saturdayAlarm = tempAlarm
            1 -> sundayAlarm = tempAlarm
        }
    }

    //Deletes the alarm set on a given day
    fun deleteAlarm(day: Int) {
        when(day) {
            2 -> {
                mondayAlarm = null
                mondaySleepCycles = 0
                mondaySleepingTime = ""
                alarmManager.cancel(mondayPendingIntent)
            }
            3 -> {
                tuesdayAlarm = null
                tuesdaySleepCycles = 0
                tuesdaySleepingTime = ""
                alarmManager.cancel(tuesdayPendingIntent)
            }
            4 -> {
                wednesdayAlarm = null
                wednesdaySleepCycles = 0
                wednesdaySleepingTime = ""
                alarmManager.cancel(wednesdayPendingIntent)
            }
            5 -> {
                thursdayAlarm = null
                thursdaySleepCycles = 0
                thursdaySleepingTime = ""
                alarmManager.cancel(thursdayPendingIntent)
            }
            6 -> {
                fridayAlarm = null
                fridaySleepCycles = 0
                fridaySleepingTime = ""
                alarmManager.cancel(fridayPendingIntent)
            }
            7 -> {
                saturdayAlarm = null
                saturdaySleepCycles = 0
                saturdaySleepingTime = ""
                alarmManager.cancel(saturdayPendingIntent)
            }
            1 -> {
                sundayAlarm = null
                sundaySleepCycles = 0
                sundaySleepingTime = ""
                alarmManager.cancel(sundayPendingIntent)
            }
        }
        updateDatabase()
    }

    //Asks model to calculate sleeping time based on the time the alarm was set and the sleep cycles provided
    fun calculateSleepingTime(sleepCycles: Int): String {
        return alarmsModel.calculateSleepingTime(minutesToSleep, sleepCycles)
    }

    fun differenceFromCurrentTime(idealTimeToSleep: String, currentTime: Calendar): Long {
        val idealTimeToSleep = LocalTime.parse(idealTimeToSleep, DateTimeFormatter.ofPattern("HH:mm"))
        val idealTimeToSleepHour = idealTimeToSleep.hour
        val idealTimeToSleepMinute = idealTimeToSleep.minute

        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentTime.get(Calendar.MINUTE)

        return (((idealTimeToSleepHour*60) + idealTimeToSleepMinute) - ((currentHour*60) + currentMinute)).toLong()
    }
}