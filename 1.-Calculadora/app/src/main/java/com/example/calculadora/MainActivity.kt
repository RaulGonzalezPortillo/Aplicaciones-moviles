package com.example.calculadora

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import java.text.DecimalFormat

private const val TAG = "CalculadoraViewModel"

class MainActivity : AppCompatActivity() {

    //Creamos un patrón para remover 0s innecesarios después del punto al imprimir
    val dec = DecimalFormat("0.########")
    //Creamos variables para las diferentes displays de la calculadora
    private lateinit var display : TextView
    private lateinit var operacionActualDisplay : TextView
    private lateinit var memoryDisplay: TextView
    //Creamos nuestro intermediario entre la vista y el modelo
    private val calculadoraViewModel: CalculadoraViewModel by lazy {
        ViewModelProvider(this).get(CalculadoraViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "En onCreate")
        //Inicializamos displays
        display = findViewById(R.id.display)
        operacionActualDisplay = findViewById(R.id.operacionActualDisplay)
        memoryDisplay = findViewById(R.id.memoryDisplay)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "En onDestroy")
        //Guarda la información actual en el intermediario
        calculadoraViewModel.resultado = display.text.toString().toDouble()
        calculadoraViewModel.operacionActualBackup = operacionActualDisplay.text.toString()
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "En onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "En onResume")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "En onStart")
        //Damos formato a la información en memoria y la enviamos a los displays
        display.text = dec.format(calculadoraViewModel.resultado)
        operacionActualDisplay.text = calculadoraViewModel.operacionActualBackup
        memoryDisplay.text = dec.format(calculadoraViewModel.memory)
    }

    //Actualiza las variables guardadas en el intermediario
    private fun actualizaInformacion() {
        calculadoraViewModel.memory = calculadoraViewModel.getMemory()
        calculadoraViewModel.operando = calculadoraViewModel.getOperando()
        calculadoraViewModel.operandoEspera = calculadoraViewModel.getOperandoEspera()
        calculadoraViewModel.operacionEspera = calculadoraViewModel.getOperacionEspera()
    }

    //Pide al intermediario que borre toda su información y la del modelo
    private fun clearEverything() {
        calculadoraViewModel.escribiendoNumero = false
        calculadoraViewModel.operacionActualTiempoReal = false
        calculadoraViewModel.setMemory(0.0)
        calculadoraViewModel.setOperacionEspera("")
        calculadoraViewModel.setOperando(0.0)
        calculadoraViewModel.setOperandoEspera(0.0)
        calculadoraViewModel.resultado = 0.0
    }

    //Controla el comportamiento de los botones CE y ⌫
    fun presionaClear(boton: View) {
        when((boton as Button).text.toString()) {
            //Si se presiona CE
            "ce" -> {
                //Vacía todas las variables en modelo
                clearEverything()
                //Refresca el intermediario
                actualizaInformacion()
                //Actualiza las displays
                display.text = "0"
                operacionActualDisplay.text = "0"
                memoryDisplay.text = "0"
            }
            "⌫" -> {
                //Si hay más de un valor en Display
                if(display.text.toString().length > 1) {
                    //Quita el último valor e indica que se está escribiendo un número
                    display.text = display.text.toString().dropLast(1)
                    calculadoraViewModel.escribiendoNumero = true
                    if(calculadoraViewModel.operacionActualTiempoReal) {
                        operacionActualDisplay.text = operacionActualDisplay.text.toString().dropLast(1)
                    }
                }
                //Si solo hay un valor en Display
                else {
                    //Vacía display e indica que no se están escribiendo un número
                    display.text = "0"
                    if(calculadoraViewModel.operacionEspera != "" && calculadoraViewModel.escribiendoNumero) {
                        operacionActualDisplay.text = operacionActualDisplay.text.toString().dropLast(1)
                    }
                    calculadoraViewModel.escribiendoNumero = false
                }
            }
        }
    }

    //Controla el comportamiento de los botones de entrada (números, '.' y '𝛑')
    @SuppressLint("SetTextI18n")
    fun presionaDigito(boton: View) {
        //Asigna el contenido del display a digito
        val digitoPresionado = (boton as Button).text
        //Si recibió 𝛑
        if (digitoPresionado.contains("𝛑"))
        {
            //Reemplaza el contenido de display e indica que se está escribiendo número
            display.text = "3.14159265"
            calculadoraViewModel.escribiendoNumero = true
        }
        else
        {
            //Si el usuario ya estaba escribiendo un número
            if (calculadoraViewModel.escribiendoNumero) {
                //Y si no se está intentando ingresar un segúndo punto decimal
                if (!(display.text.toString().contains('.') && digitoPresionado.contains('.'))) {
                    //Anexa el digito
                    display.append(digitoPresionado)
                }
            } 
            //Si no se estaba escribiendo un número
            else {
                //Reemplaza Display con el digito e indica que está escribiendose un número
                display.text = digitoPresionado
                calculadoraViewModel.escribiendoNumero = true
            }
        }
        if(calculadoraViewModel.operacionActualTiempoReal) {
            operacionActualDisplay.append(digitoPresionado)
        }
    }

    //Controla el comportamiento de los botones de función ('+', '/', etc...)
    fun presionaFuncion(boton: View) {
        val operacionPresionada = (boton as Button).text.toString()
        if (calculadoraViewModel.escribiendoNumero) {
            calculadoraViewModel.setOperando(display.text.toString().toDouble())
            calculadoraViewModel.escribiendoNumero = false
        }
        calculadoraViewModel.ejecutaOperacion(operacionPresionada)
        //Si se recibió una operación de un solo operando
        if(
            operacionPresionada == "+/-" ||
            operacionPresionada == "1/x" ||
            operacionPresionada == "x²" ||
            operacionPresionada == "²√" ||
            operacionPresionada == "store" ||
            operacionPresionada == "recall" ||
            operacionPresionada == "mem+" ||
            operacionPresionada == "mem-" ||
            operacionPresionada == "memc"
        ) {
            //Presiona '=' inmediatamente
            calculadoraViewModel.ejecutaOperacion("=")
        }
        calculadoraViewModel.operacionEspera = calculadoraViewModel.getOperacionEspera()
        //Si hay un operador válido en memoria
        if(calculadoraViewModel.operacionEspera != "" && calculadoraViewModel.operacionEspera != "=") {
            //Actualiza el operando en espera
            calculadoraViewModel.operandoEspera = calculadoraViewModel.getOperandoEspera()
            //Le damos formato y lo enviamos a displayOperacionActual
            operacionActualDisplay.text = dec.format(calculadoraViewModel.operandoEspera)
            //Agregamos el operador presionado
            operacionActualDisplay.append(" $operacionPresionada ")
            //Indicamos a la calculadora que actualice en tiempo real los siguientes dígitos
            calculadoraViewModel.operacionActualTiempoReal = true
        }
        //Si recibimos un operador inválido
        else {
            //Desactivamos el modo de actualización de operación actual en tiempo real
            calculadoraViewModel.operacionActualTiempoReal = false
        }
        display.text = dec.format(calculadoraViewModel.resultado)
        calculadoraViewModel.memory = calculadoraViewModel.getMemory()
        memoryDisplay.text = dec.format(calculadoraViewModel.memory)
    }
}