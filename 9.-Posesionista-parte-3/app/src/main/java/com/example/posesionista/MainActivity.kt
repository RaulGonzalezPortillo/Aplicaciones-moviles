package com.example.posesionista

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//Especificamos que vamos a implementar la interfaz de tabla de cosas
class MainActivity : AppCompatActivity(), TablaCosasFragment.InterfazTablaCosas {
    private var cosaActual = Cosa()
    private var tablaCosasViewModel: TablaCosasViewModel? = null
    private var posicionCosaActual: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Buscamos el fragment_container por medio del Fragment Manager
        val fragmentoActual = supportFragmentManager.findFragmentById(R.id.fragment_container)
        //Si el fragmento no ha sido inicializado, lo añade
        if(fragmentoActual == null) {
            val fragmento = TablaCosasFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragmento)
                .commit()
        }
    }

    //Implementamos onCosaSeleccionada
    override fun onCosaSeleccionada(cosa: Cosa, viewModel: TablaCosasViewModel) {
        cosaActual = cosa
        tablaCosasViewModel = viewModel
        posicionCosaActual = viewModel.determinarSeccion(cosa.precio)
        val fragmento = CosaFragment.nuevaInstancia(cosa)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragmento)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        tablaCosasViewModel?.ordenarSeccion(cosaActual, posicionCosaActual)
        super.onBackPressed()
    }
}