package fr.openium.okparallax

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout


/**
 * Created by t.coulange on 17/11/2016.
 */
class ParallaxOverlayHeader : FrameLayout, ParallaxInterface, NestedScrollView.OnScrollChangeListener {
    var onParallaxScrollListener: OnParallaxScrollListener? = null
        set(value) {
            field = value
            value?.onParallaxScroll(0f, 0f, this)
        }
    private var header: View? = null
    private var overlay: View? = null
    private var scrollMultiplier = 0.5f
    lateinit var recyclerViewScrollListener: RecyclerView.OnScrollListener


    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet? = null) {

        attrs?.let {
            val a = context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.ParallaxOverlayHeader,
                    0, 0)

            try {
                val headerL = a.getResourceId(R.styleable.ParallaxOverlayHeader_header, 0)
                val overlayL = a.getResourceId(R.styleable.ParallaxOverlayHeader_overlay, 0)

                if (headerL != 0) {
                    inflate(context, headerL, this)
                    header = getChildAt(childCount - 1)
                }
                if (overlayL != 0) {
                    inflate(context, overlayL, this)
                    overlay = getChildAt(childCount - 1)
                }
            } finally {
                a.recycle()
            }
        }
        recyclerViewScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                translateHeader((if (recyclerView.layoutManager.getChildAt(0) === this@ParallaxOverlayHeader)
                    recyclerView.computeVerticalScrollOffset()
                else
                    header!!.height).toFloat())
            }
        }
    }

    override fun getParallaxDelegate(): ParallaxDelegate {
        return (header as ParallaxInterface).getParallaxDelegate()
    }


    override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        translateHeader(v!!.computeVerticalScrollOffset().toFloat())
    }

    fun translateHeader(of: Float) {
        val ofCalculated = of * scrollMultiplier
        if (of < height) {
            header?.translationY = ofCalculated
        }
        (header as? ParallaxInterface)?.getParallaxDelegate()?.setClipY(header!!, Math.round(ofCalculated))
        if (onParallaxScrollListener != null) {
            val left = Math.min(1f, ofCalculated / (header!!.height * scrollMultiplier))
            onParallaxScrollListener!!.onParallaxScroll(left, of, header!!)
        }
    }


}
