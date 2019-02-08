package io.scheme.feature.scheme.view.gesture

import android.view.MotionEvent
import android.view.ScaleGestureDetector

class GestureViewController : GestureListener {

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean = false

    override fun onScaleEnd(detector: ScaleGestureDetector?) = Unit

    override fun onScale(detector: ScaleGestureDetector?): Boolean = false

    override fun onShowPress(event: MotionEvent?) {}

    override fun onLongPress(event: MotionEvent?) {}

    override fun onSingleTapUp(event: MotionEvent?): Boolean = false

    override fun onDown(event: MotionEvent?): Boolean = false

    override fun onFling(event1: MotionEvent?, event2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean = false

    override fun onScroll(event1: MotionEvent?, event2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean = false

    override fun onDoubleTap(event: MotionEvent?): Boolean = false

    override fun onDoubleTapEvent(event: MotionEvent?): Boolean = false

    override fun onSingleTapConfirmed(event: MotionEvent?): Boolean = false
}