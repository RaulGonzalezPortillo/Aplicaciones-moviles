package com.example.cuentodedosfragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

const val TAG2 = "MainActivity"
class MainActivity : AppCompatActivity(), ModificadorFragment.OnTextoModificadoListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBotonPresionado(texto: String, tamano: Int) {
        Log.d(TAG2, "Se recibió el texto $texto y el tamaño $tamano")
        val textoFragment = supportFragmentManager.findFragmentById(R.id.fragmentoTexto) as TextoFragment
        textoFragment.modificarTexto(texto, tamano)
    }
}