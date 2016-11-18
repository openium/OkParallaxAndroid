package fr.openium.okparallax

import android.view.View

/**
 * Created by t.coulange on 18/11/2016.
 */
interface OnParallaxScrollListener {
    fun onParallaxScroll(percentage: Float, offset: Float, parallax: View?)
}