package com.leandom.banner;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class ScrollOrientationHelper {

    private float downX;
    private float downY;
    private boolean scrollHorizontal = false;
    private boolean scrollVertical = false;
    private boolean determineOrientation = false;
    private float touchSlop;

    public ScrollOrientationHelper(Context context) {
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                scrollHorizontal = false;
                scrollVertical = false;
                determineOrientation = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(ev.getX() - downX);
                float dy = Math.abs(ev.getY() - downY);
                if (!determineOrientation) {
                    if (dx > dy && dx > touchSlop) {
                        scrollHorizontal = true;
                        determineOrientation = true;
                    } else if (dy > dx && dy > touchSlop) {
                        scrollVertical = true;
                        determineOrientation = true;
                    }
                }
                break;
        }
    }

    /**
     * @return true if scroll horizontal. If return false, scroll vertical or undetermined
     */
    public boolean isScrollHorizontal() {
        return scrollHorizontal;
    }

    /**
     * @return true if scroll vertical. If return false, scroll horizontal or undetermined
     */
    public boolean isScrollVertical() {
        return scrollVertical;
    }

}
