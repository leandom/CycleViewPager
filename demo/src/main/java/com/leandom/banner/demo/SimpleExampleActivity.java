package com.leandom.banner.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.leandom.banner.CyclePagerAdapter;
import com.leandom.banner.CycleViewPager;
import com.leandom.banner.ViewPagerIndicator;

public class SimpleExampleActivity extends AppCompatActivity {

    private CycleViewPager mCyclellViewPager;
    private ViewPagerIndicator mIndicator;
    private SimpleBannerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_example);
        mCyclellViewPager = (CycleViewPager) findViewById(R.id.banner);
        mIndicator = (ViewPagerIndicator) findViewById(R.id.viewpager_indicator);
        mPagerAdapter = new SimpleBannerAdapter(this);
        mCyclellViewPager.setAdapter(mPagerAdapter);
        mIndicator.bindToViewPager(mCyclellViewPager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mCyclellViewPager.startAutoScroll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCyclellViewPager.stopAutoScroll();
    }

    public void workInListView(View view) {
        startActivity(new Intent(this, WorkInListViewActivity.class));
    }

    public void seeMoreExamples(View view) {
        startActivity(new Intent(this, MoreExampleActivity.class));
    }

    static class SimpleBannerAdapter extends CyclePagerAdapter {

        private static final int[] drawableIds = new int[]{R.drawable.desert, R.drawable.koala,
                R.drawable.jellyfish, R.drawable.hydrangeas};
        private Context mContext;

        public SimpleBannerAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getRealCount() {
            return drawableIds.length;
        }

        @Override
        public View getViewAtRealPosition(final int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.banner_item, container, false);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imageView.setImageResource(drawableIds[position % drawableIds.length]);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "click at " + position, Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }
    }

}
