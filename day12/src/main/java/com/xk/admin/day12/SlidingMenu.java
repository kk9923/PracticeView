package com.xk.admin.day12;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import static com.xk.admin.day12.ScreenUtils.getScreenWidth;

/**
 * Created by admin on 2017/11/13.
 */

/**
 * QQ5.0 侧滑菜单
 */
public class SlidingMenu extends HorizontalScrollView {
    // 菜单的宽度
    private int mMenuWidth;

    private View mContentView,mMenuView;

    // GestureDetector 处理快速滑动
    private GestureDetector gestureDetector ;

    // 菜单是否打开
    private boolean mMenuIsOpen = false;
    public SlidingMenu(Context context) {
        this(context,null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu);

        float rightMargin = array.getDimension(
                R.styleable.SlidingMenu_menuRightMargin, ScreenUtils.dip2px(context, 50));
        // 菜单页的宽度是 = 屏幕的宽度 - 右边的一小部分距离（自定义属性）
        mMenuWidth = (int) (getScreenWidth(context) - rightMargin);
        array.recycle();
        gestureDetector = new GestureDetector(getContext(),mGestureListener);
    }
    private GestureDetector.OnGestureListener mGestureListener = new
            GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    // 只关注快速滑动,只要快速就会回掉
                    // 条件 打开的时候往右边快速滑动切换（关闭），关闭的时候往左边快速滑动切换（打开）
                    Log.e("TAG", "velocityX -> " + velocityX);
                    // 快速往左边滑动的时候是一个负数，往右边滑动的时候是一个正数
                    if (mMenuIsOpen) {
                        // 打开的时候往右边快速滑动切换（关闭）
                        if (velocityX < 0) {
                            closeMenu();
                            return true;
                        }
                    } else {
                        // 关闭的时候往左边快速滑动切换（打开）
                        if (velocityX > 0) {
                            openMenu();
                            return true;
                        }
                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            };

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ViewGroup container = (ViewGroup) getChildAt(0);   // 得到包裹的线性布局

        mMenuView = container.getChildAt(0);
        // 设置只能通过 LayoutParams ，
        ViewGroup.LayoutParams menuParams = mMenuView.getLayoutParams();
        menuParams.width = mMenuWidth;
        // 设置菜单页的宽度
        mMenuView.setLayoutParams(menuParams);

        // 2.设置内容页的宽度是
        mContentView = container.getChildAt(1);
        ViewGroup.LayoutParams contentParams = mContentView.getLayoutParams();
        contentParams.width = getScreenWidth(getContext());
        mContentView.setLayoutParams(contentParams);
         //  页面第一次打开的时候关闭菜单页    没有效果    需要在onLayout()方法中调用
        // 原因：   onFinishInflate() 方法比onLayout()方法先调用，即使这里调用scrollTo(mMenuWidth,0);在onLayout()方法中也会重新Layout，会打开菜单页
        // scrollTo(mMenuWidth,0);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
         float leftScale= 1f * l /mMenuWidth ;    //   关闭 ----   打开         leftScale 从 1  到  0
        float rightScale = 0.7f + 0.3f *leftScale; //   rightScale   从 1  到  0.7
        ViewCompat.setPivotX(mContentView,0);
        ViewCompat.setPivotY(mContentView,mContentView.getMeasuredHeight()/2);
        ViewCompat.setScaleX(mContentView,rightScale);
        ViewCompat.setScaleY(mContentView,rightScale);
      //  System.out.println(l);
      //  scrollTo(mMenuWidth,0);

        float leftAlpha = 0.7f + (1-leftScale)*0.3f;    //  左边的透明度  leftAlpha  从 0.7 到  1
        ViewCompat.setAlpha(mMenuView,leftAlpha);
        ViewCompat.setScaleX(mMenuView,leftAlpha);
        ViewCompat.setScaleY(mMenuView,leftAlpha);
        ViewCompat.setTranslationX(mMenuView,0.25f*l);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        scrollTo(mMenuWidth,0);
    }
    // 是否拦截
    private boolean mIsIntercept = false;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mIsIntercept = false;
        if (mMenuIsOpen){
            float x = ev.getX();
            if (x > mMenuWidth){
                mIsIntercept = true;
                closeMenu();
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mIsIntercept){
            return  true ;
        }
        if (gestureDetector.onTouchEvent(ev)){
            return true ;
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            // 只需要管手指抬起 ，根据我们当前滚动的距离来判断
            int currentScrollX = getScrollX();

            if (currentScrollX > mMenuWidth / 2) {
                // 关闭
                closeMenu();
            } else {
                // 打开
                openMenu();
            }
            // 确保 super.onTouchEvent() 不会执行
            return true;
        }
        return super.onTouchEvent(ev);
    }
    /**
     * 打开菜单 滚动到 0 的位置
     */
    private void openMenu() {
        // smoothScrollTo 有动画
        mMenuIsOpen = true ;
        smoothScrollTo(0, 0);
    }

    /**
     * 关闭菜单 滚动到 mMenuWidth 的位置
     */
    private void closeMenu() {
        mMenuIsOpen = false ;
        smoothScrollTo(mMenuWidth, 0);
    }

}
