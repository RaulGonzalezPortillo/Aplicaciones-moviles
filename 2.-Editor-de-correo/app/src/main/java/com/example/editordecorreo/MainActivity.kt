package com.example.editordecorreo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    var ejecutaParaEsperarRespuesta = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        respuesta ->

        if (respuesta.resultCode == Activity.RESULT_OK) {
            val datos: Intent? = respuesta.data
            findViewById<TextView>(R.id.labelEmail).text = datos?.getStringExtra("EMAIL_NUEVO")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun editaCorreo(@Suppress("UNUSED_PARAMETER")botonRecibido: View) {
        val campoEmail = findViewById<TextView>(R.id.labelEmail)
        var emailActual = campoEmail.text.toString()
        val intento = NuevoCorreoActivity.nuevaInstancia(this, emailActual)
        ejecutaParaEsperarRespuesta.launch(intento)
    }
}