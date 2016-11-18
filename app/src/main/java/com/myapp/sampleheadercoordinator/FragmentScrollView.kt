package com.myapp.sampleheadercoordinator

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.openium.okparallax.OnParallaxScrollListener
import fr.openium.okparallax.ParallaxOverlayHeader
import kotlinx.android.synthetic.main.fragment_scroll.*


/**
 * Created by t.coulange on 19/10/2016.
 */
class FragmentScrollView : Fragment(), OnParallaxScrollListener {
    override fun onParallaxScroll(percentage: Float, offset: Float, parallax: View?) {
        val c = toolbar.getBackground()
        if (c != null) {
            c.setAlpha(Math.round(percentage * 255))
            toolbar.setBackground(c)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scroll, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val parallax = ll.getChildAt(0) as ParallaxOverlayHeader
        nestedScrollview.setOnScrollChangeListener(parallax)
        parallax.onParallaxScrollListener = this

    }
}