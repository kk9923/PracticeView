package com.kx.admin.day19.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.kx.admin.day19.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 1 绑定资源文件id，创建Fragment,给ViewPager设置Adapter,
 * 2 Fragment中  在View创建的时候 我们去解析属性 ，如果xml布局中由我们定义的动画id，则给此View设置一个tag对象，用于保存动画id对应的值，并将其添加至集合中保存，
 * 3 监听ViewPager的页面切换，在回掉中分别得到相应position位置上Fragment中保存的有动画id的View的集合，便利该集合，得到每个可移动的View和对应的tag对象，给每个View设置位移变化即可。
 */
public class ParallaxViewPager extends ViewPager {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    public ParallaxViewPager(Context context) {
        this(context,null);
    }

    public ParallaxViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 获取左out 右 in
                ParallaxFragment outFragment = (ParallaxFragment) fragments.get(position);
                List<View> parallaxViews = outFragment.getParallaxViews();
                for (View parallaxView : parallaxViews) {
                    ParallaxTag tag = (ParallaxTag) parallaxView.getTag(R.id.parallax_tag);
                    // 为什么这样写 ？
                    parallaxView.setTranslationX((-positionOffsetPixels)*tag.getTranslationXOut());
                    parallaxView.setTranslationY((-positionOffsetPixels)*tag.getTranslationYOut());
                }

                try {
                    ParallaxFragment inFragment = (ParallaxFragment) fragments.get(position+1);
                    parallaxViews = inFragment.getParallaxViews();
                    for (View parallaxView : parallaxViews) {
                        ParallaxTag tag = (ParallaxTag) parallaxView.getTag(R.id.parallax_tag);
                        parallaxView.setTranslationX((getMeasuredWidth()-positionOffsetPixels)*tag.getTranslationXIn());
                        parallaxView.setTranslationY((getMeasuredWidth()-positionOffsetPixels)*tag.getTranslationYIn());
                    }
                }catch (Exception e){}

            }
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setLayoutIds(FragmentManager fm, int[] layOutIds) {
        fragments.clear();
        for (int layOutId : layOutIds) {
            ParallaxFragment parallaxFragment = new ParallaxFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ParallaxFragment.LAYOUT_ID_KEY,layOutId);
            parallaxFragment.setArguments(bundle);
            fragments.add(parallaxFragment);
        }
        // 设置我们的 ViewPager 的Adapter
        setAdapter(new ParallaxAdapter(fm));
    }
    private  class ParallaxAdapter extends FragmentPagerAdapter{

        public ParallaxAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
