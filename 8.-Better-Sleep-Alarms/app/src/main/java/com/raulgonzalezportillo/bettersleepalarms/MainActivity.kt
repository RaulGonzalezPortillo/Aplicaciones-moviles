package com.raulgonzalezportillo.bettersleepalarms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        //Initialize bottomNavigationView and navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.navigationHostFragment)
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
    }
}