package com.example.posesionista

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class CosaFragment: Fragment() {
    private lateinit var cosa:Cosa
    private lateinit var campoNombre: EditText
    private lateinit var campoValor: EditText
    private lateinit var campoNumeroSerie: EditText
    private lateinit var labelFecha: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cosa = Cosa()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.cosa_fragment, container, false)
        campoNombre = vista.findViewById(R.id.campoNombre) as EditText
        campoValor = vista.findViewById(R.id.campoValor) as EditText
        campoNumeroSerie = vista.findViewById(R.id.campoNumeroSerie) as EditText
        labelFecha = vista.findViewById(R.id.labelFecha) as TextView
        return vista
    }
}