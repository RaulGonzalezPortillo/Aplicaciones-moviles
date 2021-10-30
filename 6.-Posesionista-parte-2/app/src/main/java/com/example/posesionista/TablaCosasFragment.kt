@file:Suppress("DEPRECATION", "UNUSED")

package com.example.posesionista

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.app.AlertDialog
import kotlin.collections.*
import java.util.*

private const val TAG = "TablaCosasFragment"

class TablaCosasFragment: Fragment() {
    private lateinit var cosaRecyclerView: RecyclerView
    private var adaptador: CosaAdapter? = null
    private var callbackInterfaz: InterfazTablaCosas? = null

    private val tablaCosasViewModel: TablaCosasViewModel by lazy {
        ViewModelProvider(this).get(TablaCosasViewModel::class.java)
    }

    companion object {
        fun nuevaInstancia():TablaCosasFragment {
            return TablaCosasFragment()
        }
    }

    interface InterfazTablaCosas {
        fun onCosaSeleccionada(cosa: Cosa)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbackInterfaz = context as InterfazTablaCosas
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Al crear la tabla de cosas, indica que existe un menu que renderizar
        setHasOptionsMenu(true)
        Log.d(TAG, "Total de cosas: ${tablaCosasViewModel.inventario.size}")
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
        cosaRecyclerView = vista.findViewById(R.id.cosa_recycler_view) as RecyclerView
        cosaRecyclerView.layoutManager = LinearLayoutManager(context)
        actualizarUIYGestos()
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
                tablaCosasViewModel.agregaCosa(nuevaCosa)
                callbackInterfaz?.onCosaSeleccionada(nuevaCosa)
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

    private fun actualizarUIYGestos() {
        val inventario = tablaCosasViewModel.inventario
        adaptador = CosaAdapter(inventario)
        cosaRecyclerView.adapter = adaptador

        //Hacemos un callback a ItemTouchHelper, especificando las direcciones que vamos a utilizar
        val callbackGestos = object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            //Al arrastrar y soltar
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                //Obtenemos las posiciones del elemento
                val posicionInicial = viewHolder.adapterPosition
                val posicionFinal = target.adapterPosition
                //Intercambiamos la posición de la cosa en el inventario
                Collections.swap(inventario, posicionInicial, posicionFinal)
                adaptador?.notifyItemMoved(posicionInicial, posicionFinal)
                return false
            }
            //Al deslizar...
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //...hacia la izquierda
                if(direction == ItemTouchHelper.LEFT) {
                    adaptador?.remueveCosa(viewHolder.adapterPosition)
                }
            }
        }
        //Adjunta el ItemTouchHelper a cosaRecyclerView
        val itemTouchHelper = ItemTouchHelper(callbackGestos)
        itemTouchHelper.attachToRecyclerView(cosaRecyclerView)
    }

    private inner class CosaHolder(vista: View): RecyclerView.ViewHolder(vista), View.OnClickListener {
        //Creamos referencias a los TextView de cosa_layout
        private val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        private val precioTextView: TextView = itemView.findViewById(R.id.precioTextView)
        private val numeroSerieTextView: TextView = itemView.findViewById(R.id.numeroSerieTextView)

        //Referencia a cosa_layout
        val cosaLayout: ConstraintLayout = itemView.findViewById(R.id.cosa_layout)

        private lateinit var cosa: Cosa

        var colorItem = ""

        @SuppressLint("SetTextI18n")
        fun binding(cosa:Cosa) {
            this.cosa = cosa
            nombreTextView.text = cosa.nombre
            precioTextView.text = "$" + cosa.precio.toString()
            numeroSerieTextView.text = cosa.numeroSerie

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
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            callbackInterfaz?.onCosaSeleccionada(cosa)
        }
    }

    private inner class CosaAdapter(var inventario: List<Cosa>): RecyclerView.Adapter<CosaHolder>() {
        override fun onBindViewHolder(holder: CosaHolder, position: Int) {
            val cosa = inventario[position]
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
        @SuppressLint("NotifyDataSetChanged")
        fun remueveCosa(posicion: Int) {
            val constructor = AlertDialog.Builder(activity)
            constructor.setTitle("Advertencia")
            constructor.setMessage("¿Desea eliminar esta cosa?")
            //Si el usuario hace dismiss (dando tap fuera del dialogo por ejemplo)
            constructor.setOnDismissListener {
                //Actualiza la vista
                notifyDataSetChanged()
            }
            //Si hace tap en Aceptar
            constructor.setPositiveButton("Aceptar") { _ , _ ->
                //Remueve del inventario el item con la posición recibida
                inventario = inventario.filterIndexed { indice, _ ->
                    indice != posicion
                }
                notifyDataSetChanged()
            }
            //Si hace tap en Cancelar
            constructor.setNegativeButton("Cancelar") { _ , _ ->
                notifyDataSetChanged()
            }
            //Creamos un AlertDialog con el constructor y lo mostramos al usuario
            val alerta: AlertDialog = constructor.create()
            alerta.show()
        }
    }
}