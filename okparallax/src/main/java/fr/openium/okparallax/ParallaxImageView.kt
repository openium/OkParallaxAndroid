package fr.openium.okparallax

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by t.coulange on 17/11/2016.
 */
class ParallaxImageView : ImageView, ParallaxInterface {
    lateinit var delegate: ParallaxDelegate

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        delegate = ParallaxDelegate(true)
    }

    override fun dispatchDraw(canvas: Canvas) {
        delegate.dispatchDraw(canvas, this)
        super.dispatchDraw(canvas)
    }

    override fun getParallaxDelegate(): ParallaxDelegate {
        return delegate
    }
}