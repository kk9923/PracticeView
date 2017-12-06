package com.xk.admin.day16.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by admin on 2017/12/5.
 */

public class DropDownMenu extends LinearLayout {
    //顶部菜单布局
    private LinearLayout tabMenuView;
    //底部容器，包含popupMenuViews，maskView
    private FrameLayout containerView;
    //弹出菜单父布局
    private FrameLayout mMenuContainerView;
    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    //tabMenuView里面选中的tab位置，-1表示未选中
    private int current_tab_position = -1;
 private     BaseMenuAdapter mAdapter ;
    //分割线颜色
    private int dividerColor = 0xffcccccc;
    //tab选中颜色
   // private int textSelectedColor = 0xff890c85;
    //tab未选中颜色
  //  private int textUnselectedColor = 0xff111111;
    //遮罩颜色
    private int maskColor = 0x88888888;
    //tab字体大小
    private int menuTextSize = 14;

    //tab选中图标
 //   private int menuSelectedIcon;
    //tab未选中图标
  //  private int menuUnselectedIcon;
    // 内容菜单的高度
    private int mMenuContainerHeight;
    // 当前打开的位置
    private int mCurrentPosition = -1;
    private long DURATION_TIME = 350;
    // 动画是否在执行
    private boolean mAnimatorExecute;
    private float menuHeighPercent = 0.5f;
    public DropDownMenu(Context context) {
        this(context,null);
    }
    public DropDownMenu(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    public DropDownMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);

        //为DropDownMenu添加自定义属性
        int menuBackgroundColor = 0xffffffff;
        int underlineColor = 0xffcccccc;
        //初始化tabMenuView并添加到tabMenuView
        tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
       tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView, 0);

        //为tabMenuView添加下划线
        View underLine = new View(getContext());
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2Px(0.5f)));
        underLine.setBackgroundColor(underlineColor);
        addView(underLine, 1);

        //初始化containerView并将其添加到DropDownMenu
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView, 2);



        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              //  closeMenu();
            }
        });

        mMenuContainerHeight = (int) (DeviceUtils.getScreenSize(getContext()).y * menuHeighPercent);
    }
    /**
     * 设置 Adapter
     * @param adapter
     */
    public void setAdapter(BaseMenuAdapter adapter,View contentView) {
        this.mAdapter = adapter;
        // 获取有多少条
        int count = mAdapter.getCount();
        containerView.addView(contentView, 0);
        containerView.addView(maskView, 1);
        maskView.setVisibility(GONE);
        if (containerView.getChildAt(2) != null){
            containerView.removeViewAt(2);
        }
        mMenuContainerView=new FrameLayout(getContext());
        mMenuContainerView .setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mMenuContainerHeight));
        containerView.addView(mMenuContainerView, 2);
        for (int i = 0; i < count; i++) {
            // 获取菜单的Tab
            View tabView = mAdapter.getTabView(i, tabMenuView);
            tabMenuView.addView(tabView);
            LinearLayout.LayoutParams params = (LayoutParams) tabView.getLayoutParams();
            params.weight = 1;
            tabView.setLayoutParams(params);

            // 设置tab点击事件
            setTabClick(tabView, i);

            // 获取菜单的内容
            View menuView = mAdapter.getMenuView(i, mMenuContainerView);
            menuView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            menuView.setVisibility(GONE);
            mMenuContainerView.addView(menuView);
        }
    }
    private void setTabClick(View tabView, int position) {


    }

    public int dp2Px(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }
}
