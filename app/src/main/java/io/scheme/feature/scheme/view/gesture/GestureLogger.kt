package io.scheme.feature.scheme.view.gesture

import android.view.MotionEvent
import android.view.ScaleGestureDetector

class GestureLogger :
    GestureListener {

    override fun onScale(detector: ScaleGestureDetector?): Boolean {
        println("onScale: $detector")
        return false
    }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        println("onScaleBegin: $detector")
        return false
    }

    override fun onScaleEnd(detector: ScaleGestureDetector?) {
        println("onScaleEnd: $detector")
    }

    override fun onDoubleTap(event: MotionEvent?): Boolean {
        println("onDoubleTap: $event")
        return false
    }

    override fun onDoubleTapEvent(event: MotionEvent?): Boolean {
        println("onDoubleTapEvent: $event")
        return false
    }

    override fun onSingleTapConfirmed(event: MotionEvent?): Boolean {
        println("onSingleTapConfirmed: $event")
        return false
    }

    override fun onShowPress(event: MotionEvent?) {
        println("onShowPress: $event")
    }

    override fun onSingleTapUp(event: MotionEvent?): Boolean {
        println("onSingleTapUp: $event")
        return false
    }

    override fun onDown(event: MotionEvent?): Boolean {
        println("onDown: $event")
        return false
    }

    override fun onFling(event1: MotionEvent?, event2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        println("onFling1: $event1")
        println("onFling2: $event2")
        return false
    }

    override fun onScroll(event1: MotionEvent?, event2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        println("onScroll1: $event1")
        println("onScroll2: $event2")
        return false
    }

    override fun onLongPress(event: MotionEvent?) {
        println("onLongPress: $event")
    }
}
