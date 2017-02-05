package com.leandom.banner;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

public abstract class CyclePagerAdapter extends PagerAdapter {

    private final int MAX_PAGES = 200;
    protected LinkedList<View> mScrapViews = new LinkedList<View>();
    protected int mMaxScrapViewSize = 2;

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View scrap = retrieveFromScrap();
        View view = getView(position, scrap, container);
        container.addView(view);
        return view;
    }

    private View retrieveFromScrap() {
        if (mScrapViews.size() > 0) {
            return mScrapViews.removeLast();
        }
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
        if (mScrapViews.size() < mMaxScrapViewSize) {
            mScrapViews.add(view);
        }
    }

    @Override
    public int getCount() {
        if (getRealCount() < 2) {
            return getRealCount();
        }
        return getRealCount() * (MAX_PAGES / getRealCount());
    }

    public View getView(int position, View convertView, ViewGroup container) {
        int realPosition = getRealPosition(position);
        return getViewAtRealPosition(realPosition, convertView, container);
    }

    @Override
    public final boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public int getRealPosition(int position) {
        if (getRealCount() == 0) {
            return 0;
        }
        return position % getRealCount();
    }

    public abstract int getRealCount();

    public abstract View getViewAtRealPosition(int position, View convertView, ViewGroup container);


}