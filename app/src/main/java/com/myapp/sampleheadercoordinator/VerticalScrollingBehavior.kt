package com.myapp.sampleheadercoordinator

import android.content.Context
import android.os.Parcelable
import android.support.annotation.IntDef
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.WindowInsetsCompat
import android.util.AttributeSet
import android.view.View

abstract class VerticalScrollingBehavior<V : View> : CoordinatorLayout.Behavior<V> {

    private var mTotalDyUnconsumed = 0
    private var mTotalDy = 0
    /*
       @return Overscroll direction: SCROLL_DIRECTION_UP, CROLL_DIRECTION_DOWN, SCROLL_NONE
   */
    @ScrollDirection
    var overScrollDirection = NONE
        private set
    /**
     * @return Scroll direction: SCROLL_DIRECTION_UP, SCROLL_DIRECTION_DOWN, SCROLL_NONE
     */

    @ScrollDirection
    var scrollDirection = NONE
        private set

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor() : super() {
    }

    //    @Retention(RetentionPolicy.SOURCE)
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(DIRECTION_UP.toLong(), DIRECTION_DOWN.toLong())
    annotation class ScrollDirection

    companion object SCROLL {
        const val DIRECTION_UP = 1
        const val DIRECTION_DOWN = -1
        const val NONE = 0
    }


    /**
     * @param coordinatorLayout
     * *
     * @param child
     * *
     * @param direction         Direction of the overscroll: SCROLL_DIRECTION_UP, SCROLL_DIRECTION_DOWN
     * *
     * @param currentOverScroll Unconsumed value, negative or positive based on the direction;
     * *
     * @param totalOverScroll   Cumulative value for current direction
     */
    abstract fun onNestedVerticalOverScroll(coordinatorLayout: CoordinatorLayout, child: View, @ScrollDirection direction: Int, currentOverScroll: Int, totalOverScroll: Int)

    /**
     * @param scrollDirection Direction of the overscroll: SCROLL_DIRECTION_UP, SCROLL_DIRECTION_DOWN
     */
    abstract fun onDirectionNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, dx: Int, dy: Int, consumed: IntArray, @ScrollDirection scrollDirection: Int)

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout?, child: V?, directTargetChild: View?, target: View?, nestedScrollAxes: Int): Boolean {
        return nestedScrollAxes and View.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedScrollAccepted(coordinatorLayout: CoordinatorLayout?, child: V?, directTargetChild: View?, target: View?, nestedScrollAxes: Int) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes)
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout?, child: V?, target: View?) {
        super.onStopNestedScroll(coordinatorLayout, child, target)
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout?, child: V?, target: View?, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        if (dyUnconsumed > 0 && mTotalDyUnconsumed < 0) {
            mTotalDyUnconsumed = 0
            overScrollDirection = DIRECTION_UP
        } else if (dyUnconsumed < 0 && mTotalDyUnconsumed > 0) {
            mTotalDyUnconsumed = 0
            overScrollDirection = DIRECTION_DOWN
        }
        mTotalDyUnconsumed += dyUnconsumed
        onNestedVerticalOverScroll(coordinatorLayout!!, child!!, overScrollDirection, dyConsumed, mTotalDyUnconsumed)
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout?, child: V?, target: View?, dx: Int, dy: Int, consumed: IntArray?) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed)
        if (dy > 0 && mTotalDy < 0) {
            mTotalDy = 0
            scrollDirection = DIRECTION_UP
        } else if (dy < 0 && mTotalDy > 0) {
            mTotalDy = 0
            scrollDirection = DIRECTION_DOWN
        }
        mTotalDy += dy
        onDirectionNestedPreScroll(coordinatorLayout!!, child!!, target!!, dx, dy, consumed!!, scrollDirection)
    }


    override fun onNestedFling(coordinatorLayout: CoordinatorLayout?, child: V?, target: View?, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed)
        scrollDirection = if (velocityY > 0) DIRECTION_UP else DIRECTION_DOWN
        return onNestedDirectionFling(coordinatorLayout!!, child!!, target!!, velocityX, velocityY, scrollDirection)
    }

    protected abstract fun onNestedDirectionFling(coordinatorLayout: CoordinatorLayout, child: V, target: View, velocityX: Float, velocityY: Float, @ScrollDirection scrollDirection: Int): Boolean

    override fun onNestedPreFling(coordinatorLayout: CoordinatorLayout?, child: V?, target: View?, velocityX: Float, velocityY: Float): Boolean {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
    }

    override fun onApplyWindowInsets(coordinatorLayout: CoordinatorLayout?, child: V?, insets: WindowInsetsCompat): WindowInsetsCompat {

        return super.onApplyWindowInsets(coordinatorLayout, child, insets)
    }

    override fun onSaveInstanceState(parent: CoordinatorLayout?, child: V?): Parcelable {
        return super.onSaveInstanceState(parent, child)
    }

}