package com.example.posesionista

import androidx.lifecycle.ViewModel
import java.util.*

class TablaCosasViewModel: ViewModel() {
    //Declaramos el inventario
    val inventario = mutableListOf<Cosa>()
    //Y una lista con nombres y adjetivos
    val nombres = arrayOf("Teléfono", "Pan", "Playera")
    val adjetivos = arrayOf("Gris", "Suave", "Cómoda")
    init {
        for(i in 0 until 100) {
            val cosa = Cosa()
            //Generamos un nombre aleatorio
            val nombreAleatorio = nombres.random()
            val adjetivoAleatorio = adjetivos.random()
            val precioAleatorio = Random().nextInt(100)
            //Asignamos los valores a nuestra cosa
            cosa.nombre = "$nombreAleatorio $adjetivoAleatorio"
            cosa.precio = precioAleatorio
            //La añadimos al inventario
            inventario += cosa
        }
    }
}