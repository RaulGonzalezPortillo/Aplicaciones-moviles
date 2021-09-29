package com.example.posesionista

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

private const val TAG = "TablaCosasFragment"

class TablaCosasFragment: Fragment() {
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
}