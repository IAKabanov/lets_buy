package net.sytes.kai_soft.letsbuyka

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class FABHideOnScroll(context: Context, attrs: AttributeSet): FloatingActionButton.Behavior() {
    private var isAnimationRun = false

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        /*  Здесь запускается анимация  */
        if (!isAnimationRun) {
            animate(child, dyConsumed)
            isAnimationRun = true
        }
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, type: Int) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        isAnimationRun = false
    }

    private fun animate(child: FloatingActionButton, dyConsumed: Int){
        if (dyConsumed > 0){
            val layoutParams = child.layoutParams as (CoordinatorLayout.LayoutParams)
            val fabBottomMargin = layoutParams.bottomMargin
            child.animate().translationY((child.height + fabBottomMargin).toFloat()).setInterpolator(
                    LinearInterpolator()).start()
        }
        else if (dyConsumed < 0){
            child.animate().translationY(0f).setInterpolator(LinearInterpolator()).start()
        }
    }
}