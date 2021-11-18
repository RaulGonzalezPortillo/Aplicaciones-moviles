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
        for (i in 1 until 10) {
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
            agregarCosa(cosa)
        }
    }

    //Función que agrega una cosa al inventario
    fun agregarCosa(cosa: Cosa) {
        val indice = determinarSeccion(cosa.precio)
        inventario[indice].lista.add(cosa)
    }

    fun calcularSumaPreciosSeccion(lista: ArrayList<Cosa>): Int {
        var sumaPrecios = 0
        for (cosa in lista) {
            sumaPrecios += cosa.precio
        }
        return sumaPrecios
    }

    fun calcularSumaPreciosInventario(): Int {
        var sumaPrecios = 0
        for (i in 0 until 11) {
            for (cosa in inventario[i].lista) {
                sumaPrecios += cosa.precio
            }
        }
        return sumaPrecios
    }

    fun calcularTotalCosas(): Int {
        var totalCosas = 0
        for (i in 0 until 11) {
            totalCosas += inventario[i].lista.size
        }
        return totalCosas
    }

    //Función que nos dice en que índice del arreglo inventario está una cosa, con base en el precio
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

    //Función que ordena el inventario alfabéticamente
    fun ordenarAlfabeticamente(precio: Int, flagOrden: Boolean) {
        val posicion = determinarSeccion(precio)
        //Si recibe true en la flag de orden
        if (flagOrden) {
            //Ordena de forma alfabética-ascendente
            inventario[posicion].lista.sortBy { it.nombre }
        }
        //Si no
        else {
            //Ordena de forma alfabética-descendente
            inventario[posicion].lista.sortByDescending { it.nombre }
        }
    }

    //Función que ordena el inventario cronológicamente
    fun ordenarCronologicamente(precio: Int, flagOrden: Boolean) {
        val posicion = determinarSeccion(precio)
        //Al igual que ordenarAlfabeticamente, flagOrden indica si se ordenará de forma ascendente o descendente
        if (flagOrden) {
            inventario[posicion].lista.sortBy { it.fecha }
        }
        else {
            inventario[posicion].lista.sortByDescending { it.fecha }
        }
    }

    //Función que ordena el inventario por precio
    fun ordenarPrecio(precio: Int, flagOrden: Boolean) {
        val posicion = determinarSeccion(precio)
        if (flagOrden) {
            inventario[posicion].lista.sortBy { it.precio }
        }
        else {
            inventario[posicion].lista.sortByDescending { it.precio }
        }
    }

    fun ordenarSeccion(cosa: Cosa, indiceActual: Int) {
        val nuevoIndice = determinarSeccion(cosa.precio)
        if (nuevoIndice != indiceActual) {
            val seccionReordenada = inventario[indiceActual].lista.filter {
                it.precio in rangos[indiceActual]
            }
            inventario[indiceActual].lista.clear()
            inventario[indiceActual].lista.addAll(seccionReordenada)
            inventario[nuevoIndice].lista.add(cosa)
        }
    }

    //Función que remueve una cosa del inventario
    fun removerCosa(cosa: Cosa) {
        val seccion = determinarSeccion(cosa.precio)
        inventario[seccion].lista.remove(cosa)
    }
}
