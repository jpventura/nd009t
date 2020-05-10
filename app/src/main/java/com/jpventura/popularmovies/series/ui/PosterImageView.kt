package com.jpventura.popularmovies.series.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class PosterImageView(
    context: Context,
    attributes: AttributeSet?
) : AppCompatImageView(context, attributes) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, (1.33*measuredWidth).toInt())
    }

}
