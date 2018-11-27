package io.inchtime.refreshkit

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.RelativeLayout

open class RefreshLayout constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    RelativeLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    private lateinit var detector: GestureDetectorCompat

    protected val interpolator = AccelerateInterpolator()

    protected var refreshContent: View? = null

    protected var scrollRate = .6f

    /// 用户手指在屏幕上滑动的距离
    protected var scrollY = 0f

    protected var headRefreshThreshold = 100f

    init {
        setupGestureDetector()
    }

    private fun setupGestureDetector() {

        detector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {

            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                //
                val offsetY = e2.rawY - e1.rawY
                scrollY = offsetY * scrollRate
                //

                for (i in 0..childCount) {

                    val view = getChildAt(i)

                    if (view != null) {
                        view.translationY = scrollY
                    }
                }
                return true
            }

            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                println("velocityY: $velocityY")
                return true
            }
        })
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        when (event.actionMasked) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {

                if (scrollY >= headRefreshThreshold) {

                    val reboundAnimator =
                        ObjectAnimator.ofFloat(this, "translationY", translationY, headRefreshThreshold)
                    reboundAnimator.duration = 200
                    reboundAnimator.interpolator = interpolator
                    reboundAnimator.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationCancel(animation: Animator) {
                            super.onAnimationCancel(animation)
                        }

                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                        }
                    })
                    reboundAnimator.addUpdateListener { animation ->

                    }
                    reboundAnimator.start()
                }
            }
        }

        return super.dispatchTouchEvent(event)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.actionMasked) {
            MotionEvent.ACTION_UP -> {
                performClick()
            }
        }

        return detector.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }


//
//    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

//    }
//
//    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
//        return p is LayoutParams
//    }
//
//    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
//        return LayoutParams(MATCH_PARENT, MATCH_PARENT)
//    }
//
//    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
//        return LayoutParams(context, attrs)
//    }
//
//    override fun generateLayoutParams(lp: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
//        return RefreshLayout.LayoutParams(lp)
//    }
//
//    open class LayoutParams : MarginLayoutParams {
//
//        constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
//
//        constructor(width: Int, height: Int) : super(width, height)
//
//        constructor(source: MarginLayoutParams) : super(source)
//
//        constructor(source: ViewGroup.LayoutParams) : super(source)
//
//    }
}