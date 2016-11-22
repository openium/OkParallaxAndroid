package fr.openium.okparallax

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout


/**
 * Created by t.coulange on 17/11/2016.
 */
open class ParallaxOverlayHeader : FrameLayout, ParallaxInterface, NestedScrollView.OnScrollChangeListener {
    var onParallaxScrollListener: OnParallaxScrollListener? = null
        set(value) {
            field = value
            value?.onParallaxScroll(0f, 0f, this)
        }
    var header: View? = null
    var overlay: View? = null
    var scrollMultiplier = 0.5f
    lateinit var recyclerViewScrollListener: RecyclerView.OnScrollListener


    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
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
                addHeader(headerL)
                addOverlay(overlayL)


                scrollMultiplier = a.getFloat(R.styleable.ParallaxOverlayHeader_multiplier, 0.5f)
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

    fun addHeader(@DrawableRes headerL: Int) {
        require(header == null, { "header already added" })
        if (headerL != 0) {
            inflate(context, headerL, this)
            header = getChildAt(childCount - 1)
        }
    }

    /**
     * Should be called after addHeader
     */
    fun addOverlay(@DrawableRes overlayL: Int) {
        require(overlay == null, { "overlay already added" })
        if (overlayL != 0) {
            inflate(context, overlayL, this)
            overlay = getChildAt(childCount - 1)
        }
    }

    fun addHeader(headerV: View) {
        require(header == null, { "header already added" })
        addView(headerV)
        header = headerV
    }

    /**
     * Should be called after addHeader
     */
    fun addOverlay(overlayV: View) {
        require(overlay == null, { "overlay already added" })
        addView(overlayV)
        overlay = overlayV
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
