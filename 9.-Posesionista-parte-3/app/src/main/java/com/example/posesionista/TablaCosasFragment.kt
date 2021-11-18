@file:Suppress("DEPRECATION", "UNUSED", "SetTextI18n", "NotifyDataSetChanged")

package com.example.posesionista

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Environment
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.*

private const val TAG = "TablaCosasFragment"

class TablaCosasFragment: Fragment() {
    private lateinit var totalCosasTextView: TextView
    private lateinit var sumaTotalPreciosTextView: TextView

    private var callbackInterfaz: InterfazTablaCosas? = null
    private var inventarioAdaptador: InventarioAdaptador? = null
    private lateinit var inventarioRecyclerView: RecyclerView

    private val tablaCosasViewModel: TablaCosasViewModel by lazy {
        ViewModelProvider(this).get(TablaCosasViewModel::class.java)
    }

    companion object {
        fun nuevaInstancia():TablaCosasFragment {
            return TablaCosasFragment()
        }
    }

    interface InterfazTablaCosas {
        fun onCosaSeleccionada(cosa: Cosa, viewModel: TablaCosasViewModel)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbackInterfaz = context as InterfazTablaCosas
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Al crear la tabla de cosas, indica que existe un menu que renderizar
        setHasOptionsMenu(true)
    }

