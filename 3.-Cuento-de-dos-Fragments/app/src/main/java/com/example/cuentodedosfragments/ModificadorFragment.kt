package com.example.cuentodedosfragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import java.lang.ClassCastException

const val TAG = "ModificadorFragment"
class ModificadorFragment: Fragment(), SeekBar.OnSeekBarChangeListener {

    private var activityCallback: OnTextoModificadoListener? = null
    private lateinit var campoModificarTexto: EditText
    private var tamano = 10

    interface OnTextoModificadoListener {
        fun onBotonPresionado(texto: String, tamano: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            activityCallback = context as OnTextoModificadoListener
        } catch (e: ClassCastException) {
            throw ClassCastException ("$context debe implementar la interfaz OnTextoModificadoListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_modificador, container, false)
        val seekBar: SeekBar = vista.findViewById(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(this)
        val botonModificarTexto = vista.findViewById<Button>(R.id.botonModificarTexto)
        botonModificarTexto.setOnClickListener{boton: View -> modificarTexto(boton)}
        campoModificarTexto = vista.findViewById(R.id.campoModificarTexto)
        return vista
    }

    @Suppress("UNUSED_PARAMETER")
    private fun modificarTexto(boton: View) {
        activityCallback?.onBotonPresionado(campoModificarTexto.text.toString(), tamano)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        Log.d(TAG, "Posición del SeekBar: $progress")
        tamano = progress
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
        Log.d(TAG, "El usuario inició desplazamiento del SeekBar")
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        Log.d(TAG, "El usuario terminó de mover el SeekBar")
    }
}