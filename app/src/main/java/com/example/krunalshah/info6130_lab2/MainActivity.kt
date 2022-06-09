package com.example.krunalshah.info6130_lab2

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import android.media.MediaPlayer
import android.os.Bundle
import android.view.ViewAnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.lang.Math.hypot

class MainActivity : AppCompatActivity() {

    private lateinit var name: TextView
    private lateinit var stopButton: Button
    private lateinit var layout: ConstraintLayout
    var mediaPlayer: MediaPlayer? = null
    private lateinit var aView: AnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        name = findViewById(R.id.myName)
        layout = findViewById(R.id.cLayout)
        stopButton = findViewById(R.id.stopButton)
        aView = findViewById(R.id.aView)

        mediaPlayer = MediaPlayer.create(this,R.raw.beat_it)

        layout.setOnClickListener {
            name.clearAnimation()
            stopButton.clearAnimation()

            mediaPlayer?.start()
        }

        stopButton.setOnClickListener {
            mediaPlayer?.pause()

            aView.clearAnimation()
            layout.clearAnimation()

            name.animate().apply {
                rotationXBy(360f)
            }.start()

            stopButton.animate().apply {
                rotationYBy(360f)
            }.start()
        }
    }
}