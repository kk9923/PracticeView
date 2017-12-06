package com.xk.admin.day14;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;

/**
 * Created by admin on 2017/11/14.
 */

public class VerticalDragView extends FrameLayout {

    private View dragView;

    private ViewDragHelper dragHelper ;
    private int mMenuHeight;
    private boolean mMenuIsOpen = false;

    public VerticalDragView(@NonNull Context context) {
        this(context,null);
    }

    public VerticalDragView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VerticalDragView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dragHelper=ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {   //控制可拖动的子View
                //return child==dragView;
                return true;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {        // 控制竖直方向上 可拖动子View 的距离
                if (child== dragView){
                    if (top< 0 ){
                        top = 0 ;
                    }else if ( top > mMenuHeight){
                        top = mMenuHeight ;
                    }
                }else {
                    top=0;
                }
                return top;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild == dragView) {
                    if (dragView.getTop() > mMenuHeight / 2) {
                        // 滚动到菜单的高度（打开）
                      //  mDragHelper.settleCapturedViewAt(0, mMenuHeight);
                        dragHelper.smoothSlideViewTo(dragView,0,mMenuHeight);
                    //   dragHelper.settleCapturedViewAt(0,mMenuHeight);
                        mMenuIsOpen = true;
                    } else {
                        // 滚动到0的位置（关闭）
                     //   mDragHelper.settleCapturedViewAt(0, 0);
                        dragHelper.smoothSlideViewTo(dragView,0,0);
                       // dragHelper.settleCapturedViewAt(0,0);
                        mMenuIsOpen = false;
                    }
                    invalidate();
                }
            }

        });
    }

    /**
     * 响应滚动
     */
    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dragView = getChildAt(1);
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            View menuView = getChildAt(0);
            mMenuHeight = menuView.getMeasuredHeight();
        }
    }

    // 现象就是ListView可以滑动，但是菜单滑动没有效果了
    private float mDownY;
//  because ACTION_DOWN was not received for this pointer before ACTION_MOVE.
// TODO: 2017/11/15    问题 ？   dragView 处于 menuView 下方时， menuView 和 dragView 都无法响应点击事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (mMenuIsOpen){
//            return true;
//        }
        // 向下滑动拦截，不要给ListView做处理
        // 谁拦截谁 父View拦截子View ，但是子 View 可以调这个方法
        // requestDisallowInterceptTouchEvent 请求父View不要拦截，改变的其实就是 mGroupFlags 的值
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
               // System.out.println(mDownY);
                // 让 DragHelper 拿一个完整的事件
                dragHelper.processTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY();
           //     System.out.println(moveY);
                if ((moveY - mDownY) > 0 && !canChildScrollUp()) {
                    //当手指向下划，(moveY - mDownY) > 0
                  //   代码来自于SwipeRefreshLayout中的判断  子View还能向下滑动   canChildScrollUp()
                 //    向下滑动 && 滚动到了顶部（不能向下滑动 ），拦截不让ListView做处理
                    return true;
                } else  if ((moveY - mDownY) < 0 && !canChildScrollUp() && mMenuIsOpen){
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    // TODO: 2017/11/15  修改版  dragView 和 menuView  都可以响应点击事件    
    // TODO: 2017/11/15  问题 ？  1 : 滑动时不连续： 当拖动dragView向上划，滑倒顶部时，继续划就划不动了，要送开手指才能dragView才能继续划
    // TODO: 2017/11/15           2 :  当拖动dragView向下划，滑倒顶部时，继续划就划不动了，要送开手指才能dragView才能继续划动到menuView的下面（即松开手指，再次按下时dragHelper才能响应事件）
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        // 向下滑动拦截，不要给ListView做处理
//        // 谁拦截谁 父View拦截子View ，但是子 View 可以调这个方法
//        // requestDisallowInterceptTouchEvent 请求父View不要拦截，改变的其实就是 mGroupFlags 的值
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mDownY = ev.getY();
//                // 让 DragHelper 拿一个完整的事件
//                dragHelper.processTouchEvent(ev);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float moveY = ev.getY();
//                System.out.println(moveY);
//                if ((moveY - mDownY) > 0 && !canChildScrollUp()) {
//                    //当手指向下划，(moveY - mDownY) > 0
//                    //   代码来自于SwipeRefreshLayout中的判断  子View还能向下滑动   canChildScrollUp()
//                    //    向下滑动 && 滚动到了顶部（不能向下滑动 ），拦截不让ListView做处理
//                    return true;
//                } else  if ((moveY - mDownY) < 0 && !canChildScrollUp() && mMenuIsOpen){
//                    return true;
//                }
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_MOVE :
//                float moveY = event.getY();
//                if ((moveY - mDownY) < 0 && dragView.getTop()<=0 && mMenuIsOpen){
//                   return true;
//               }
//                break;
//        }
        dragHelper.processTouchEvent(event);
        return true;
    }
    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     * 判断View是否滚动到了最顶部,还能不能向上滚
     */
    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (dragView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) dragView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(dragView, -1) || dragView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(dragView, -1);
        }
    }

}
