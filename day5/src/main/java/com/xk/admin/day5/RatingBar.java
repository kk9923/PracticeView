package com.xk.admin.day5;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by admin on 2017/11/4.
 */

/**
 * 自定义评分控件
 */
public class RatingBar extends View {
    private static final String TAG = "RatingBar";
    private Bitmap mStarNormalBitmap, mStarFocusBitmap;
    private int mGradeNumber = 5;    //最大数量

    private int mCurrentGrade = 0;   //当前选中数量
    private int mItemSpacimg  = 0;  //间距
    private int itemWidth;

    public RatingBar(Context context) {
        this(context, null);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        int starNormalId = array.getResourceId(R.styleable.RatingBar_starNormal, 0);
        if (starNormalId == 0) {
            throw new RuntimeException("请设置属性 starNormal ");
        }

        mStarNormalBitmap = BitmapFactory.decodeResource(getResources(), starNormalId);

        int starFocusId = array.getResourceId(R.styleable.RatingBar_starFocus, 0);
        if (starFocusId == 0) {
            throw new RuntimeException("请设置属性 starFocus ");
        }

        mStarFocusBitmap = BitmapFactory.decodeResource(getResources(), starFocusId);

        mGradeNumber = array.getInt(R.styleable.RatingBar_maxNumber, mGradeNumber);
        mItemSpacimg = array.getDimensionPixelSize(R.styleable.RatingBar_itemSpacing, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5,getResources().getDisplayMetrics()));
        itemWidth = mStarNormalBitmap.getWidth()+getPaddingRight()+getPaddingLeft()+mItemSpacimg;
        array.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = mStarFocusBitmap.getHeight() + getPaddingTop()+getPaddingBottom() ;
        int width = mStarFocusBitmap.getWidth() * mGradeNumber +getPaddingLeft() +getPaddingRight() + 4* mItemSpacimg;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            // 结合第二个步骤 触摸的时候mCurrentGrade值是不断变化
            if(mCurrentGrade>i){// 1  01
                // 当前分数之前
                canvas.drawBitmap(mStarFocusBitmap, i* itemWidth, 0, null);
            }else{
                canvas.drawBitmap(mStarNormalBitmap, i* itemWidth, 0, null);
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
              case MotionEvent.ACTION_DOWN :   //按下
                case MotionEvent.ACTION_MOVE:  // 移动
                    float x = event.getX();
                    Log.e(TAG,"x=  " +x);
                    Log.e(TAG,"getWidth=  " +getWidth());
                    int currentNum = (int) (x *5 /getWidth()+1 );
                    Log.e(TAG,"currentNum=  " +currentNum);
                    // 范围问题
                    if(currentNum<0){
                        currentNum = 0;
                    }
                    if(currentNum>mGradeNumber){
                        currentNum = mGradeNumber;
                    }
                    // 分数相同的情况下不要绘制了 , 尽量减少onDraw()的调用
                    if(currentNum == mCurrentGrade){
                        return true;
                    }
                    mCurrentGrade = currentNum ;
                    Log.e(TAG,"mGradeNumber=  " +mCurrentGrade);
                   // System.out.println(mGradeNumber);
                    invalidate();
                  //  return true;
                    break;
        }
        return true;
    }
}
