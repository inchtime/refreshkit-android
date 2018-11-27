package io.inchtime.refreshkit.indicators

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.LinearInterpolator

class VisualClockIndicator: Indicator() {

    private var duration = 800L

    private var secondHandDegree = 0f

    private var minuteHandDegree = 0f

    override fun draw(canvas: Canvas, paint: Paint) {

        paint.strokeWidth = 3f
        paint.style = Paint.Style.STROKE

        val x = (width / 2).toFloat()
        val y = (height / 2).toFloat()

        canvas.drawCircle(x, y, width / 2.0f - 8f, paint)

        canvas.save()

        canvas.translate(x, y)
        canvas.rotate(secondHandDegree)
        canvas.drawLine(0f, 0f, width / 3f, 0f, paint)

        canvas.restore()

        canvas.translate(x, y)
        canvas.rotate(minuteHandDegree)
        canvas.drawLine(0f, 0f, width / 4f, 0f, paint)

    }

    override fun onCreateAnimators(): ArrayList<ValueAnimator> {

        val animators = ArrayList<ValueAnimator>()

        val secondHandAnimator = ValueAnimator.ofFloat(0f, 360f)
        secondHandAnimator.interpolator = LinearInterpolator()
        secondHandAnimator.duration = duration
        secondHandAnimator.repeatCount = -1
        addUpdateListener(secondHandAnimator, ValueAnimator.AnimatorUpdateListener { animation ->
            secondHandDegree = animation.animatedValue as Float
            invalidateSelf()
        })

        val minuteHandAnimator = ValueAnimator.ofFloat(0f, 360f)
        minuteHandAnimator.interpolator = LinearInterpolator()
        minuteHandAnimator.duration = duration * 6
        minuteHandAnimator.repeatCount = -1
        addUpdateListener(minuteHandAnimator, ValueAnimator.AnimatorUpdateListener { animation ->
            minuteHandDegree = animation.animatedValue as Float
            invalidateSelf()
        })
        animators.add(secondHandAnimator)
        animators.add(minuteHandAnimator)
        return animators
    }

}