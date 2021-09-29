package com.example.editordecorreo

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class NuevoCorreoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_correo)
        val campoEditarEmail = findViewById<EditText>(R.id.campoEditarEmail)
        campoEditarEmail.setText(intent.getStringExtra("EMAIL_ACTUAL"))
    }

    fun aceptar(@Suppress("UNUSED_PARAMETER") boton: View) {
        val nuevoEmail = findViewById<EditText>(R.id.campoEditarEmail).text.toString()
        if (nuevoEmail.isEmpty()) {
            Toast.makeText(this, "El nuevo email no puede estar vacío", Toast.LENGTH_SHORT).show()
            return
        }
        val datos = Intent().apply {
            putExtra("EMAIL_NUEVO", nuevoEmail)
        }
        setResult(Activity.RESULT_OK, datos)
        finish()
    }

    fun cancelar(@Suppress("UNUSED_PARAMETER") boton: View) {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    companion object {
        fun nuevaInstancia(contexto: Context, emailActual: String): Intent {
            return Intent(contexto, NuevoCorreoActivity::class.java).apply {
                //Agrega información extra al intento
                putExtra("EMAIL_ACTUAL", emailActual)
            }
        }
    }
}