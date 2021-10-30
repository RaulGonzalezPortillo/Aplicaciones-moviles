package com.example.hipnosis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ActividadHipnosis : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_hipnosis)
        val vistaHipnosis = VistaHipnosis(this)
        supportActionBar?.hide()
    }
}