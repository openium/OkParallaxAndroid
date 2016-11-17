package com.myapp.sampleheadercoordinator

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View

/**
 * Created by t.coulange on 16/11/2016.
 */
class ParallaxDelegate(private val mShouldClip: Boolean) {
    private var mOffset: Int = 0

    fun dispatchDraw(canvas: Canvas, view: View) {
        if (mShouldClip) {
            canvas.clipRect(Rect(view.left, view.top, view.right, view.bottom + mOffset))
        }
    }

    fun setClipY(view: View, offset: Int) {
        mOffset = offset
        view.invalidate()
    }
}