package com.example.hipnosis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.animation.AnimationUtils
import androidx.core.view.GestureDetectorCompat

private const val TAG = "ActividadHipnosis"
private lateinit var vistaHipnosis: VistaHipnosis
private var factorEscala = 1F

class ActividadHipnosis : AppCompatActivity() {
    private lateinit var detectorGestos: GestureDetectorCompat
    private lateinit var detectorEscalamiento: ScaleGestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_hipnosis)
        supportActionBar?.hide()
        vistaHipnosis = findViewById(R.id.vistaHipnosis)
        //detectorGestos = GestureDetectorCompat(this, this)
        detectorGestos = GestureDetectorCompat(this, ListenerGestos())
        detectorEscalamiento = ScaleGestureDetector(this, ListenerEscala())
        AnimationUtils.loadAnimation(this, R.anim.animacion).also {
            animacion ->
            vistaHipnosis.startAnimation(animacion)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        /*
        val posicion = PointF(event.x, event.y)
        var evento = ""
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                evento = "ACTION_DOWN"
            }
            MotionEvent.ACTION_MOVE -> {
                vistaHipnosis.x = posicion.x
                vistaHipnosis.y = posicion.y
                evento = "ACTION_MOVE"
            }
            MotionEvent.ACTION_UP -> {
                evento = "ACTION_UP"
            }
            MotionEvent.ACTION_CANCEL -> {
                evento = "ACTION_CANCEL"
            }
        }
        Log.i(TAG, "$evento sucedió en x = ${posicion.x}, y = ${posicion.y}")
        return true
        */
        if(detectorGestos.onTouchEvent(event) || detectorEscalamiento.onTouchEvent(event)) {
            return true
        }
        super.onTouchEvent(event)
        return false
    }

    private class ListenerEscala: ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            Log.d(TAG, "onScale: scaleFactor = ${detector.scaleFactor}")
            factorEscala *= detector.scaleFactor
            Log.d(TAG, "onScale: factorEscala = $factorEscala")
            factorEscala = 0.1F.coerceAtLeast(factorEscala.coerceAtMost(2F))
            vistaHipnosis.scaleX = factorEscala
            vistaHipnosis.scaleY = factorEscala
            return true
        }
    }

    private class ListenerGestos: GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            Log.d(TAG, "onDown en el Listener anidado")
            return true
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            Log.d(TAG, "onScroll en el Listener anidado")
            vistaHipnosis.x -= distanceX
            vistaHipnosis.y -= distanceY
            return true
        }

        override fun onLongPress(e: MotionEvent?) {
            AnimationUtils.loadAnimation(vistaHipnosis.context, R.anim.animacion).also {
                animacion ->
                vistaHipnosis.startAnimation(animacion)
            }
        }
    }

    /*
    override fun onDown(p0: MotionEvent?): Boolean {
        Log.d(TAG, "onDown: $p0")
        return true
    }

    override fun onShowPress(p0: MotionEvent?) {
        Log.d(TAG, "onShowPress: $p0")
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        Log.d(TAG, "onSingleTapUp: $p0")
        return true
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        Log.d(TAG, "onScroll: $p0, $p1, $p2, $p3")
        vistaHipnosis.x -= p2
        vistaHipnosis.y -= p3
        return true
    }

    override fun onLongPress(p0: MotionEvent?) {
        Log.d(TAG, "onLongPress: $p0")
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        Log.d(TAG, "onFling: $p0, $p1, $p2, $p3")
        return true
    }

    override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean {
        Log.d(TAG, "onSingleTapConfirmed: $p0")
        return true
    }

    override fun onDoubleTap(p0: MotionEvent?): Boolean {
        Log.d(TAG, "onDoubleTap: $p0")
        return true
    }

    override fun onDoubleTapEvent(p0: MotionEvent?): Boolean {
        Log.d(TAG, "onDoubleTapEvent: $p0")
        return true
    }*/
}