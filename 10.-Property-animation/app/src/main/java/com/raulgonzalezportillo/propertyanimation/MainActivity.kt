@file:Suppress("UNUSED_PARAMETER", "SetTextI18n")

package com.raulgonzalezportillo.propertyanimation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var mrSparkle: View
    private lateinit var botonRotar: Button
    private lateinit var botonMover: Button
    private lateinit var botonEscalar: Button
    private lateinit var botonColores: Button
    private lateinit var botonTransparencia: Button
    private lateinit var botonMulti: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mrSparkle = findViewById(R.id.imagenMrSparkle)
        botonRotar = findViewById(R.id.botonRotar)
        botonMover = findViewById(R.id.botonMover)
        botonEscalar = findViewById(R.id.botonEscalar)
        botonColores = findViewById(R.id.botonColores)
        botonTransparencia = findViewById(R.id.botonTransparencia)
        botonMulti = findViewById(R.id.botonMulti)
    }

    fun rotar(vista: View) {
        val animador = ObjectAnimator.ofFloat(mrSparkle, View.ROTATION, 0F, 360F)
        animador.duration = 1000
        animador.repeatCount = 3
        animador.deshabilitarBoton(botonRotar)
        animador.start()
    }

    fun mover(vista: View) {
        val animador = ObjectAnimator.ofFloat(mrSparkle, View.TRANSLATION_X, 200F)
        animador.repeatCount = 1
        animador.repeatMode = ObjectAnimator.REVERSE
        animador.deshabilitarBoton(botonMover)
        animador.start()
    }

    fun escalar(vista: View) {

    }

    fun colores(vista: View) {

    }

    fun transparencia(vista: View) {

    }

    fun multi(vista: View) {

    }

    private fun ObjectAnimator.deshabilitarBoton(boton: Button) {
        addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationStart(p0: Animator?) {
                boton.isEnabled = false
            }
            override fun onAnimationEnd(p0: Animator?) {
                boton.isEnabled = true
            }
        })
    }
}