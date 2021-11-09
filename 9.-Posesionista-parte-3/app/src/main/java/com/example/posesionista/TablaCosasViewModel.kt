package com.example.posesionista

import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.collections.ArrayList

//Declaramos el tipo 'Seccion', que contiene el nombre de la sección y un arreglo con los elementos dentro de la misma
data class Seccion(val nombre: String, val lista: ArrayList<Cosa>)

class TablaCosasViewModel: ViewModel() {
    //Declaramos una lista con nombres y adjetivos
    private val nombres = arrayOf("Teléfono", "Pan", "Sweater")
    private val adjetivos = arrayOf("gris", "suave", "cómodo")

    private val rangos = arrayListOf(
        0..99,
        100..199,
        200..299,
        300..399,
        400..499,
        500..599,
        600..699,
        700..799,
        800..899,
        900..999,
        10000..1000000
    )

    val inventario = ArrayList<Seccion>()

    //Llenamos la lista de secciones con sus respectivos nombres
    init {
        inventario.add(Seccion("$0 a $99", arrayListOf()))
        for (i in 1 until 9) {
            inventario.add(Seccion("$${i}00 a $${i}99", arrayListOf()))
        }
        inventario.add(Seccion("Más de $1000", arrayListOf()))

        //Añadimos 10 cosas aleatorias
        for (i in 0 until 10) {
            val cosa = Cosa()
            //Generamos un nombre aleatorio
            val nombreAleatorio = nombres.random()
            val adjetivoAleatorio = adjetivos.random()
            val precioAleatorio = Random().nextInt(1100)
            //Asignamos los valores a nuestra cosa
            cosa.nombre = "$nombreAleatorio $adjetivoAleatorio"
            cosa.precio = precioAleatorio
            //La añadimos al inventario
            agregaCosa(cosa)
        }
    }

    fun determinarSeccion(precio: Int): Int {
        val indice = when(precio) {
            in 0..99 -> 0
            in 100..199 -> 1
            in 200..299 -> 2
            in 300..399 -> 3
            in 400..499 -> 4
            in 500..599 -> 5
            in 600..699 -> 6
            in 700..799 -> 7
            in 800..899 -> 8
            in 900..999 -> 9
            else -> 10
        }
        return indice
    }

    //Función que agrega una cosa al inventario
    fun agregaCosa(cosa: Cosa) {
        val indice = determinarSeccion(cosa.precio)
        inventario[indice].lista.add(cosa)
    }
}
