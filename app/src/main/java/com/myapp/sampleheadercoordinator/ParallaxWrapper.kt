package com.myapp.sampleheadercoordinator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import android.widget.RelativeLayout

/**
 * Created by t.coulange on 16/11/2016.
 */
class ParallaxWrapper(context: Context, private val mShouldClip: Boolean, layout: Int) : RelativeLayout(context) {

    private var mOffset: Int = 0

    init {
        inflate(context, layout, this)
    }


    override fun dispatchDraw(canvas: Canvas) {
        if (mShouldClip) {
            canvas.clipRect(Rect(left, top, right, bottom + mOffset))
        }
        super.dispatchDraw(canvas)
    }

    fun setClipY(offset: Int) {
        Log.d("tag", "offset $offset")
        mOffset = offset
        invalidate()
    }
}