package com.example.calculadora

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "CalculadoraViewModel"

class CalculadoraViewModel: ViewModel() {
    private val modeloCalculadora = ModeloCalculadora()
    var escribiendoNumero = false
    var memory = 0.0
    var operacionActualTiempoReal = false
    var operacionActualBackup = "0"
    var operacionEspera = ""
    var operando = 0.0
    var operandoEspera = 0.0
    var resultado: Double = 0.0
    init {
        Log.d(TAG, "Instancia de viewModel")
    }

    //Asigna el valor recibido a memory en modeloCalculadora
    @JvmName("setMemoryViewModel")
    fun setMemory(operandoRecibido: Double) {
        modeloCalculadora.setMemory(operandoRecibido)
    }

    //Devuelve el valor actual de memory en modeloCalculadora
    @JvmName("getMemoryViewModel")
    fun getMemory(): Double {
        return modeloCalculadora.getMemory()
    }

    //Asigna el valor recibido a operacionEspera en modeloCalculadora
    @JvmName("setOperacionEsperaViewModel")
    fun setOperacionEspera(operacionRecibida: String) {
        modeloCalculadora.setOperacionEspera(operacionRecibida)
    }

    //Devuelve el valor actual de operacionEspera en modeloCalculadora
    @JvmName("getOperacionEsperaViewModel")
    fun getOperacionEspera(): String {
        return modeloCalculadora.getOperacionEspera()
    }

    //Asigna el valor recibido a operando en modeloCalculadora
    @JvmName("setOperandoViewModel")
    fun setOperando(operandoRecibido: Double) {
        modeloCalculadora.setOperando(operandoRecibido)
    }

    //Devuelve el valor actual de operando en modeloCalculadora
    @JvmName("getOperandoViewModel")
    fun getOperando(): Double {
        return modeloCalculadora.getOperando()
    }

    //Asigna el valor recibido a operandoEspera en modeloCalculadora
    @JvmName("setOperandoEsperaViewModel")
    fun setOperandoEspera(operandoRecibido: Double) {
        modeloCalculadora.setOperandoEspera(operandoRecibido)
    }

    //Devuelve el valor actual de operando en modeloCalculadora
    @JvmName("getOperandoEsperaViewModel")
    fun getOperandoEspera(): Double {
        return modeloCalculadora.getOperandoEspera()
    }

    //Envía la operación presionada en modeloCalculadora
    fun ejecutaOperacion(operacionPresionada: String) {
        resultado = modeloCalculadora.ejecutaOperacion(operacionPresionada)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "El ViewModel está por destruirse")
    }
}