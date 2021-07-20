package com.example.myapplication

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import kotlin.math.abs

class MySlide(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var slider: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.silde_layout, this, true)
        slider = findViewById(R.id.slider)
    }

    private var lastDispatchX = 0
    private var lastDispatchY = 0
    private var isIntercepted = false
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x.toInt()
        val y = ev.y.toInt()
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isIntercepted) {
                    val dx = abs(x - lastDispatchX)
                    val dy = abs(y - lastDispatchY)
                    if (dx > dy) {
                        isIntercepted = true
                        parent.requestDisallowInterceptTouchEvent(true)
                    } else {
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isIntercepted = false
            }
        }
        lastDispatchX = x
        lastDispatchY = y
        return super.dispatchTouchEvent(ev)
    }

    private var allowSlide = false

    private var lastX = 0
    private var lastY = 0

    private var lastLeft = 0
    private var lastRight = 0
    private var lastTop = 0
    private var lastBottom = 0

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x: Int = event.x.toInt()
        val y: Int = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (x >= slider.left && x <= slider.right) {
                    allowSlide = true
                    lastX = x
                    lastY = y
                    lastLeft = slider.left
                    lastRight = slider.right
                    lastTop = slider.top
                    lastBottom = slider.bottom
                } else {
                    allowSlide = false
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (allowSlide) {
                    val dx = x - lastX
                    val dy = 0
                    var left = slider.left + dx
                    var right = slider.right + dx
                    var top = slider.top + dy
                    var bottom = slider.bottom + dy

                    if (left < 0) {
                        right -= left
                        left = 0
                    }
                    if (right > measuredWidth) {
                        right = measuredWidth
                        left = right - slider.width
                    }

                    slider.layout(left, top, right, bottom)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (allowSlide) {
                    slider.layout(lastLeft, lastTop, lastRight, lastBottom)
                }
            }

        }
        lastX = x
        lastY = y
        return true
    }
}