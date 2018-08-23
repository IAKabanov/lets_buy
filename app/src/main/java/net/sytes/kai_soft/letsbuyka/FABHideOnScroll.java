package net.sytes.kai_soft.letsbuyka;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 * Created by Лунтя on 16.06.2018.
 */


/*  Этот класс отвечает за анимацию floating action button  */
public class FABHideOnScroll extends FloatingActionButton.Behavior {

    private boolean isAnimationRun = false; //Это нужно для того, чтобы анимация не тормозила
                                            // и запускалась один раз


    public FABHideOnScroll(Context context, AttributeSet attrs) {
        super();
    }


    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull FloatingActionButton child,
                               @NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, type);

        /*  Здесь запускается анимация  */
        if (isAnimationRun == false) {
            animate(child, dyConsumed);
            isAnimationRun = true;
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull FloatingActionButton child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target, int nestedScrollAxes, int type) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                   @NonNull FloatingActionButton child, @NonNull View target,
                                   int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
        isAnimationRun = false;
    }

    /*  Логика анимации */
    private void animate(FloatingActionButton child, int dyConsumed){
        if (dyConsumed > 0) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                    child.getLayoutParams();
            int fab_bottomMargin = layoutParams.bottomMargin;
            child.animate().translationY(child.getHeight() + fab_bottomMargin)
                    .setInterpolator(new LinearInterpolator()).start();
        } else if (dyConsumed < 0) {
            child.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
        }
    }

}

