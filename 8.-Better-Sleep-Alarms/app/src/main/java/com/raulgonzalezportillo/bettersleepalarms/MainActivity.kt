package com.raulgonzalezportillo.bettersleepalarms

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val viewModel: AlarmsViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        //Initialize bottomNavigationView and navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigationHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        //Initialize appBarConfiguration with the selectable fragments
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.sleepFragment, R.id.alarmsFragment, R.id.settingsFragment))
        //Add the configuration to our navController
        setupActionBarWithNavController(navController, appBarConfiguration)
        //Merge bottomNavigationView with navController
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Hide action bar
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        viewModel.init()
    }


    //For Android 8+, it's necessary to create a notification channel with the app info
    private fun createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name: CharSequence = "BetterSleepAlarmsChannel"
            val description = "Alarm manager channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("bettersleepalarms", name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(channel)
        }
    }
}