    //Reemplazamos la función con la que creamos el menú de opciones
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        //Inflamos el menu_tabla_cosas, desde menu
        inflater.inflate(R.menu.menu_tabla_cosas, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.lista_cosas_fragment, container, false)
        inventarioRecyclerView = vista.findViewById(R.id.cosa_recycler_view)
        inventarioRecyclerView.layoutManager = LinearLayoutManager(context)
        totalCosasTextView = vista.findViewById(R.id.totalCosasTextView)
        sumaTotalPreciosTextView = vista.findViewById(R.id.sumaTotalPreciosTextView)
        actualizarUI()
        actualizarPieTabla()
        return vista
    }

    override fun onDetach() {
        super.onDetach()
        callbackInterfaz = null
    }

    //Reemplazamos la función que se activa al seleccionar un item del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Cuando el ID del item...
        return when (item.itemId) {
            //Sea igual al de nueva cosa
            R.id.itemNuevaCosa -> {
                //Crea una nueva cosa, abre el editor para esa cosa y agrega la nueva cosa a la tabla
                val nuevaCosa = Cosa()
                tablaCosasViewModel.agregarCosa(nuevaCosa)
                callbackInterfaz?.onCosaSeleccionada(nuevaCosa, tablaCosasViewModel)
                true
            }
            //Si no, devuelve el item que se seleccionó
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        //Declaramos un apuntador a la barra de actividad
        val barraActividad = activity as AppCompatActivity
        //Cambiamos el título
        barraActividad.supportActionBar?.setTitle(R.string.app_name)
    }

    override fun onResume() {
        super.onResume()
        actualizarPieTabla()
    }

    private fun actualizarUI() {
        val inventario = tablaCosasViewModel.inventario
        inventarioAdaptador = InventarioAdaptador(inventario)
        inventarioRecyclerView.adapter = inventarioAdaptador
    }

    private fun actualizarPieTabla() {
        totalCosasTextView.text = "${tablaCosasViewModel.calcularTotalCosas()}"
        sumaTotalPreciosTextView.text = "$${tablaCosasViewModel.calcularSumaPreciosInventario()}"
    }


    private inner class CosaHolder(vista: View): RecyclerView.ViewHolder(vista), View.OnClickListener {
        //Creamos referencias a los elementos de cosa_layout
        private val thumbnailImageView: ImageView = vista.findViewById(R.id.thumbnailImageView)
        private val nombreTextView: TextView = vista.findViewById(R.id.nombreTextView)
        private val precioTextView: TextView = vista.findViewById(R.id.precioTextView)
        private val numeroSerieTextView: TextView = vista.findViewById(R.id.numeroSerieTextView)

        //Referencia a cosa_layout
        val cosaLayout: ConstraintLayout = vista.findViewById(R.id.cosa_layout)

        var colorItem = ""

        private lateinit var cosa: Cosa

        fun binding(cosa:Cosa) {
            this.cosa = cosa
            nombreTextView.text = cosa.nombre
            precioTextView.text = "$" + cosa.precio.toString()
            numeroSerieTextView.text = cosa.numeroSerie

            val archivoFoto = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${cosa.id}.jpg")

            if(archivoFoto.exists()) {
                thumbnailImageView.setImageBitmap(BitmapFactory.decodeFile(archivoFoto.absolutePath))
            }
            else {
                thumbnailImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_sin_imagen))
            }

            //Decide el color del item en base el precio
            colorItem = when(cosa.precio) {
                in 0..99 -> "#f72585"
                in 100..199 -> "#b5179e"
                in 200..299 -> "#7209b7"
                in 300..399 -> "#560bad"
                in 400..499 -> "#480ca8"
                in 500..599 -> "#3a0ca3"
                in 600..699 -> "#3f37c9"
                in 700..799 -> "#4361ee"
                in 800..899 -> "#4895ef"
                in 900..999 -> "#4cc9f0"
                else -> "#3a0ca3"
            }
            //Envía el color a cosa_layout
            cosaLayout.setBackgroundColor(Color.parseColor(colorItem))
        }

        init {
            vista.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            callbackInterfaz?.onCosaSeleccionada(cosa, tablaCosasViewModel)
        }
    }

    private inner class CosaAdaptador(var inventario: ArrayList<Cosa>): RecyclerView.Adapter<CosaHolder>() {
        override fun onBindViewHolder(holder: CosaHolder, posicion: Int) {
            val cosa = inventario[posicion]
            holder.binding(cosa)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CosaHolder {
            val holder = layoutInflater.inflate(R.layout.cosa_layout, parent, false)
            return CosaHolder(holder)
        }

        override fun getItemCount(): Int {
            return inventario.size
        }

        //Función que elimína items del inventario
        fun remueveCosa(posicion: Int) {
            var rutaFotos: File?
            var archivoFoto: File
            val constructor = AlertDialog.Builder(activity)
            constructor.setTitle("Advertencia")
            constructor.setMessage("¿Desea eliminar esta cosa?")
            //Si el usuario hace dismiss (dando tap fuera del dialogo por ejemplo)
            constructor.setOnDismissListener {
                //Actualiza la vista
                inventarioAdaptador?.notifyDataSetChanged()
                notifyDataSetChanged()
            }
            //Si hace tap en Aceptar
            constructor.setPositiveButton("Aceptar") { _ , _ ->
                //Remueve del inventario el item con la posición recibida
                try {
                    //Comienza por quitar la imagen guardada
                    rutaFotos = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    archivoFoto = File(rutaFotos, "${inventario[posicion].id}.jpg")
                    if(archivoFoto.exists())
                        archivoFoto.delete()
                    //Después le pide al ViewModel que quite la cosa en la posición indicada
                    tablaCosasViewModel.removerCosa(inventario[posicion])
                    inventarioAdaptador?.notifyDataSetChanged()
                    notifyDataSetChanged()
                    actualizarPieTabla()
                }
                catch (e: Exception) {
                    e.printStackTrace()
                    inventarioAdaptador?.notifyDataSetChanged()
                    notifyDataSetChanged()
                }
            }
            //Si hace tap en Cancelar
            constructor.setNegativeButton("Cancelar") { _ , _ ->
                inventarioAdaptador?.notifyDataSetChanged()
                notifyDataSetChanged()
            }
            //Creamos un AlertDialog con el constructor y lo mostramos al usuario
            val alerta: AlertDialog = constructor.create()
            alerta.show()
        }
    }

    private inner class InventarioAdaptador(var inventario: ArrayList<Seccion>): RecyclerView.Adapter<InventarioAdaptador.InventarioHolder>() {

        inner class InventarioHolder(vista: View): RecyclerView.ViewHolder(vista) {
            private val cosasRecyclerView: RecyclerView = vista.findViewById(R.id.cosasRecyclerView)
            private val seccionTextView: TextView = vista.findViewById(R.id.seccionTextView)
            private val numeroCosasTextView: TextView = vista.findViewById(R.id.numeroCosasTextView)
            private val sumaPreciosTextView: TextView = vista.findViewById(R.id.sumaPreciosTextView)
            private val botonOrdenAlfabetico: ImageButton = vista.findViewById(R.id.botonOrdenAlfabetico)
            private val botonOrdenCronologico: ImageButton = vista.findViewById(R.id.botonOrdenCronologico)
            private val botonOrdenPrecio: ImageButton = vista.findViewById(R.id.botonOrdenPrecio)
            private val ordenAlfabeticoTextView: TextView = vista.findViewById(R.id.ordenAlfabeticoTextView)
            private val ordenCronologicoTextView: TextView = vista.findViewById(R.id.ordenCronologicoTextView)
            private val ordenPrecioTextView: TextView = vista.findViewById(R.id.ordenPrecioTextView)

            fun binding(seccion: Seccion) {
                val cosaAdaptador = CosaAdaptador(seccion.lista)
                seccionTextView.text = seccion.nombre
                numeroCosasTextView.text = "${seccion.lista.size}"
                sumaPreciosTextView.text = "$${tablaCosasViewModel.calcularSumaPreciosSeccion(seccion.lista)}"
                cosasRecyclerView.layoutManager = LinearLayoutManager(context)
                cosasRecyclerView.adapter = cosaAdaptador

                //Habilita o deshabilita los botones de ordenamiento, dependiendo si hay al menos un elemento en la sección
                if(seccion.lista.size > 1) {
                    botonOrdenAlfabetico.isEnabled = true
                    botonOrdenCronologico.isEnabled = true
                    botonOrdenPrecio.isEnabled = true
                }
                else {
                    botonOrdenAlfabetico.isEnabled = false
                    botonOrdenCronologico.isEnabled = false
                    botonOrdenPrecio.isEnabled = false
                }

                botonOrdenAlfabetico.apply {
                    setOnClickListener {
                        if(ordenAlfabeticoTextView.text == "▲") {
                            tablaCosasViewModel.ordenarAlfabeticamente(seccion.lista[0].precio, true)
                            ordenAlfabeticoTextView.text = "▼"
                            notifyDataSetChanged()
                        }
                        else {
                            tablaCosasViewModel.ordenarAlfabeticamente(seccion.lista[0].precio, false)
                            ordenAlfabeticoTextView.text = "▲"
                            notifyDataSetChanged()
                        }
                    }
                }

                botonOrdenCronologico.apply {
                    setOnClickListener {
                        if(ordenCronologicoTextView.text == "▲") {
                            tablaCosasViewModel.ordenarCronologicamente(seccion.lista[0].precio, true)
                            ordenCronologicoTextView.text = "▼"
                            notifyDataSetChanged()
                        }
                        else {
                            tablaCosasViewModel.ordenarCronologicamente(seccion.lista[0].precio, false)
                            ordenCronologicoTextView.text = "▲"
                            notifyDataSetChanged()
                        }
                    }
                }

                botonOrdenPrecio.apply {
                    setOnClickListener {
                        if(ordenPrecioTextView.text == "▲") {
                            tablaCosasViewModel.ordenarPrecio(seccion.lista[0].precio, true)
                            ordenPrecioTextView.text = "▼"
                            notifyDataSetChanged()
                        }
                        else {
                            tablaCosasViewModel.ordenarPrecio(seccion.lista[0].precio, false)
                            ordenPrecioTextView.text = "▲"
                            notifyDataSetChanged()
                        }
                    }
                }

                //Hacemos un callback a ItemTouchHelper, especificando las direcciones que vamos a utilizar
                val callbackGestos = object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
                    //Al arrastrar y soltar
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        try {
                            //Obtenemos las posiciones del elemento
                            val posicionInicial = viewHolder.adapterPosition
                            val posicionFinal = target.adapterPosition
                            if (posicionInicial != posicionFinal) {
                                //Intercambiamos la posición de la cosa en el inventario
                                Collections.swap(seccion.lista, posicionInicial, posicionFinal)
                                cosaAdaptador.notifyItemMoved(posicionInicial, posicionFinal)
                            }
                        }
                        catch (e: Exception) {
                            e.printStackTrace()
                            inventarioAdaptador?.notifyDataSetChanged()
                            notifyDataSetChanged()
                        }
                        return false
                    }
                    //Al deslizar...
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direccion: Int) {
                        //...hacia la izquierda
                        if(direccion == ItemTouchHelper.LEFT) {
                            cosaAdaptador.remueveCosa(viewHolder.adapterPosition)
                        }
                    }
                }
                //Adjunta el ItemTouchHelper a cosaRecyclerView
                val itemTouchHelper = ItemTouchHelper(callbackGestos)
                itemTouchHelper.attachToRecyclerView(cosasRecyclerView)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventarioHolder {
            val inventarioHolder = LayoutInflater.from(parent.context).inflate(R.layout.seccion_layout, parent, false)
            return InventarioHolder(inventarioHolder)
        }

        override fun onBindViewHolder(inventarioHolder: InventarioHolder, posicion: Int) {
            inventarioHolder.binding(inventario[posicion])
            inventarioHolder.itemView.setBackgroundColor(Color.parseColor(obtenerColorSeccion(posicion)))
        }

        fun obtenerColorSeccion(posicion: Int): String {
            val colorSeccion = when(posicion) {
                0 -> "#f72585"
                1 -> "#b5179e"
                2 -> "#7209b7"
                3 -> "#560bad"
                4 -> "#480ca8"
                5 -> "#3a0ca3"
                6 -> "#3f37c9"
                7 -> "#4361ee"
                8 -> "#4895ef"
                9 -> "#4cc9f0"
                else -> "#3a0ca3"
            }
            return colorSeccion
        }

        override fun getItemCount(): Int {
            return inventario.size
        }
    }
}