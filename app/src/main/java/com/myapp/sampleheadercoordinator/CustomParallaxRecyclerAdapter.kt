package com.myapp.sampleheadercoordinator

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class CustomParallaxRecyclerAdapter<T>(private var mData: MutableList<T>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     * Get the current parallax scroll multiplier.
     */
    /**
     * Set parallax scroll multiplier.

     * @param mul The multiplier
     */
    var scrollMultiplier = 0.5f

    object VIEW_TYPES {
        val NORMAL = 1
        val HEADER = 2
        val FIRST_VIEW = 3
    }

    abstract fun onBindViewHolderImpl(viewHolder: RecyclerView.ViewHolder, adapter: CustomParallaxRecyclerAdapter<T>, i: Int)

    abstract fun onCreateViewHolderImpl(viewGroup: ViewGroup, adapter: CustomParallaxRecyclerAdapter<T>, i: Int): RecyclerView.ViewHolder

    abstract fun getItemCountImpl(adapter: CustomParallaxRecyclerAdapter<T>): Int

    interface OnClickEvent {
        /**
         * Event triggered when you click on a item of the adapter

         * @param v        view
         * *
         * @param position position on the array
         */
        fun onClick(v: View, position: Int)
    }

    interface OnParallaxScroll {
        /**
         * Event triggered when the parallax is being scrolled.
         */
        fun onParallaxScroll(percentage: Float, offset: Float, parallax: View?)
    }

    private var mHeader: ParallaxOverlayHeader? = null
    private var mOnClickEvent: OnClickEvent? = null
    private var mParallaxScroll: OnParallaxScroll? = null
    private var mRecyclerView: RecyclerView? = null
    /**
     * Defines if we will clip the layout or not. MUST BE CALLED BEFORE [ ][.setParallaxHeader]
     */
    var isShouldClipView = true

    /**
     * Set the view as header.

     * @param header The inflated header
     * *
     * @param view   The RecyclerView to set scroll listeners
     */
    fun setParallaxHeader(@LayoutRes header: Int, view: RecyclerView) {
        mRecyclerView = view
        mHeader = LayoutInflater.from(mRecyclerView!!.context).inflate(header, null, false) as ParallaxOverlayHeader
        mRecyclerView!!.addOnScrollListener(mHeader?.recyclerViewScrollListener)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        if (mHeader != null) {
            if (i == 0) {
                return
            }
            onBindViewHolderImpl(viewHolder, this, i - 1)
        } else {
            onBindViewHolderImpl(viewHolder, this, i)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        if (i == VIEW_TYPES.HEADER && mHeader != null) {
            return ViewHolder(mHeader!!)
        }
        if (i == VIEW_TYPES.FIRST_VIEW && mHeader != null && mRecyclerView != null) {
            val holder = mRecyclerView!!.findViewHolderForAdapterPosition(0)
            if (holder != null) {
                mHeader?.translateHeader((-holder.itemView.top).toFloat())
            }
        }
        val holder = onCreateViewHolderImpl(viewGroup, this, i)
        if (mOnClickEvent != null) {
            holder.itemView.setOnClickListener { v -> mOnClickEvent!!.onClick(v, holder.adapterPosition - if (mHeader == null) 0 else 1) }
        }
        return holder
    }

    /**
     * @return true if there is a header on this adapter, false otherwise
     */
    fun hasHeader(): Boolean {
        return mHeader != null
    }

    fun setOnClickEvent(onClickEvent: OnClickEvent) {
        mOnClickEvent = onClickEvent
    }

    fun setOnParallaxScroll(parallaxScroll: OnParallaxScroll) {
        mHeader?.mParallaxScroll = parallaxScroll
        mHeader?.mParallaxScroll!!.onParallaxScroll(0f, 0f, mHeader)
    }

    var data: MutableList<T>
        get() = mData
        set(data) {
            mData = data
            notifyDataSetChanged()
        }

    fun addItem(item: T, position: Int) {
        mData.add(position, item)
        notifyItemInserted(position + if (mHeader == null) 0 else 1)
    }

    fun removeItem(item: T) {
        val position = mData.indexOf(item)
        if (position < 0)
            return
        mData.remove(item)
        notifyItemRemoved(position + if (mHeader == null) 0 else 1)
    }


    override fun getItemCount(): Int {
        return getItemCountImpl(this) + if (mHeader == null) 0 else 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 1)
            return VIEW_TYPES.FIRST_VIEW
        return if (position == 0 && mHeader != null) VIEW_TYPES.HEADER else VIEW_TYPES.NORMAL
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}