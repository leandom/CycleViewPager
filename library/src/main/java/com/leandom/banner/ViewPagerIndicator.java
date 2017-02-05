package com.leandom.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

public class ViewPagerIndicator extends View {

    private Drawable mIndicatorNormal;
    private Drawable mIndicatorSelected;
    private int mCount;
    private int mCurrent;
    private float mIndicatorPadding = 10;
    private int mContentWidth;
    private int mGravity;
    private boolean mDisableShowOnlyOneCount = true;
    private ViewPager mViewPager;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mIndicatorPadding = typedArray.getDimension(R.styleable.ViewPagerIndicator_indicatorPadding, mIndicatorPadding);
        mIndicatorNormal = typedArray.getDrawable(R.styleable.ViewPagerIndicator_indicatorNormal);
        mIndicatorSelected = typedArray.getDrawable(R.styleable.ViewPagerIndicator_indicatorSelected);
        if (mIndicatorNormal == null) {
            mIndicatorNormal = getResources().getDrawable(R.drawable.indicator_normal_default);
        }
        if (mIndicatorSelected == null) {
            mIndicatorSelected = getResources().getDrawable(R.drawable.indicator_selected_default);
        }
        mGravity = typedArray.getInt(R.styleable.ViewPagerIndicator_android_gravity, Gravity.CENTER);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int itemWidth = mIndicatorNormal.getIntrinsicWidth();
        if (mCount == 0) {
            mContentWidth = 0;
        } else {
            mContentWidth = (int) (itemWidth * mCount + mIndicatorPadding * (mCount - 1));
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = mContentWidth + getPaddingLeft() + getPaddingRight();
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            int itemHeight = mIndicatorNormal.getIntrinsicHeight();
            heightSize = itemHeight + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int count = mCount;
        final int current = mCurrent;
        if (mDisableShowOnlyOneCount && mCount <= 1) {
            return;
        }
        int itemWidth = mIndicatorNormal.getIntrinsicWidth();
        int offsetX;
        if (mGravity == Gravity.LEFT) {
            offsetX = getPaddingLeft();
        } else if (mGravity == Gravity.RIGHT) {
            offsetX = getWidth() - (mContentWidth + getPaddingRight());
        } else {
            // center
            offsetX = (getWidth() - mContentWidth) / 2;
        }
        int startY = getPaddingTop();
        for (int i = 0; i < count; i++) {
            if (i == current) {
                setBounds(mIndicatorSelected, offsetX, startY);
                mIndicatorSelected.draw(canvas);
            } else {
                setBounds(mIndicatorNormal, offsetX, startY);
                mIndicatorNormal.draw(canvas);
            }
            offsetX += itemWidth + mIndicatorPadding;
        }
    }

    /**
     * Only support left, right, center. Default is center.
     *
     * @param gravity
     * @see android.view.Gravity
     */
    public void setHorizontalGravity(int gravity) {
        this.mGravity = gravity;
        invalidate();
    }

    public void disableShowOnlyOneCount(boolean disableShowOnlyOneCount) {
        this.mDisableShowOnlyOneCount = disableShowOnlyOneCount;
    }

    private static void setBounds(Drawable drawable, int startX, int startY) {
        int height = drawable.getIntrinsicHeight();
        int width = drawable.getIntrinsicWidth();
        drawable.setBounds(startX, startY, startX + width, startY + height);
    }

    public void bindToViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        if (viewPager instanceof CycleViewPager) {
            ((CycleViewPager) viewPager).addOnRealPageSelectedListener(mOnRealPageSelectedListener);
        } else {
            viewPager.addOnPageChangeListener(mOnPageChangeListener);
            mOnPageChangeListener.onPageSelected(viewPager.getCurrentItem());
        }
    }


    public void invalidate(int current, int count) {
        if (mCount != count) {
            requestLayout();
        }
        mCurrent = current;
        mCount = count;
        invalidate();
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            if (mViewPager.getAdapter() == null) {
                invalidate(0, 0);
            } else {
                int count = mViewPager.getAdapter().getCount();
                invalidate(position, count);
            }
        }
    };

    private CycleViewPager.OnRealPageSelectedListener mOnRealPageSelectedListener =
            new CycleViewPager.OnRealPageSelectedListener() {
                @Override
                public void onPageSelected(int current, int realCount) {
                    invalidate(current, realCount);
                }
            };
}
