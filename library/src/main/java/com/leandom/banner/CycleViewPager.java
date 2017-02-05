package com.leandom.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CycleViewPager extends ViewPager {

    // private static final String TAG = "AutoScrollViewPager";
    private int mAutoScrollInterval = 4000;
    private int mPageSwitchDuration = 800;
    private boolean mAutoScrollAble = false;
    private boolean mStopAutoScrollWhenTouch = true;
    private CyclePagerAdapter mCyclePagerAdapter;
    private ScrollOrientationHelper mScrollOrientationHelper;
    private List<OnRealPageSelectedListener> mOnRealPageSelectedListeners;

    public CycleViewPager(Context context) {
        this(context, null);
    }

    public CycleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScrollOrientationHelper = new ScrollOrientationHelper(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CycleViewPager);
        mAutoScrollInterval = typedArray.getInt(R.styleable.CycleViewPager_autoScrollInterval, mAutoScrollInterval);
        mPageSwitchDuration = typedArray.getInt(R.styleable.CycleViewPager_pageSwitchDuration, mPageSwitchDuration);
        typedArray.recycle();
        setPageSwitchDuration(mPageSwitchDuration);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof CyclePagerAdapter) {
            mCyclePagerAdapter = (CyclePagerAdapter) adapter;
            mCyclePagerAdapter.registerDataSetObserver(mDataSetObserver);
            addOnPageChangeListener(mOnPageChangeListener);
            setMiddleItemInner(false, true);
        }
    }

    public int setMiddleItem() {
        return setMiddleItemInner(true, true);
    }

    private int setMiddleItemInner(boolean setToFirstItem, boolean immediately) {
        if (mCyclePagerAdapter != null && mCyclePagerAdapter.getRealCount() > 1) {
            int currentItem = setToFirstItem ? 0 : getCurrentItem();
            final int middleItem = mCyclePagerAdapter.getCount() / 2 + mCyclePagerAdapter.getRealPosition(currentItem);
            if (immediately) {
                setCurrentItem(middleItem, false);
            } else {
                post(new Runnable() {
                    @Override
                    public void run() {
                        setCurrentItem(middleItem, false);
                    }
                });
            }
            return middleItem;
        }
        return getCurrentItem();
    }

    @Override
    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        throw new UnsupportedOperationException("There will be some problems when setCurrentItem method is called");
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        throw new UnsupportedOperationException("use addOnPageChangeListener instead");
    }

    private OnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float offset, int offsetPixels) {
            if (offset != 0) {
                return;
            }
            if (mCyclePagerAdapter == null || mCyclePagerAdapter.getRealCount() <= 1) {
                return;
            }
            if (position == 0) {
                setMiddleItemInner(false, false);
                // Log.d(TAG, "setFirstPage from first item");
            } else if (position == mCyclePagerAdapter.getCount() - 1) {
                setMiddleItemInner(false, false);
                // Log.d(TAG, "setFirstPage from last item");
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mCyclePagerAdapter == null) {
                return;
            }
            notifyPageSelectedListener(position, null);
        }

    };

    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            notifyPageSelectedListener(getCurrentItem(), null);
        }
    };

    /**
     * @param position
     * @param whichListener if null, notify all listener
     */
    private void notifyPageSelectedListener(int position, OnRealPageSelectedListener whichListener) {
        if (mCyclePagerAdapter == null) {
            return;
        }
        if (whichListener != null || mOnRealPageSelectedListeners != null) {
            int realPosition = mCyclePagerAdapter.getRealPosition(position);
            int realCount = mCyclePagerAdapter.getRealCount();
            if (whichListener != null) {
                whichListener.onPageSelected(realPosition, realCount);
            } else {
                for (OnRealPageSelectedListener listener : mOnRealPageSelectedListeners) {
                    listener.onPageSelected(realPosition, realCount);
                }
            }
        }
    }

    public void startAutoScroll() {
        mAutoScrollAble = true;
        startAutoScrollInner();
    }

    public void setPageSwitchDuration(int duration) {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            scrollerField.set(this, new MyScroller(getContext(), duration));
            mPageSwitchDuration = duration;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAutoScrollInterval(int autoScrollInterval) {
        if (autoScrollInterval > 0) {
            mAutoScrollInterval = autoScrollInterval;
        } else {
            mAutoScrollInterval = 4000;
        }
    }

    public void stopAutoScroll() {
        mAutoScrollAble = false;
        stopAutoScrollInner();
    }

    public void stopAutoScrollWhenTouch(boolean stopAutoScrollWhentTouch) {
        mStopAutoScrollWhenTouch = stopAutoScrollWhentTouch;
    }

    private void startAutoScrollInner() {
        removeCallbacks(mAutoScrollRunnable);
        postDelayed(mAutoScrollRunnable, mAutoScrollInterval);
    }

    private void stopAutoScrollInner() {
        removeCallbacks(mAutoScrollRunnable);
    }

    private Runnable mAutoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            removeCallbacks(this);
            if (!mAutoScrollAble || mCyclePagerAdapter == null || mCyclePagerAdapter.getRealCount() <= 1) {
                return;
            }
            scrollToNext();
            postDelayed(this, mAutoScrollInterval);
        }
    };

    private void scrollToNext() {
        int next = getCurrentItem() + 1;
        if (next == mCyclePagerAdapter.getCount()) {
            int current = setMiddleItemInner(false, false);
            next = current + 1;
        }
        setCurrentItem(next, true);
    }


    public void addOnRealPageSelectedListener(OnRealPageSelectedListener listener) {
        if (mOnRealPageSelectedListeners == null) {
            mOnRealPageSelectedListeners = new ArrayList<>();
        }
        mOnRealPageSelectedListeners.add(listener);
        notifyPageSelectedListener(getCurrentItem(), listener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mScrollOrientationHelper.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mStopAutoScrollWhenTouch) {
                    stopAutoScrollInner();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mStopAutoScrollWhenTouch && mAutoScrollAble) {
                    startAutoScrollInner();
                }
                break;
        }
        if (mScrollOrientationHelper.isScrollHorizontal()) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAutoScroll();
        removeOnPageChangeListener(mOnPageChangeListener);
        super.onDetachedFromWindow();
    }

    public static class MyScroller extends Scroller {
        private int mDuration = 800;

        public MyScroller(Context context, int duration) {
            super(context);
            this.mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }

    public interface OnRealPageSelectedListener {
        void onPageSelected(int current, int realCount);
    }
}
