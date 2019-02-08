package io.fluks.feature.scheme.view.gesture

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector

interface GestureListener :
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener,
    ScaleGestureDetector.OnScaleGestureListener {
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


@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
class CompoundGestureListener(
    vararg listeners: GestureDetector.OnGestureListener
) : GestureListener,
    GestureDetector.OnGestureListener by CompoundOnGestureListener(*listeners),
    GestureDetector.OnDoubleTapListener by CompoundOnDoubleTapListener(
        *listeners.filterIsInstance<GestureDetector.OnDoubleTapListener>().toTypedArray()
    ),
    ScaleGestureDetector.OnScaleGestureListener by CompoundOnScaleGestureListener(
        *listeners.filterIsInstance<ScaleGestureDetector.OnScaleGestureListener>().toTypedArray()
    ),
    Collection<GestureDetector.OnGestureListener> by listeners.asList()

class CompoundOnGestureListener(
    vararg listeners: GestureDetector.OnGestureListener
) : GestureDetector.OnGestureListener,
    Collection<GestureDetector.OnGestureListener> by listeners.asList() {

    override fun onShowPress(e: MotionEvent?) =
        forEach { it.onShowPress(e) }

    override fun onLongPress(e: MotionEvent?) =
        forEach { it.onLongPress((e)) }

    override fun onSingleTapUp(e: MotionEvent?): Boolean =
        map { it.onSingleTapUp(e) }.any { it }


    override fun onDown(e: MotionEvent?): Boolean =
        map { it.onDown(e) }.any { it }


    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean =
        map { it.onFling(e1, e2, velocityX, velocityY) }.any { it }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean =
        map { it.onScroll(e1, e2, distanceX, distanceY) }.any { it }

}

class CompoundOnDoubleTapListener(
    vararg listeners: GestureDetector.OnDoubleTapListener
) : GestureDetector.OnDoubleTapListener,
    Collection<GestureDetector.OnDoubleTapListener> by listeners.asList() {

    override fun onDoubleTap(e: MotionEvent?): Boolean =
        map { it.onDoubleTap(e) }.any { true }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean =
        map { it.onDoubleTapEvent(e) }.any { true }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean =
        map { it.onSingleTapConfirmed(e) }.any { true }
}

class CompoundOnScaleGestureListener(
    vararg listeners: ScaleGestureDetector.OnScaleGestureListener
) : ScaleGestureDetector.OnScaleGestureListener,
    Collection<ScaleGestureDetector.OnScaleGestureListener> by listeners.asList() {

    override fun onScale(detector: ScaleGestureDetector?): Boolean =
        map { it.onScale(detector) }.any { it }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean =
        map { it.onScaleBegin(detector) }.any { it }

    override fun onScaleEnd(detector: ScaleGestureDetector?): Unit =
        forEach { it.onScale(detector) }

}
