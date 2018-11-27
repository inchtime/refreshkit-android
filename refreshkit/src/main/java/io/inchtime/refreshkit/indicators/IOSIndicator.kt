package io.inchtime.refreshkit.indicators

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.view.animation.LinearInterpolator

class IOSIndicator : Indicator() {

    private var duration = 800L

    private var degree = 0f

    private val colors = intArrayOf(
        0xAADDDDDD.toInt(),
        0xAADDDDDD.toInt(),
        0xAADDDDDD.toInt(),
        0xAACCCCCC.toInt(),
        0xAABBBBBB.toInt(),
        0xAAAAAAAA.toInt(),
        0xAA999999.toInt(),
        0xAA888888.toInt(),
        0xAA777777.toInt(),
        0xAA666666.toInt(),
        0xAA555555.toInt(),
        0xAA555555.toInt()
    )

    override fun draw(canvas: Canvas, paint: Paint) {

        val x = width / 2f
        val y = height / 2f
        val radius = x / 12f

        canvas.save()

        canvas.translate(x, y)

        val d = degree - degree % 30

        canvas.rotate(d)

        for (i in 0 until colors.size) {

            paint.style = Paint.Style.FILL
            paint.color = colors[i]

            canvas.rotate(30f)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect(x / 2.5f, -radius, x, radius, radius, radius, paint)
            } else {
                canvas.drawRect(x / 2.5f, -radius, x, radius, paint)
            }

        }

        canvas.restore()

    }

    override fun onCreateAnimators(): ArrayList<ValueAnimator> {

        val animators = ArrayList<ValueAnimator>()

        val animator = ValueAnimator.ofFloat(0f, 360f)
        animator.interpolator = LinearInterpolator()
        animator.duration = duration
        animator.repeatCount = -1
        addUpdateListener(animator, ValueAnimator.AnimatorUpdateListener { animation ->
            degree = animation.animatedValue as Float
            invalidateSelf()
        })

        animators.add(animator)

        return animators
    }

}