package fr.openium.okparallax

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ParallaxRecyclerDelegate(val impl: RecyclerImpl) {
    object VIEW_TYPES {
        val NORMAL = 0xaaa
        val HEADER = 0xaab
//        val FIRST_VIEW = 0xaac
    }

    var initHeader = false

    interface RecyclerImpl {
        fun onBindViewHolderImpl(viewHolder: RecyclerView.ViewHolder, position: Int)

        fun onCreateViewHolderImpl(viewGroup: ViewGroup, position: Int): RecyclerView.ViewHolder

        fun getItemCountImpl(): Int
        fun getItemViewTypeImpl(position: Int): Int {
            return VIEW_TYPES.NORMAL
        }
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

    fun setParallaxHeader(@LayoutRes headerL: Int, view: RecyclerView, listener: OnParallaxScrollListener? = null) {
        val header = LayoutInflater.from(view.context).inflate(headerL, null, false) as ParallaxOverlayHeader
        setParallaxHeader(header, view, listener)
    }

    fun setParallaxHeader(header: View, view: RecyclerView, listener: OnParallaxScrollListener? = null) {
        mRecyclerView = view
        mHeader = header as ParallaxOverlayHeader
        mHeader?.onParallaxScrollListener = listener
        mRecyclerView!!.addOnScrollListener(mHeader?.recyclerViewScrollListener)
    }


    fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (mHeader != null) {
            if (position == 0) {
                return
            }
            impl.onBindViewHolderImpl(viewHolder, position - 1)
        } else {
            impl.onBindViewHolderImpl(viewHolder, position)
        }
    }

    fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPES.HEADER && mHeader != null) {
            return ViewHolder(mHeader!!)
        }
        if (!initHeader && mHeader != null && mRecyclerView != null) {
            val holder = mRecyclerView!!.findViewHolderForAdapterPosition(0)
            if (holder != null) {
                mHeader?.translateHeader((-holder.itemView.top).toFloat())
            }
            initHeader = true
        }
        val holder = impl.onCreateViewHolderImpl(viewGroup, viewType)
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
        return impl.getItemCountImpl() + if (mHeader == null) 0 else 1
    }

    fun getItemViewType(position: Int): Int {
        return if (mHeader != null) {
            if (position == 0) {
                VIEW_TYPES.HEADER
            } else {
                impl.getItemViewTypeImpl(position - 1)
            }
        } else {
            impl.getItemViewTypeImpl(position)
        }
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}