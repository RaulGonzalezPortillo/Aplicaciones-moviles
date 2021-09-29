package com.example.posesionista

import java.util.*

data class Cosa (var nombre: String = "",
                 var precio: Int = 0,
                 val numeroSerie: UUID = UUID.randomUUID(),
                 val fecha: Date = Date()) {

}