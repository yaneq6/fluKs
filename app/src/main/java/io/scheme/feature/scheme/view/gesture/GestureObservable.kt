package io.scheme.feature.scheme.view.gesture

import android.view.MotionEvent
import android.view.ScaleGestureDetector
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface GestureObservable {
    val onScaleBegin: Observable<ScaleGestureDetector>
    val onScaleEnd: Observable<ScaleGestureDetector>
    val onScale: Observable<ScaleGestureDetector>
    val onShowPress: Observable<MotionEvent>
    val onLongPress: Observable<MotionEvent>
    val onSingleTapUp: Observable<MotionEvent>
    val onDown: Observable<MotionEvent>
    val onFling: Observable<FlingMotionEvent>
    val onScroll: Observable<ScrollMotionEvent>
    val onDoubleTap: Observable<MotionEvent>
    val onDoubleTapEvent: Observable<MotionEvent>
    val onSingleTapConfirmed: Observable<MotionEvent>
    val onEvent: Observable<Any>
}

data class FlingMotionEvent(
    val event1: MotionEvent?,
    val event2: MotionEvent?,
    val velocityX: Float,
    val velocityY: Float
)

data class ScrollMotionEvent(
    val event1: MotionEvent?,
    val event2: MotionEvent?,
    val distanceX: Float,
    val distanceY: Float
)

class GestureObservableListener :
    GestureObservable,
    GestureListener {
    override val onScaleBegin = PublishSubject.create<ScaleGestureDetector>()!!
    override val onScaleEnd = PublishSubject.create<ScaleGestureDetector>()!!
    override val onScale = PublishSubject.create<ScaleGestureDetector>()!!
    override val onShowPress = PublishSubject.create<MotionEvent>()!!
    override val onLongPress = PublishSubject.create<MotionEvent>()!!
    override val onSingleTapUp = PublishSubject.create<MotionEvent>()!!
    override val onDown = PublishSubject.create<MotionEvent>()!!
    override val onDoubleTap = PublishSubject.create<MotionEvent>()!!
    override val onDoubleTapEvent = PublishSubject.create<MotionEvent>()!!
    override val onSingleTapConfirmed = PublishSubject.create<MotionEvent>()!!
    override val onEvent = PublishSubject.create<Any>()!!
    override val onFling = PublishSubject.create<FlingMotionEvent>()!!
    override val onScroll = PublishSubject.create<ScrollMotionEvent>()!!

    override fun onShowPress(event: MotionEvent?) {
        event?.let(onShowPress::onNext)
    }

    override fun onLongPress(event: MotionEvent?) {
        event?.let(onLongPress::onNext)
    }

    override fun onScaleEnd(detector: ScaleGestureDetector?) {
        detector?.let(onScaleEnd::onNext)
    }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean =
        true.also { detector?.let(onScaleBegin::onNext) }

    override fun onScale(detector: ScaleGestureDetector?): Boolean =
        true.also { detector?.let(onScale::onNext) }

    override fun onSingleTapUp(event: MotionEvent?): Boolean =
        true.also { event?.let(onSingleTapUp::onNext) }

    override fun onDown(event: MotionEvent?): Boolean =
        true.also { event?.let(onDown::onNext) }

    override fun onDoubleTap(event: MotionEvent?): Boolean =
        true.also { event?.let(onDoubleTap::onNext) }

    override fun onDoubleTapEvent(event: MotionEvent?): Boolean =
        true.also { event?.let(onDoubleTapEvent::onNext) }

    override fun onSingleTapConfirmed(event: MotionEvent?): Boolean =
        true.also { event?.let(onSingleTapConfirmed::onNext) }

    override fun onFling(
        event1: MotionEvent?,
        event2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean = true.also {
        FlingMotionEvent(
            event1 = event1,
            event2 = event2,
            velocityX = velocityX,
            velocityY = velocityY
        ).let(onFling::onNext)
    }

    override fun onScroll(
        event1: MotionEvent?,
        event2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean = true.also {
        ScrollMotionEvent(
            event1 = event1,
            event2 = event2,
            distanceX = distanceX,
            distanceY = distanceY
        )
    }
}
