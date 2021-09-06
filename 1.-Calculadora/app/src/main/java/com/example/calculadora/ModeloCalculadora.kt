package com.example.calculadora

import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class ModeloCalculadora {
    private var memory = 0.0
    private var operacionEspera = ""
    private var operando = 0.0
    private var operandoEspera = 0.0

    fun setMemory(operandoRecibido: Double) {
        memory = operandoRecibido
    }

    fun getMemory(): Double {
        return memory
    }

    fun setOperacionEspera(operacionRecibida: String) {
        operacionEspera = operacionRecibida
    }

    fun getOperacionEspera(): String {
        return operacionEspera
    }

    fun setOperando(operandoRecibido: Double) {
        operando = operandoRecibido
    }

    fun getOperando(): Double {
        return operando
    }

    fun setOperandoEspera(operandoRecibido: Double) {
        operandoEspera = operandoRecibido
    }

    fun getOperandoEspera(): Double {
        return operandoEspera
    }

    //Ejecuta las operaciones y devuelve el resultado
    fun ejecutaOperacion(operacionRecibida: String): Double {
        ejecutaOperacionEspera()
        operacionEspera = operacionRecibida
        operandoEspera = operando
        return operando
    }

    //Contiene la lógica para resolver las operaciones
    private fun ejecutaOperacionEspera() {
        when(operacionEspera) {
            "+" -> operando += operandoEspera
            "-" -> operando = operandoEspera - operando
            "x" -> operando *= operandoEspera
            "/" -> {
                if (operando != 0.0) {
                    operando = operandoEspera / operando
                }
            }
            "ⁿ√" -> operando = operandoEspera.pow(1.0 / operando)
            "xⁿ" -> operando = operandoEspera.pow(operando)
            "sin(x)" -> operando = sin(operando)
            "cos(x)" -> operando = cos(operando)
            "+/-" -> operando *= (-1)
            "1/x" -> operando = 1/operando
            "x²" -> operando = operando.pow(2)
            "²√" -> operando = sqrt(operando)
            "store" -> memory = operando
            "recall" -> operando = memory
            "mem+" -> memory += operando
            "mem-" -> memory -= operando
            "memc" -> memory = 0.0
        }
    }
}