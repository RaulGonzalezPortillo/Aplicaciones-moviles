package com.example.hipnosis

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.util.SizeF
import android.view.MotionEvent
import android.view.View
import kotlin.math.min
import kotlin.random.Random

private const val TAG = "VistaHipnosis"

class VistaHipnosis(context: Context, attrs: AttributeSet? = null): View(context, attrs) {
    private val paintLinea = Paint().apply {
        color = Color.RED
        strokeWidth = 10F
    }

    private val paintCirculo = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10F
        style = Paint.Style.STROKE
    }

    private fun dibujaTache(canvas: Canvas) {
        //Inicio X, Inicio Y, Fin X, Fin Y
        canvas.drawLine(0F, 0F, width.toFloat(), height.toFloat(), paintLinea)
        canvas.drawLine(width.toFloat(), 0F, 0F, height.toFloat(), paintLinea)
    }

    private fun dibujaCirculo(canvas: Canvas) {
        val tamanoVista = SizeF(measuredWidth.toFloat(), measuredHeight.toFloat())
        val radioCirculo = min(tamanoVista.width / 2, tamanoVista.height / 2)
        canvas.drawCircle(tamanoVista.width / 2, tamanoVista.height / 2, radioCirculo - 50, paintCirculo)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        dibujaCirculo(canvas)
        dibujaTache(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val ubicacionTap = PointF(event.x, event.y)
        var accion = ""
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                accion = "ACTION_DOWN"
                paintLinea.color = Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
                paintCirculo.color = Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
                //Pide al sistema que en el siguiente refresco de pantalla vuelva a pintar la vista
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                accion = "ACTION_MOVE"
                paintLinea.color = Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
                paintCirculo.color = Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
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