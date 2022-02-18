package com.udacity

import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.RESTART
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var background = 0
    private var downloadColor = 0
    private var animationPercentage = 0.0f

    private val valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, _, new ->
        if (new == ButtonState.Clicked) {
            valueAnimator.start()
            invalidate()
        }
        if (new == ButtonState.Completed) {
            valueAnimator.end()
            valueAnimator.cancel()
            invalidate()
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }


    init {
        buttonState = ButtonState.Completed
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            background = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            downloadColor = getColor(R.styleable.LoadingButton_downloadColor, 0)
        }

        valueAnimator.apply {
            duration = 100000
            interpolator = DecelerateInterpolator(7.0f)

            addUpdateListener { updatedAnimation ->
                animationPercentage = updatedAnimation.animatedValue as Float
                invalidate()
            }
        }
        valueAnimator.repeatMode = RESTART
        valueAnimator.repeatCount = INFINITE


    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = background
        canvas.drawRect(0.0F, 0.0F, widthSize.toFloat(), heightSize.toFloat(), paint)
        paint.color = downloadColor
        canvas.drawRect(
            0.0F,
            0.0F,
            widthSize * animationPercentage,
            heightSize.toFloat(),
            paint
        )
        paint.color = Color.WHITE
        canvas.drawText(
            "Download",
            (widthSize * 0.5).toFloat(),
            (heightSize * 0.5).toFloat(),
            paint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}