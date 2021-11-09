package com.example.hipnosis

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.SizeF
import android.view.MotionEvent
import android.view.View
import kotlin.math.min
import kotlin.random.Random

private const val TAG = "VistaHipnosis"

class VistaHipnosis(context: Context, attrs: AttributeSet? = null): View(context, attrs) {

    private var coordenadaToque = PointF(0F, 0F)

    private val paintLinea = Paint().apply {
        color = Color.RED
        strokeWidth = 10F
    }

    private val paintCirculo = Paint().apply {
        color = Color.BLACK
        strokeWidth = 20F
        style = Paint.Style.STROKE
    }

    private val paintTexto = Paint().apply {
        color = Color.BLACK
        textSize = 80F
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
    }

    private fun dibujaTache(canvas: Canvas) {
        //Inicio X, Inicio Y, Fin X, Fin Y
        canvas.drawLine(0F, 0F, width.toFloat(), height.toFloat(), paintLinea)
        canvas.drawLine(width.toFloat(), 0F, 0F, height.toFloat(), paintLinea)
    }

    private fun dibujaCirculo(canvas: Canvas) {
        val tamanoVista = SizeF(measuredWidth.toFloat(), measuredHeight.toFloat())
        val radioMinimo = min(tamanoVista.width, tamanoVista.height)
        val radioMaximo = Math.max(tamanoVista.width, tamanoVista.height)
        for (radioActual in radioMaximo.toInt() downTo 0 step 40) {
            canvas.drawCircle(tamanoVista.width / 2, tamanoVista.height / 2, radioActual.toFloat(), paintCirculo)
        }
    }

    private fun dibujaTexto(canvas: Canvas) {
        val tamanoVista = SizeF(measuredWidth.toFloat(), measuredHeight.toFloat())
        val texto = "Tienes mucho sueño"
        val rectanguloTexto = Rect()
        paintTexto.getTextBounds(texto, 0, texto.length, rectanguloTexto)
        canvas.drawText(texto, (tamanoVista.width - rectanguloTexto.width()) / 2F, tamanoVista.height / 2F, paintTexto)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        dibujaCirculo(canvas)
        //dibujaTache(canvas)
        dibujaTexto(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val ubicacionTap = PointF(event.x, event.y)
        var accion = ""
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                accion = "ACTION_DOWN"
                paintLinea.color = Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
                paintCirculo.color = Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
                coordenadaToque = ubicacionTap
                //Pide al sistema que en el siguiente refresco de pantalla vuelva a pintar la vista
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                accion = "ACTION_MOVE"
                paintLinea.color = Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
                paintCirculo.color = Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
                this.x += ubicacionTap.x - coordenadaToque.x
                this.y += ubicacionTap.y - coordenadaToque.y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                accion = "ACTION_UP"
            }
            MotionEvent.ACTION_CANCEL -> {
                accion = "ACTION_CANCEL"
            }
        }
        Log.i(TAG, "Se realizó la acción $accion en x = ${ubicacionTap.x}, y = ${ubicacionTap.y}")
        return true
    }
}