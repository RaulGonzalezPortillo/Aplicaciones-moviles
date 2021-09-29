package com.example.posesionista

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Buscamos el fragment_container por medio del Fragment Manager
        val fragmentoActual = supportFragmentManager.findFragmentById(R.id.fragment_container)
        //Si el fragmento no ha sido inicializado, lo añade
        if(fragmentoActual == null) {
            val fragmento = TablaCosasFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragmento)
                .commit()
        }
    }
}