package com.example.posesionista

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CosaFragment: Fragment() {
    private lateinit var cosa:Cosa
    private lateinit var campoNombre: EditText
    private lateinit var campoPrecio: EditText
    private lateinit var campoNumeroSerie: EditText
    private lateinit var labelFecha: TextView
    private lateinit var imagenCosa: ImageView
    private lateinit var botonCamara: ImageButton
    private lateinit var botonFecha: ImageButton
    private lateinit var botonBorrarFoto: ImageButton
    private lateinit var archivoFoto: File

    //Contrato para el registro de la respuesta de la cámara
    private var respuestaCamara = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        resultado ->
        //Si el resultado fué OK
        if(resultado.resultCode == Activity.RESULT_OK) {
            //Extrae la información del resultado
            @Suppress("UNUSED_VARIABLE")
            var datos = resultado.data
            //Asignamos la imágen tomada al ImageView de la cosa
            //imagenCosa.setImageBitmap(datos?.extras?.get("data") as Bitmap)
            imagenCosa.setImageBitmap(BitmapFactory.decodeFile(archivoFoto.absolutePath))
            botonBorrarFoto.isEnabled = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cosa = Cosa()
        //Desempaquetamos la cosa recibida
        cosa = arguments?.getParcelable("COSA_RECIBIDA")!!
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
        labelFecha = vista.findViewById(R.id.campoFecha) as TextView
        botonCamara = vista.findViewById(R.id.botonCamara)
        botonFecha = vista.findViewById(R.id.botonFecha)
        botonBorrarFoto = vista.findViewById(R.id.botonBorrarFoto)
        imagenCosa = vista.findViewById(R.id.imagenCosa) as ImageView
        //Extrae la foto guardada en disco para la cosa
        archivoFoto = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${cosa.id}.jpg")
        //Decodifica la foto y la muestra en el ImageView designado
        if(archivoFoto.exists()) {
            imagenCosa.setImageBitmap(BitmapFactory.decodeFile(archivoFoto.absolutePath))
        }
        else {
            imagenCosa.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_sin_imagen_dark))
            botonBorrarFoto.isEnabled = false
        }
        campoNombre.setText(cosa.nombre)
        campoPrecio.setText(cosa.precio.toString())
        campoNumeroSerie.setText(cosa.numeroSerie)
        labelFecha.text = cosa.fecha
        return vista
    }

    override fun onStart() {
        super.onStart()

        val botonAtrasCallback = object: OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                //Al utilizar este callback, el botón atrás no hará nada
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, botonAtrasCallback)

        //Implementamos TextWatcher para saber cuando se cambia la info. de las cosas
        val observador = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Sin acción
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Obtenemos el hash del campo recibido y comparamos para ver cual es
                if(s.hashCode() == campoNombre.text.hashCode()) {
                    //Si el nombre está vacío
                    if(s.toString().isEmpty()) {
                        //Deshabilita el botón atrás y alerta al usuario con un Toast
                        botonAtrasCallback.isEnabled = true
                        Toast.makeText(requireContext(), "No puede dejar el campo de Nombre vacío", Toast.LENGTH_SHORT).show()
                    }
                    //Si no
                    else {
                        //Habilita el botón atrás y guarda el nombre de cosa
                        botonAtrasCallback.isEnabled = false
                        cosa.nombre = s.toString()
                    }
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

        //Declaramos un apuntador a la barra de actividad
        val barraActividad = activity as AppCompatActivity
        //Cambiamos el título
        barraActividad.supportActionBar?.setTitle(R.string.tituloCosaFragment)

        //Al botón de fecha le agregamos un onClickListener
        botonFecha.apply {
            setOnClickListener {
                //Obtenemos la fecha actual
                val fechaActual = SimpleDateFormat( "dd-MM-yyyy", Locale.getDefault()).format(Date())
                //La separamos en día, mes y año
                val fechaDividida = fechaActual.split("-")
                val maxYear = fechaDividida[2].toInt()
                val maxMonth = fechaDividida[1].toInt()
                val maxDay = fechaDividida[0].toInt()

                //Creamos el DatePicker
                val dialogoDatePicker = activity?.let {vista ->
                    DatePickerDialog(vista, {_, year, month, day ->
                    //Obtenemos la nueva fecha
                        val nuevaFecha = "${day}-${month + 1}-${year}"
                        //La colocamos en pantalla
                        labelFecha.text = nuevaFecha
                        //Y la guardamos en la cosa seleccionada
                        cosa.fecha = nuevaFecha
                    //Limitamos el rango de DatePicker para que no permita escoger fechas futuras
                    }, maxYear, maxMonth, maxDay)
                }

                dialogoDatePicker?.datePicker?.maxDate = Date().time // Establecemos la fecha máxima a usar en el calendario.
                dialogoDatePicker?.show()
            }
        }

        //De igual forma, al botón de cámara le agregamos un onClickListener
        botonCamara.apply {
            setOnClickListener {
                //Creamos un nuevo intento para tomar una fotografía
                val intentTomarFoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                archivoFoto = obtenerArchivoFoto("${cosa.id}.jpg")
                val fileProvider = FileProvider.getUriForFile(context,"com.example.posesionista.fileprovider", archivoFoto)
                intentTomarFoto.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
                try {
                    //Lanzamos el intento
                    respuestaCamara.launch(intentTomarFoto)
                } catch (e: ActivityNotFoundException) {
                }
            }
        }

        //Al hacer click en el botón de borrar foto
        botonBorrarFoto.apply {
            setOnClickListener {
                //Borra la foto
                archivoFoto.delete()
                //Coloca el placeholder
                imagenCosa.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_sin_imagen_dark))
                //Deshabilita el botón de borrar foto
                botonBorrarFoto.isEnabled = false
            }
        }
    }

    private fun obtenerArchivoFoto(nombreArchivo: String): File {
        //Obtenemos la ruta del directorio de fotos
        val pathFotos = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(pathFotos, nombreArchivo)
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