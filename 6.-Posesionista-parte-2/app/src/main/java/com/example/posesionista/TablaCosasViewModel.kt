package com.example.posesionista

import androidx.lifecycle.ViewModel
import java.util.*

class TablaCosasViewModel: ViewModel() {
    //Declaramos el inventario
    val inventario = mutableListOf<Cosa>()
    //Y una lista con nombres y adjetivos
    private val nombres = arrayOf("Teléfono", "Pan", "Sweater")
    private val adjetivos = arrayOf("gris", "suave", "cómodo")
    init {
        for(i in 0 until 10) {
            val cosa = Cosa()
            //Generamos un nombre aleatorio
            val nombreAleatorio = nombres.random()
            val adjetivoAleatorio = adjetivos.random()
            val precioAleatorio = Random().nextInt(1000)
            //Asignamos los valores a nuestra cosa
            cosa.nombre = "$nombreAleatorio $adjetivoAleatorio"
            cosa.precio = precioAleatorio
            //La añadimos al inventario
            inventario += cosa
        }
    }
    //Función que agrega una cosa al inventario
    fun agregaCosa(cosa: Cosa) {
        inventario.add(cosa)
    }
}