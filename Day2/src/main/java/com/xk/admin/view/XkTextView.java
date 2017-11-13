package com.xk.admin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;

/**
 * Created by admin on 2017/10/26.
 */

/**
         *  <!-- name 属性名称
             format 格式: string 文字  color 颜色
             dimension 宽高 字体大小  integer 数字
             reference 资源（drawable）
             -->
             <attr name="xkText" format="string"/>
             <attr name="xkTextColor" format="color"/>
             <attr name="xkTextSize" format="dimension"/>
             <attr name="xkMaxLength" format="integer"/>
             <!-- background 自定义View都是继承自View , 背景是由View管理的-->
             <!--<attr name="darrenBackground" format="reference|color"/>-->
             <!-- 枚举 -->
             <attr name="xkInputType">
                 <enum name="number" value="1"/>
                 <enum name="text" value="2"/>
                 <enum name="password" value="3"/>
             </attr>
 */
public class XkTextView extends LinearLayout {
    private String mText;
    private int mTextColor ;
    private int mTextSize ;
    private int mMaxLength = -1;
    private int mInputType = -1;
    private  Paint   mPaint ;
    private Rect bounds;

    public XkTextView(Context context) {
        this(context,null);
    }

    public XkTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XkTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.XkTextView);
        mText = typedArray.getString(R.styleable.XkTextView_xkText);
        mTextColor = typedArray.getInt(R.styleable.XkTextView_xkTextColor, Color.RED);
       // mTextSize = typedArray.getDimensionPixelSize(R.styleable.XkTextView_xkTextSize, sp2px(16));
        mTextSize=typedArray.getDimensionPixelSize(R.styleable.XkTextView_xkTextSize, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        mMaxLength = typedArray.getInt(R.styleable.XkTextView_xkMaxLength, -1);
        mInputType = typedArray.getInt(R.styleable.XkTextView_xkInputType,2);
        typedArray.recycle();

        mPaint = new Paint();
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 设置字体的大小和颜色
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);

        // setBackgroundColor(Color.TRANSPARENT);
        setWillNotDraw(false);
        bounds = new Rect();
        // 获取文本的Rect
        mPaint.getTextBounds(mText,0,mText.length(), bounds);
    }
    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,
                getResources().getDisplayMetrics());
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 布局的宽高都是由这个方法指定
        // 指定控件的宽高，需要测量
        // 获取宽高的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 1.确定的值，这个时候不需要计算，给的多少就是多少
        int width = MeasureSpec.getSize(widthMeasureSpec);

        // 2.给的是wrap_content 需要计算
        if(widthMode == MeasureSpec.AT_MOST){
            // 计算的宽度 与 字体的长度有关  与字体的大小  用画笔来测量
            width = bounds.width() + getPaddingLeft() +getPaddingRight();
        }
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if(widthMode == MeasureSpec.AT_MOST){
            // 计算的宽度 与 字体的长度有关  与字体的大小  用画笔来测量
            height = bounds.height() + getPaddingTop() + getPaddingBottom();
        }
        // 设置控件的宽高
        setMeasuredDimension(width,height);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //dy 代表的是：高度的一半到 baseLine的距离
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        // top 是一个负值  bottom 是一个正值    top，bttom的值代表是  bottom是baseLine到文字底部的距离（正值）
        // 必须要清楚的，可以自己打印就好
        int dy = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        int baseLine = getHeight()/2 + dy;

        int x = getPaddingLeft();
        canvas.drawText(mText,x,baseLine,mPaint);

    }
}
