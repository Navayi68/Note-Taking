package com.example.notetaking.UI.Gesture

import android.view.MotionEvent

interface GestureListenerInterface {
    fun onSwipeGesture(
        gestureConstants: GestureConstants,
        downMotionEvent: MotionEvent,
        moveMotionEvent: MotionEvent,
        initVelocityX: Float,
        initVelocityY: Float
    ) {
    }

    fun onSingleTapUp(motionEvent: MotionEvent) {}
    fun onLongPress(motionEvent: MotionEvent) {}
}