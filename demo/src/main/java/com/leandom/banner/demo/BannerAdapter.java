package com.leandom.banner.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.leandom.banner.CyclePagerAdapter;

class BannerAdapter extends CyclePagerAdapter {

    private static final int[] drawableIds = new int[]{R.drawable.desert, R.drawable.koala,
            R.drawable.jellyfish, R.drawable.hydrangeas};
    private Context mContext;
    private int mCount;
    private boolean mInfinite;

    public BannerAdapter(Context context, int count, boolean infinite) {
        this.mContext = context;
        this.mCount = count;
        this.mInfinite = infinite;
    }

    public void setCount(int count) {
        this.mCount = count;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mInfinite) {
            return super.getCount();
        } else {
            return mCount;
        }
    }

    @Override
    public int getRealCount() {
        return mCount;
    }

    @Override
    public View getViewAtRealPosition(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.banner_item, container, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        imageView.setImageResource(drawableIds[position % drawableIds.length]);
        return convertView;
    }
}
