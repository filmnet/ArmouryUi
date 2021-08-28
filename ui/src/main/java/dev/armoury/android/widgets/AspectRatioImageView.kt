package dev.armoury.android.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import dev.armoury.android.R

class AspectRatioImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var aspectRatio: Float = INVALID_RATIO
    private var dominantMeasurement: Int = MEASUREMENT_WIDTH

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView)

        aspectRatio = a.getFloat(R.styleable.AspectRatioImageView_aspectRatio, aspectRatio)
        dominantMeasurement = a.getInt(
            R.styleable.AspectRatioImageView_dominantMeasurement,
            dominantMeasurement
        )

        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (aspectRatio == INVALID_RATIO) return
        val newWidth: Int
        val newHeight: Int
        when (dominantMeasurement) {
            MEASUREMENT_WIDTH -> {
                newWidth = measuredWidth
                newHeight = (measuredWidth * aspectRatio).toInt()
            }
            MEASUREMENT_HEIGHT -> {
                newWidth = (measuredHeight * aspectRatio).toInt()
                newHeight = measuredHeight
            }
            else -> throw IllegalArgumentException("Unknown measurement with ID $dominantMeasurement")
        }
        setMeasuredDimension(newWidth, newHeight)
    }

    companion object {
        private const val INVALID_RATIO = -1f

        //  Measurements
        private const val MEASUREMENT_WIDTH = 0
        private const val MEASUREMENT_HEIGHT = 1
    }
}