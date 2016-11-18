package com.myapp.sampleheadercoordinator

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fr.openium.okparallax.OnParallaxScrollListener
import fr.openium.okparallax.ParallaxRecyclerDelegate
import kotlinx.android.synthetic.main.fragment_main.*


/**
 * Created by t.coulange on 19/10/2016.
 */
class FragmentMain : Fragment(), OnParallaxScrollListener {
    override fun onParallaxScroll(percentage: Float, offset: Float, parallax: View?) {
        val c = toolbar.getBackground()
        if (c != null) {
            c.setAlpha(Math.round(percentage * 255))
            toolbar.setBackground(c)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = AdapterRecycler()
        recyclerView.adapter = adapter
        adapter.delegate.setParallaxHeader(R.layout.pheader, recyclerView, this)
    }

    class AdapterRecycler : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ParallaxRecyclerDelegate.RecyclerImpl {
        override fun onBindViewHolderImpl(viewHolder: RecyclerView.ViewHolder, adapter: ParallaxRecyclerDelegate, position: Int) {
            (viewHolder.itemView as TextView).text = "Item $position"
        }

        override fun onCreateViewHolderImpl(parent: ViewGroup, adapter: ParallaxRecyclerDelegate, i: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
            return CustomHolder(view)
        }

        override fun getItemCountImpl(adapter: ParallaxRecyclerDelegate): Int {
            return 30
        }

        val delegate: ParallaxRecyclerDelegate

        init {
            delegate = ParallaxRecyclerDelegate(this)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return delegate.onCreateViewHolder(parent, viewType)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            return delegate.onBindViewHolder(holder, position)
        }

        override fun getItemCount(): Int {
            return delegate.getItemCount()
        }

        override fun getItemViewType(position: Int): Int {
            return delegate.getItemViewType(position)
        }

        class CustomHolder(view: View) : RecyclerView.ViewHolder(view) {

        }

    }
}