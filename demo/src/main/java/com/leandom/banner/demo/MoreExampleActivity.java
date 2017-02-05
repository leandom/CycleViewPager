package com.leandom.banner.demo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.leandom.banner.CycleViewPager;
import com.leandom.banner.ViewPagerIndicator;

public class MoreExampleActivity extends AppCompatActivity {

    private ViewPager viewPager1;
    private CycleViewPager cycleViewPager2;
    private CycleViewPager cycleViewPager3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_esample);

        // ViewPagerIndicator can be used with Viewpager
        viewPager1 = (ViewPager) findViewById(R.id.viewpager1);
        viewPager1.setAdapter(new BannerAdapter(this, 6, false));
        ViewPagerIndicator indicator1 = (ViewPagerIndicator) findViewById(R.id.indicator1);
        indicator1.bindToViewPager(viewPager1);

        // cycleViewPager2
        cycleViewPager2 = (CycleViewPager) findViewById(R.id.viewpager2);
        cycleViewPager2.setAdapter(new BannerAdapter(this, 4, true));
        ViewPagerIndicator indicator2 = (ViewPagerIndicator) findViewById(R.id.indicator2);
        indicator2.bindToViewPager(cycleViewPager2);

        // cycleViewPager3
        cycleViewPager3 = (CycleViewPager) findViewById(R.id.viewpager3);
        cycleViewPager3.setAdapter(new BannerAdapter(this, 6, true));
        // cycleViewPager3.stopAutoScrollWhenTouch(false);
        ViewPagerIndicator indicator3 = (ViewPagerIndicator) findViewById(R.id.indicator3);
        indicator3.bindToViewPager(cycleViewPager3);

    }

    @Override
    protected void onStart() {
        super.onStart();
        cycleViewPager3.startAutoScroll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cycleViewPager3.stopAutoScroll();
    }
}
