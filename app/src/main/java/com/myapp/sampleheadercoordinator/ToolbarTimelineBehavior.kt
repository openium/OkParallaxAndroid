package com.myapp.sampleheadercoordinator

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorCompat
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.View

open class ToolbarTimelineBehavior(context: Context, attrs: AttributeSet) : VerticalScrollingBehavior<Toolbar>(context, attrs) {
    protected var hidden = false
    private var translationAnimator: ViewPropertyAnimatorCompat? = null
    private var translationObjectAnimator: ObjectAnimator? = null
    protected var isBehaviorTranslationEnabled = true
    private var block = false
//    private var currentColor: Int
//    private val STARTING_SCRIM_OFFSET: Int

    private var yDistance = 0
    private var headerSize = 0

    var isScrimEnabled = true


    init {
//        currentColor = ColorUtils.setAlphaComponent(context.getColorCompat(R.color.red_darker), 0)
//        STARTING_SCRIM_OFFSET = context.dip(80.toFloat()).toInt()
//        headerSize = context.resources.getDimensionPixelSize(R.dimen.header_size)
    }

//    fun shouldScrim(should: Boolean, lockAlpha: Int = Color.alpha(currentColor)) {
//        isScrimEnabled = should
//        currentColor = ColorUtils.setAlphaComponent(currentColor, lockAlpha)
//    }

    override fun blocksInteractionBelow(parent: CoordinatorLayout?, child: Toolbar): Boolean {
        return block || super.blocksInteractionBelow(parent, child)
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: Toolbar, layoutDirection: Int): Boolean {
        val layoutChild = super.onLayoutChild(parent, child, layoutDirection)
        return layoutChild
    }

//    override fun onDependentViewChanged(parent: CoordinatorLayout, child: Toolbar, dependency: View): Boolean {
//        refreshColor(dependency, child)
//        return super.onDependentViewChanged(parent, child, dependency)
//    }

//    fun refreshColor(view: View, child: Toolbar) {
//        if (isScrimEnabled) {
////            val value = MathUtils.ensureRange((view.y + headerSize).toInt(), 0, STARTING_SCRIM_OFFSET)
////            val alpha = (255 - value.toFloat() / STARTING_SCRIM_OFFSET * 255).toInt()
////        Timber.d("alpha ${alpha} value ${value} y ${y} height ${height}")
////            currentColor = ColorUtils.setAlphaComponent(currentColor, alpha)
//            child.setBackgroundColor(currentColor)
//        } else {
//            child.setBackgroundColor(currentColor)
//        }
//    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout, child: Toolbar, dependency: View) {
        super.onDependentViewRemoved(parent, child, dependency)
    }

//    override fun layoutDependsOn(parent: CoordinatorLayout, child: Toolbar, dependency: View): Boolean {
//        return dependency.id == R.id.headerLine
//    }

    override fun onNestedVerticalOverScroll(coordinatorLayout: CoordinatorLayout, child: View, direction: Int, currentOverScroll: Int, totalOverScroll: Int) {
    }

