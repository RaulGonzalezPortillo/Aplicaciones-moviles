@file:Suppress("InlinedApi")

package com.raulgonzalezportillo.bettersleepalarms

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

//This BroadcastReceiver listens for alarms, even if the application is not running. Includes a notification builder to configure the alarms.
class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context, MainActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 1, i, PendingIntent.FLAG_MUTABLE)

        val builder = NotificationCompat.Builder(context!!, "bettersleepalarms")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Better Sleep Alarms")
            .setContentText("Good morning!")
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123, builder.build())
    }
}