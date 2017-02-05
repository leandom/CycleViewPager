package com.leandom.banner.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.leandom.banner.CycleViewPager;
import com.leandom.banner.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

public class WorkInListViewActivity extends AppCompatActivity {

    private static final String TAG = "WorkInListViewActivity";
    private CycleViewPager mAutoScrollViewPager;
    private BannerAdapter mAutoScrollPagerAdapter;
    private CustomSwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private int mCount = 2;
    private boolean mDesc = false;
    private Handler mHandler = new Handler();
    private ViewPagerIndicator mIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        mAutoScrollPagerAdapter = new BannerAdapter(this, 3, true);
        mListView = (ListView) findViewById(R.id.listview);
        mSwipeRefreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.swiperefresh_layout);
        // AutoScrollViewPager header
        View headerView = LayoutInflater.from(this).inflate(R.layout.list_header, mListView, false);
        mAutoScrollViewPager = (CycleViewPager) headerView.findViewById(R.id.banner);
        mIndicator = (ViewPagerIndicator) headerView.findViewById(R.id.viewpager_indicator);
        mListView.addHeaderView(headerView);
        // mAutoScrollViewPager.setPageSwitchDuration(800);
        // mAutoScrollViewPager.setAutoScrollInterval(4000);
        mListView.setAdapter(new ArrayAdapter<String>(WorkInListViewActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, getData()));
        mAutoScrollViewPager.setAdapter(mAutoScrollPagerAdapter);
        mIndicator.bindToViewPager(mAutoScrollViewPager);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                WorkInListViewActivity.this.onRefresh();
            }
        });
    }

    // test refresh
    private void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mDesc) {
                    mCount--;
                } else {
                    mCount++;
                }
                if (mCount == 5) {
                    mDesc = true;
                } else if (mCount == 1) {
                    mDesc = false;
                }
                mAutoScrollPagerAdapter.setCount(mCount);
                mAutoScrollViewPager.setMiddleItem();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);

    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAutoScrollViewPager.startAutoScroll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAutoScrollViewPager.stopAutoScroll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }


}
