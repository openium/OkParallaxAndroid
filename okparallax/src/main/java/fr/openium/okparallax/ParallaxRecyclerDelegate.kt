package fr.openium.okparallax

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ParallaxRecyclerDelegate(val impl: RecyclerImpl) {
    object VIEW_TYPES {
        val NORMAL = 1
        val HEADER = 2
        val FIRST_VIEW = 3
    }

    interface RecyclerImpl {
        fun onBindViewHolderImpl(viewHolder: RecyclerView.ViewHolder, adapter: ParallaxRecyclerDelegate, i: Int)

        fun onCreateViewHolderImpl(viewGroup: ViewGroup, adapter: ParallaxRecyclerDelegate, i: Int): RecyclerView.ViewHolder

        fun getItemCountImpl(adapter: ParallaxRecyclerDelegate): Int
    }

    interface OnClickEvent {
        /**
         * Event triggered when you click on a item of the adapter

         * @param v        view
         * *
         * @param position position on the array
         */
        fun onClick(v: View, position: Int)
    }

    private var mHeader: ParallaxOverlayHeader? = null
    private var mOnClickEvent: OnClickEvent? = null
    private var mRecyclerView: RecyclerView? = null

    fun setParallaxHeader(@LayoutRes header: Int, view: RecyclerView, listener: OnParallaxScrollListener? = null) {
        mRecyclerView = view
        mHeader = LayoutInflater.from(mRecyclerView!!.context).inflate(header, null, false) as ParallaxOverlayHeader
        mHeader?.onParallaxScrollListener = listener
        mRecyclerView!!.addOnScrollListener(mHeader?.recyclerViewScrollListener)
    }

    fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        if (mHeader != null) {
            if (i == 0) {
                return
            }
            impl.onBindViewHolderImpl(viewHolder, this, i - 1)
        } else {
            impl.onBindViewHolderImpl(viewHolder, this, i)
        }
    }

    fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        if (i == VIEW_TYPES.HEADER && mHeader != null) {
            return ViewHolder(mHeader!!)
        }
        if (i == VIEW_TYPES.FIRST_VIEW && mHeader != null && mRecyclerView != null) {
            val holder = mRecyclerView!!.findViewHolderForAdapterPosition(0)
            if (holder != null) {
                mHeader?.translateHeader((-holder.itemView.top).toFloat())
            }
        }
        val holder = impl.onCreateViewHolderImpl(viewGroup, this, i)
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

    fun getItemCount(): Int {
        return impl.getItemCountImpl(this) + if (mHeader == null) 0 else 1
    }

    fun getItemViewType(position: Int): Int {
        if (position == 1)
            return VIEW_TYPES.FIRST_VIEW
        return if (position == 0 && mHeader != null) VIEW_TYPES.HEADER else VIEW_TYPES.NORMAL
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}