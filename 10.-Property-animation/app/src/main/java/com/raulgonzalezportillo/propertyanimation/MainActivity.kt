@file:Suppress("UNUSED_PARAMETER", "SetTextI18n", "ObjectAnimatorBinding")

package com.raulgonzalezportillo.propertyanimation

import android.animation.*
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import java.util.*
import javax.crypto.spec.OAEPParameterSpec

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
        val escalaX = PropertyValuesHolder.ofFloat(View.SCALE_X, 2F)
        val escalaY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 2F)
        val animador = ObjectAnimator.ofPropertyValuesHolder(mrSparkle, escalaX, escalaY)
        animador.repeatCount = 1
        animador.repeatMode = ObjectAnimator.REVERSE
        animador.deshabilitarBoton(botonEscalar)
        animador.start()
    }

    fun colores(vista: View) {
        val colorInicial = Color.rgb(30, 122, 199)
        val colorFinal = Color.rgb(236, 129, 0)
        val animador = ObjectAnimator.ofArgb(mrSparkle.parent, "backgroundColor", colorInicial, colorFinal)
        animador.duration = 2000
        animador.deshabilitarBoton(botonColores)
        animador.start()
    }

    fun transparencia(vista: View) {
        val animador = ObjectAnimator.ofFloat(mrSparkle, View.ALPHA, 0F)
        animador.repeatCount = 1
        animador.repeatMode = ObjectAnimator.REVERSE
        animador.deshabilitarBoton(botonTransparencia)
        animador.start()
    }

    fun multi(vista: View) {
        val contenedor = mrSparkle.parent as ViewGroup
        val anchoPantalla = contenedor.width
        val altoPantalla = contenedor.height
        val anchoMrSparkle = mrSparkle.width.toFloat()
        val altoMrSparkle = mrSparkle.height.toFloat()
        val nuevoMrSparkle = AppCompatImageView(this)
        nuevoMrSparkle.setImageResource(R.drawable.mr_sparkle)
        nuevoMrSparkle.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        contenedor.addView(nuevoMrSparkle)
        nuevoMrSparkle.scaleX = 0.5F
        nuevoMrSparkle.scaleY = 0.5F
        val x = Random().nextFloat() * anchoPantalla - anchoMrSparkle / 2
        val y = Random().nextFloat() * botonRotar.top - altoMrSparkle / 2
        nuevoMrSparkle.translationX = x
        nuevoMrSparkle.translationY = y
        val rotacion = ObjectAnimator.ofFloat(nuevoMrSparkle, View.ROTATION, Random().nextFloat() * 1080)
        rotacion.interpolator = LinearInterpolator()
        val escalaX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0F)
        val escalaY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0F)
        val escala = ObjectAnimator.ofPropertyValuesHolder(nuevoMrSparkle, escalaX, escalaY)
        val conjuntoAnimacion = AnimatorSet()
        conjuntoAnimacion.playTogether(rotacion, escala)
        conjuntoAnimacion.duration = (Random().nextFloat() * 1500 + 1000).toLong()
        conjuntoAnimacion.start()
        conjuntoAnimacion.addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                contenedor.removeView(nuevoMrSparkle)
            }
        })
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