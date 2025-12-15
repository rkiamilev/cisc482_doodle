package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View

// This custom view handles the manual drawing logic using Android Graphics APIs
class DrawingView(context: Context) : View(context) {

    // Path represents the drawing line being drawn by the user
    private var drawPath: Path = Path()

    // Paint defines the style (color, width, texture) of the drawing
    // Reference: https://developer.android.com/reference/android/graphics/Paint
    private var drawPaint: Paint = Paint()

    // Canvas and Bitmap for double buffering (saving the drawing to memory)
    private var canvasPaint: Paint = Paint(Paint.DITHER_FLAG)
    private lateinit var drawCanvas: Canvas
    private lateinit var canvasBitmap: Bitmap

    init {
        setupDrawing()
    }

    private fun setupDrawing() {
        // Configure the Paint object based on the official documentation
        drawPaint.color = Color.BLACK
        drawPaint.isAntiAlias = true // Smooths out the edges of the lines
        drawPaint.strokeWidth = 20f // Sets the default brush size
        drawPaint.style = Paint.Style.STROKE // We want a line, not a filled shape
        drawPaint.strokeJoin = Paint.Join.ROUND // Makes joints between line segments round
        drawPaint.strokeCap = Paint.Cap.ROUND // Makes the start and end of lines round
    }

    // Called when the view size changes (e.g., screen rotation or initialization)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Create a bitmap to hold the drawing pixel data
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap)
    }

    // Called every time the view needs to render
    override fun onDraw(canvas: Canvas) {
        // Draw the saved bitmap (background drawing)
        canvas.drawBitmap(canvasBitmap, 0f, 0f, canvasPaint)
        // Draw the current path (the line currently being drawn by the finger)
        canvas.drawPath(drawPath, drawPaint)
    }

    // Handle touch events to create the path
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Start a new path where the user touches
                drawPath.moveTo(touchX, touchY)
            }
            MotionEvent.ACTION_MOVE -> {
                // Draw a line to the new position as the finger moves
                drawPath.lineTo(touchX, touchY)
            }
            MotionEvent.ACTION_UP -> {
                // When finger is lifted, draw the path to the permanent bitmap
                drawCanvas.drawPath(drawPath, drawPaint)
                // Reset the path so we don't draw the previous line again
                drawPath.reset()
            }
            else -> return false
        }

        // Force the view to redraw itself with the new data
        invalidate()
        return true
    }

    // Method to change brush color dynamically
    fun setColor(newColor: String) {
        drawPaint.color = Color.parseColor(newColor)
        invalidate()
    }
}