package com.ruomiz.coordinatorheader.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ruomiz.coordinatorheader.R;
import com.ruomiz.coordinatorheader.SearchBar;

/**
 * Ruomiz on 2018/6/13.
 * Time  waits  for  none
 * desc ： 自定义滚动的beheaver
 */

public class MarketScrollingBehavior extends CoordinatorLayout.Behavior<SearchBar> {

    public MarketScrollingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, SearchBar child, View dependency) {
        return isDepent(dependency);
    }

    private boolean isDepent(View dependency) {
        return dependency != null && dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, SearchBar child, View dependency) {
        float y = dependency.getY();

        int totalScrollRange = ((AppBarLayout) dependency).getTotalScrollRange();
        if (Math.abs(y) < totalScrollRange * 0.6) {
            if (child.getCurrentState() == SearchBar.State.OPEN) {     //当searchBar处于展开状态
                child.setTranslationY(y);
                Log.e("Ruomiz", "TranslationY===" + y);
            } else if (child.getCurrentState() == SearchBar.State.EXPANDING){ //当searchBar展开和收缩的过程中
                child.setTranslationY(y);
                Log.d("Ruomiz", "TranslationY===" + y + "---state--" + child.getCurrentState());
            }
        }
        return true;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull SearchBar child,
                                  @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }
}
