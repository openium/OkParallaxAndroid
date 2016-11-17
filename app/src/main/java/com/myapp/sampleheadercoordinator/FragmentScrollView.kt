package com.myapp.sampleheadercoordinator

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*


/**
 * Created by t.coulange on 19/10/2016.
 */
class FragmentScrollView : Fragment() {
//    override fun onParallaxScroll(percentage: Float, offset: Float, parallax: View?) {
//        val c = toolbar.getBackground()
//        if (c != null) {
//            c.setAlpha(Math.round(percentage * 255))
//            toolbar.setBackground(c)
//        }
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scroll, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

//        recyclerView.layoutManager = LinearLayoutManager(context)
//        val adapter = AdapterRecycler()
//        adapter.setOnParallaxScroll { fl, fl, view ->

//        }
//        recyclerView.adapter = adapter
//        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    val firstVisiblePosition = (recyclerView!!.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition();
//                    if (firstVisiblePosition == 0) {
//                        appbar.setExpanded(true, true);
//                    }
//                }
//            }
//        })
//        adapter.setParallaxHeader(R.layout.header, recyclerView, R.layout.overlay)
//        adapter.setOnParallaxScroll(this)
//        collapsing_toolbar.setGooglePlayBehaviour(true)
    }

//    class AdapterRecycler : CustomParallaxRecyclerAdapter<String>(mutableListOf()) {
//        override fun getItemCountImpl(p0: CustomParallaxRecyclerAdapter<String>): Int {
//            return 30
//        }
//
//        override fun onBindViewHolderImpl(holder: RecyclerView.ViewHolder, p1: CustomParallaxRecyclerAdapter<String>, position: Int) {
//            (holder.itemView as TextView).text = "Item $position"
//        }
//
//        override fun onCreateViewHolderImpl(parent: ViewGroup, p1: CustomParallaxRecyclerAdapter<String>, p2: Int): RecyclerView.ViewHolder {
//            val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
//            return CustomHolder(view)
//        }
//
////        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
////
////        }
////
////        override fun getItemCount(): Int {
////            return 30
////        }
////
////        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
////            val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
////            return CustomHolder(view)
////        }
//
//
//        class CustomHolder(view: View) : RecyclerView.ViewHolder(view) {
//
//        }
//
//    }
}