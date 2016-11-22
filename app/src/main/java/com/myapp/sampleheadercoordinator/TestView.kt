package com.myapp.sampleheadercoordinator

import android.content.Context
import android.graphics.Color
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import fr.openium.okparallax.ParallaxImageView
import fr.openium.okparallax.ParallaxOverlayHeader

/**
 * Created by t.coulange on 18/11/2016.
 */
class TestView : ParallaxOverlayHeader {
    lateinit var imageView: ImageView

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        imageView = ParallaxImageView(context)
        imageView.adjustViewBounds = true
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.scaleX = 1.2F
        imageView.scaleY = 1.2F
        imageView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        addHeader(imageView)

        val ov = ParallaxImageView(context)
        ov.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM)
        ov.adjustViewBounds = true
        ov.scaleType = ImageView.ScaleType.FIT_XY
        ov.setImageResource(R.drawable.wave_background_resgen)
        addOverlay(ov)
        setBackgroundColor(Color.BLACK)
    }

    fun setIVBackground(@DrawableRes id: Int) {
        if (id != 0) {
            imageView.setImageResource(id)
        }
    }


}
