package com.myapp.sampleheadercoordinator

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

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

    private var mHeader: ParallaxWrapper? = null
    private var frameLayout: FrameLayout? = null
    private var mOnClickEvent: OnClickEvent? = null
    private var mParallaxScroll: OnParallaxScroll? = null
    private var mRecyclerView: RecyclerView? = null
    /**
     * Defines if we will clip the layout or not. MUST BE CALLED BEFORE [ ][.setParallaxHeader]
     */
    var isShouldClipView = true

    /**
     * Translates the adapter in Y

     * @param of offset in px
     */
    fun translateHeader(of: Float) {
        Log.d("custom", "of $of")
        val ofCalculated = of * scrollMultiplier
        if (of < frameLayout!!.height) {
            mHeader!!.translationY = ofCalculated
        }
        mHeader!!.setClipY(Math.round(ofCalculated))
        if (mParallaxScroll != null) {
            val holder = mRecyclerView!!.findViewHolderForAdapterPosition(0)
            val left: Float
            if (holder != null) {
                left = Math.min(1f, ofCalculated / (mHeader!!.height * scrollMultiplier))
            } else {
                left = 1f
            }
            mParallaxScroll!!.onParallaxScroll(left, of, mHeader)
        }
    }

    /**
     * Set the view as header.

     * @param header The inflated header
     * *
     * @param view   The RecyclerView to set scroll listeners
     */
    fun setParallaxHeader(@LayoutRes header: Int, view: RecyclerView, @LayoutRes overlay: Int = 0) {
        mRecyclerView = view

        frameLayout = FrameLayout(mRecyclerView!!.context)
        mHeader = ParallaxWrapper(mRecyclerView!!.context, isShouldClipView, header)
        mHeader!!.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        frameLayout!!.addView(mHeader)
        if (overlay != 0) {
            val overlayL = LayoutInflater.from(frameLayout!!.context).inflate(R.layout.overlay, frameLayout!!, true)
        }
        frameLayout!!.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        view.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (frameLayout != null) {
                    translateHeader((if (mRecyclerView!!.layoutManager.getChildAt(0) === frameLayout)
                        mRecyclerView!!.computeVerticalScrollOffset()
                    else
                        mHeader!!.height).toFloat())
                }
            }
        })
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
        if (i == VIEW_TYPES.HEADER && frameLayout != null) {
            return ViewHolder(frameLayout!!)
        }
        if (i == VIEW_TYPES.FIRST_VIEW && frameLayout != null && mRecyclerView != null) {
            val holder = mRecyclerView!!.findViewHolderForAdapterPosition(0)
            if (holder != null) {
                translateHeader((-holder.itemView.top).toFloat())
            }
        }
        val holder = onCreateViewHolderImpl(viewGroup, this, i)
        if (mOnClickEvent != null) {
            holder.itemView.setOnClickListener { v -> mOnClickEvent!!.onClick(v, holder.adapterPosition - if (frameLayout == null) 0 else 1) }
        }
        return holder
    }

    /**
     * @return true if there is a header on this adapter, false otherwise
     */
    fun hasHeader(): Boolean {
        return frameLayout != null
    }

    fun setOnClickEvent(onClickEvent: OnClickEvent) {
        mOnClickEvent = onClickEvent
    }

    fun setOnParallaxScroll(parallaxScroll: OnParallaxScroll) {
        mParallaxScroll = parallaxScroll
        mParallaxScroll!!.onParallaxScroll(0f, 0f, frameLayout)
    }

    var data: MutableList<T>
        get() = mData
        set(data) {
            mData = data
            notifyDataSetChanged()
        }

    fun addItem(item: T, position: Int) {
        mData.add(position, item)
        notifyItemInserted(position + if (frameLayout == null) 0 else 1)
    }

    fun removeItem(item: T) {
        val position = mData!!.indexOf(item)
        if (position < 0)
            return
        mData.remove(item)
        notifyItemRemoved(position + if (frameLayout == null) 0 else 1)
    }


    override fun getItemCount(): Int {
        return getItemCountImpl(this) + if (frameLayout == null) 0 else 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 1)
            return VIEW_TYPES.FIRST_VIEW
        return if (position == 0 && frameLayout != null) VIEW_TYPES.HEADER else VIEW_TYPES.NORMAL
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}