//    override fun onNestedVerticalOverScroll(coordinatorLayout: CoordinatorLayout, child: V, @ScrollDirection direction: Int, currentOverScroll: Int, totalOverScroll: Int) {
//    }

    override fun onDirectionNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: Toolbar, target: View, dx: Int, dy: Int, consumed: IntArray, @ScrollDirection scrollDirection: Int) {
    }

    override fun onNestedPreFling(coordinatorLayout: CoordinatorLayout?, child: Toolbar?, target: View?, velocityX: Float, velocityY: Float): Boolean {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
    }

    override fun onNestedDirectionFling(coordinatorLayout: CoordinatorLayout, child: Toolbar, target: View, velocityX: Float, velocityY: Float, @ScrollDirection scrollDirection: Int): Boolean {
//        Timber.d("fling $velocityX $velocityY $scrollDirection")
        if (!(target is SwipeRefreshLayout) || target.canChildScrollUp()) {
            handleDirection(child, scrollDirection)
        }
        return true
    }

    override fun onNestedScrollAccepted(coordinatorLayout: CoordinatorLayout?, child: Toolbar?, directTargetChild: View?, target: View?, nestedScrollAxes: Int) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes)
        yDistance = 0
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout?, child: Toolbar?, target: View?) {
        super.onStopNestedScroll(coordinatorLayout, child, target)
        yDistance = 0
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout?, child: Toolbar?, target: View?, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
//        Timber.d("dyConsumed $dyConsumed dyUnconsumed $dyUnconsumed")
        yDistance += dyConsumed
        if (yDistance < -25) {
            handleDirection(child!!, DIRECTION_DOWN)
        } else if (dyConsumed > 25) {
            handleDirection(child!!, DIRECTION_UP)
        }
    }

    override fun onNestedFling(coordinatorLayout: CoordinatorLayout?, child: Toolbar?, target: View?, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
//        Timber.d("onNestedFling")
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed)
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout?, child: Toolbar?, directTargetChild: View?, target: View?, nestedScrollAxes: Int): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes)
    }

    /**
     * Handle scroll direction
     * @param child
     * *
     * @param scrollDirection
     */
    protected open fun handleDirection(child: Toolbar, scrollDirection: Int) {
        if (!isBehaviorTranslationEnabled) {
            return
        }
        if (scrollDirection == DIRECTION_DOWN && hidden) {
            yDistance = 0
            hidden = false
            animateOffset(child, 0f, false, true)
        } else if (scrollDirection == DIRECTION_UP && !hidden) {
            yDistance = 0
            hidden = true
            animateOffset(child, -child.height.toFloat(), false, true)
        }
    }

    /**
     * Animate offset

     * @param child
     * *
     * @param offset
     */
    protected fun animateOffset(child: Toolbar, offset: Float, forceAnimation: Boolean, withAnimation: Boolean) {
        if (!isBehaviorTranslationEnabled && !forceAnimation) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            ensureOrCancelObjectAnimation(child, offset, withAnimation)
            translationObjectAnimator!!.start()
        } else {
            ensureOrCancelAnimator(child, withAnimation)
            translationAnimator!!.translationY(offset.toFloat()).start()
        }
    }

    /**
     * Manage animation for Android >= KITKAT

     * @param child
     */
    private fun ensureOrCancelAnimator(child: View, withAnimation: Boolean) {
        if (translationAnimator == null) {
            translationAnimator = ViewCompat.animate(child)
            translationAnimator!!.duration = (if (withAnimation) ANIM_DURATION else 0).toLong()
            translationAnimator!!.interpolator = INTERPOLATOR
        } else {
            translationAnimator!!.duration = (if (withAnimation) ANIM_DURATION else 0).toLong()
            translationAnimator!!.cancel()
        }
    }

    /**
     * Manage animation for Android < KITKAT

     * @param child
     */
    private fun ensureOrCancelObjectAnimation(child: View, offset: Float, withAnimation: Boolean) {
        if (translationObjectAnimator != null) {
            translationObjectAnimator!!.cancel()
        }

        translationObjectAnimator = ObjectAnimator.ofFloat<View>(child, View.TRANSLATION_Y, offset)
        translationObjectAnimator!!.duration = (if (withAnimation) ANIM_DURATION else 0).toLong()
        translationObjectAnimator!!.interpolator = INTERPOLATOR
    }

    /**
     * Enable or not the behavior translation
     * @param behaviorTranslationEnabled
     */
    fun setIsBehaviorTranslationEnabled(behaviorTranslationEnabled: Boolean) {
        this.isBehaviorTranslationEnabled = behaviorTranslationEnabled
        block = !behaviorTranslationEnabled
    }
//
//    /**
//     * Set OnNavigationPositionListener
//     */
//    fun setOnNavigationPositionListener(navigationHeightListener: OnNavigationPositionListener) {
//        this.navigationPositionListener = navigationHeightListener
//    }
//
//    /**
//     * Remove OnNavigationPositionListener()
//     */
//    fun removeOnNavigationPositionListener() {
//        this.navigationPositionListener = null
//    }

    /**
     * Hide AHBottomNavigation with animation
     * @param view
     * *
     * @param offset
     */
    fun hideView(view: Toolbar, offset: Float, withAnimation: Boolean) {
        if (!hidden) {
            hidden = true
            animateOffset(view, offset, true, withAnimation)
        }
    }

    /**
     * Reset AHBottomNavigation position with animation
     * @param view
     */
    fun resetOffset(view: Toolbar, withAnimation: Boolean) {
        if (hidden) {
            hidden = false
            animateOffset(view, 0f, true, withAnimation)
        }
    }

    companion object {

        private val INTERPOLATOR = LinearOutSlowInInterpolator()
        private const val ANIM_DURATION = 300


        fun <V : View> from(view: V): ToolbarTimelineBehavior {
            val params = view.layoutParams
            if (params !is CoordinatorLayout.LayoutParams) {
                throw IllegalArgumentException("The view is not a child of CoordinatorLayout")
            }
            val behavior = params.behavior
            if (behavior !is ToolbarTimelineBehavior) {
                throw IllegalArgumentException(
                        "The view is not associated with AHBottomNavigationBehavior")
            }
            return behavior
        }
    }
}