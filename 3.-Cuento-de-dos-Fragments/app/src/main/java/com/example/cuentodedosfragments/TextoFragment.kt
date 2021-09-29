package com.example.cuentodedosfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class TextoFragment: Fragment() {
    private lateinit var labelTexto: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_texto, container, false)
        labelTexto = vista.findViewById(R.id.labelTexto)
        return vista
    }
    fun modificarTexto(texto: String, tamano: Int) {
        labelTexto.textSize = tamano.toFloat()
        labelTexto.text = texto
    }
}