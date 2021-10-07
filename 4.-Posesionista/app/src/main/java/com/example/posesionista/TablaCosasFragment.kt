package com.example.posesionista

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "TablaCosasFragment"

class TablaCosasFragment: Fragment() {
    private lateinit var cosaRecyclerView: RecyclerView
    private var adaptador: CosaAdapter? = null
    private var callbackInterfaz: InterfazTablaCosas? = null

    interface InterfazTablaCosas {
        fun onCosaSeleccionada(cosa: Cosa)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbackInterfaz = context as InterfazTablaCosas
    }

    override fun onDetach() {
        super.onDetach()
        callbackInterfaz = null
    }

    private fun actualizarUI() {
        val inventario = tablaCosasViewModel.inventario
        adaptador = CosaAdapter(inventario)
        cosaRecyclerView.adapter = adaptador
    }

    private val tablaCosasViewModel: TablaCosasViewModel by lazy {
        ViewModelProvider(this).get(TablaCosasViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total de cosas: ${tablaCosasViewModel.inventario.size}")
    }

    companion object {
        fun nuevaInstancia():TablaCosasFragment {
            return TablaCosasFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.lista_cosas_fragment, container, false)
        cosaRecyclerView = vista.findViewById(R.id.cosa_recycler_view) as RecyclerView
        cosaRecyclerView.layoutManager = LinearLayoutManager(context)
        actualizarUI()
        return vista
    }

    private inner class CosaHolder(vista: View): RecyclerView.ViewHolder(vista), View.OnClickListener {
        private val nombreTextView: TextView = itemView.findViewById(R.id.label_nombre)
        private val precioTextView: TextView = itemView.findViewById(R.id.label_precio)
        private lateinit var cosa: Cosa
        @SuppressLint("SetTextI18n")
        fun binding(cosa:Cosa) {
            this.cosa = cosa
            nombreTextView.text = cosa.nombre
            precioTextView.text = "$" + cosa.precio.toString()
        }
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            callbackInterfaz?.onCosaSeleccionada(cosa)
        }
    }

    private inner class CosaAdapter(var inventario: List<Cosa>): RecyclerView.Adapter<CosaHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CosaHolder {
            val holder = layoutInflater.inflate(R.layout.cosa_layout, parent, false)
            return CosaHolder(holder)
        }

        override fun getItemCount(): Int {
            return inventario.size
        }

        override fun onBindViewHolder(holder: CosaHolder, position: Int) {
            val cosa = inventario[position]
            holder.binding(cosa)
        }
    }
}