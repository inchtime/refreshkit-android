package io.inchtime.refreshkit.indicators

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable

abstract class Indicator : Drawable(), Animatable {

    private val updateListeners = HashMap<ValueAnimator, ValueAnimator.AnimatorUpdateListener>()

    private var animators: ArrayList<ValueAnimator> = ArrayList()

    private var alpha = 255

    private var mHasAnimators: Boolean = false

    private val paint = Paint()

    var color: Int
        get() = paint.color
        set(color) {
            paint.color = color
        }

    private val isStarted: Boolean
        get() {
            for (animator in animators) {
                return animator.isStarted
            }
            return false
        }

    val width: Int
        get() = drawBounds.width()

    val height: Int
        get() = drawBounds.height()

    init {
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
    }

    override fun setAlpha(alpha: Int) {
        this.alpha = alpha
    }

    override fun getAlpha(): Int {
        return alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun draw(canvas: Canvas) {
        draw(canvas, paint)
    }

    abstract fun draw(canvas: Canvas, paint: Paint)

    abstract fun onCreateAnimators(): ArrayList<ValueAnimator>

    override fun start() {

        ensureAnimators()

        if (isStarted) {
            return
        }

        startAnimators()

        invalidateSelf()
    }

    private fun startAnimators() {

        for (animator in animators) {
            val updateListener = updateListeners[animator]
            if (updateListener != null) {
                animator.addUpdateListener(updateListener)
            }
            animator.start()
        }

    }

    private fun stopAnimators() {
        for (animator in animators) {
            if (animator.isStarted) {
                animator.removeAllUpdateListeners()
                animator.end()
            }
        }
    }

    private fun ensureAnimators() {
        if (!mHasAnimators) {
            animators = onCreateAnimators()
            mHasAnimators = true
        }
    }

    override fun stop() {
        stopAnimators()
    }

    override fun isRunning(): Boolean {
        for (animator in animators) {
            return animator.isRunning
        }
        return false
    }

    /**
     * Your should use this to add AnimatorUpdateListener when
     * create animator , otherwise , animator doesn't work when
     * the animation restart .
     * @param animator
     * @param updateListener
     */
    fun addUpdateListener(animator: ValueAnimator, updateListener: ValueAnimator.AnimatorUpdateListener) {
        updateListeners[animator] = updateListener
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        drawBounds = bounds
    }

    var drawBounds = Rect()

    fun centerX(): Int {
        return drawBounds.centerX()
    }

    fun centerY(): Int {
        return drawBounds.centerY()
    }

    fun exactCenterX(): Float {
        return drawBounds.exactCenterX()
    }

    fun exactCenterY(): Float {
        return drawBounds.exactCenterY()
    }

    companion object {
        private val ZERO_BOUNDS_RECT = Rect()
    }

}