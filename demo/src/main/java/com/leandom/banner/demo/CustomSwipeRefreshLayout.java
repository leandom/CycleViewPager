package com.leandom.banner.demo;


import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.leandom.banner.ScrollOrientationHelper;

public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {

    private ScrollOrientationHelper scrollOrientationHelper;

    public CustomSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scrollOrientationHelper = new ScrollOrientationHelper(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        scrollOrientationHelper.onTouchEvent(ev);
        if (scrollOrientationHelper.isScrollHorizontal()) {
            setEnabled(false);
        } else {
            setEnabled(true);
        }
        return super.onInterceptTouchEvent(ev);
    }
}
