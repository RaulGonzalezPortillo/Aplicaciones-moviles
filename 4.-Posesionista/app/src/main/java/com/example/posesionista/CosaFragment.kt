package com.example.posesionista

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.*

class CosaFragment: Fragment() {
    private lateinit var cosa:Cosa
    private lateinit var campoNombre: EditText
    private lateinit var campoPrecio: EditText
    private lateinit var campoNumeroSerie: EditText
    private lateinit var labelFecha: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cosa = Cosa()
        //Desempaquetamos la cosa recibida
        cosa = arguments?.getParcelable("COSA_RECIBIDA")!!
    }

    override fun onStart() {
        super.onStart()

        //Implementamos TextWatcher para saber cuando se cambia la info. de las cosas
        val observador = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Sin acción
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Obtenemos el hash del campo recibido y comparamos para ver cual es
                if(s.hashCode() == campoNombre.text.hashCode()) {
                    cosa.nombre = s.toString()
                }
                if(s.hashCode() == campoPrecio.text.hashCode()) {
                    if(s != null) {
                        if(s.isEmpty()) {
                            cosa.precio = 0
                        }
                        else {
                            cosa.precio = s.toString().toInt()
                        }
                    }
                }
                if(s.hashCode() == campoNumeroSerie.text.hashCode()) {
                    cosa.numeroSerie = s.toString()
                }
            }
            override fun afterTextChanged(s: Editable?) {
                //Sin acción
            }
        }

        campoNombre.addTextChangedListener(observador)
        campoPrecio.addTextChangedListener(observador)
        campoNumeroSerie.addTextChangedListener(observador)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.cosa_fragment, container, false)
        campoNombre = vista.findViewById(R.id.campoNombre) as EditText
        campoPrecio = vista.findViewById(R.id.campoPrecio) as EditText
        campoNumeroSerie = vista.findViewById(R.id.campoNumeroSerie) as EditText
        labelFecha = vista.findViewById(R.id.labelFecha) as TextView
        campoNombre.setText(cosa.nombre)
        campoPrecio.setText(cosa.precio.toString())
        campoNumeroSerie.setText(cosa.numeroSerie.toString())
        labelFecha.text = cosa.fecha.toString()
        return vista
    }

    companion object {
        fun nuevaInstancia(cosa: Cosa): CosaFragment {
            val argumentos = Bundle().apply {
                putParcelable("COSA_RECIBIDA", cosa)
            }
            return CosaFragment().apply {
                arguments = argumentos
            }
        }
    }
}