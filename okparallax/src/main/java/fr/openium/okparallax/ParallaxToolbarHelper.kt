package fr.openium.okparallax

import android.widget.Toolbar

/**
 * Created by t.coulange on 18/11/2016.
 */
class ParallaxToolbarHelper {
    companion object {
        fun doAlpha(toolbar: Toolbar, percentage: Float) {
            val c = toolbar.getBackground()
            if (c != null) {
                c.setAlpha(Math.round(percentage * 255))
                toolbar.setBackground(c)
            }
        }
    }
}