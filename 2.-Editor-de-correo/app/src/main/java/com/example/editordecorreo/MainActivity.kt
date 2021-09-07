package com.example.editordecorreo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun editaCorreo(@Suppress("UNUSED_PARAMETER")botonRecibido: View) {
        val intento = Intent(this, NuevoCorreoActivity::class.java)
        startActivity(intento)
    }
}