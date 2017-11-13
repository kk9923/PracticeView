package com.xk.admin.day4;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Email 240336124@qq.com
 * Created by Darren on 2017/5/21.
 * Version 1.0
 * Description:
 */
public class ViewPagerActivity extends AppCompatActivity {
    private String[] items = {"企业控制台","监 控","报 表","历史纪录"};
    private LinearLayout mIndicatorContainer;// 变成通用的
    private List<ColorTrackTextView> mIndicators;
    private ViewPager mViewPager;
    private TabView tabView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        tabView = (TabView) findViewById(R.id.tabview);
        mIndicators = new ArrayList<>();
        mIndicatorContainer = (LinearLayout) findViewById(R.id.indicator_view);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        initIndicator();
        initViewPager();
        tabView.setonTabClickListener(new TabView.onTabClickListener() {
            @Override
            public void onTabClick(int position) {
                mViewPager.setCurrentItem(position);
            }
        });
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ItemFragment.newInstance(items[position]);
            }

            @Override
            public int getCount() {
                return items.length;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {

            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               // tabView.setCurrentSelectPosition(position);
//                ColorTrackTextView left = mIndicators.get(position);
//                left.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
//                left.setCurrentProgress(1-positionOffset);
//                try {
//                    ColorTrackTextView right = mIndicators.get(position + 1);
//                    right.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
//                    right.setCurrentProgress(positionOffset);
//                }catch (Exception e){
//
//                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化可变色的指示器
     */
    private void initIndicator() {
        for (int i = 0; i < items.length; i++) {
            // 动态添加颜色跟踪的TextView
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            ColorTrackTextView colorTrackTextView = new ColorTrackTextView(this);
            // 设置颜色
            colorTrackTextView.setTextSize(25);
            colorTrackTextView.setChangeColor(Color.RED);
            colorTrackTextView.setText(items[i]);
            colorTrackTextView.setLayoutParams(params);
            // 把新的加入LinearLayout容器
            mIndicatorContainer.addView(colorTrackTextView);
            // 加入集合
            mIndicators.add(colorTrackTextView);
        }
    }
}
