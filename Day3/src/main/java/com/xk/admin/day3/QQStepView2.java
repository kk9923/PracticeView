package com.xk.admin.day3;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by admin on 2017/10/28.
 */

/**
 *  漂亮的加载进度条
 */
public class QQStepView2 extends View  {
    private Paint outerPaint ,innerPaint ,textPaint ;
    private int mOuterColor,mInnerColor,mTextColor ,mTextSize ,borderWidth ;
    private  int progress =0;
    private Rect textRect;
    private RectF rect;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
       // postInvalidate();
    }

    public QQStepView2(Context context) {
        this(context,null);
    }

    public QQStepView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
private int mWidth,mHeight ;
    public QQStepView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.QQStepView);
        mOuterColor = a.getColor(R.styleable.QQStepView_outerColor, Color.RED);
        mInnerColor = a.getColor(R.styleable.QQStepView_innerColor, Color.BLUE);
        mTextColor = a.getColor(R.styleable.QQStepView_stepTextColor, Color.YELLOW);
        mTextSize = a.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        borderWidth = a.getDimensionPixelSize(R.styleable.QQStepView_borderWidth, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()));
        a.recycle();
        outerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerPaint.setColor(mOuterColor);
        outerPaint.setStrokeWidth(borderWidth);
        outerPaint.setStrokeCap(Paint.Cap.ROUND);
        outerPaint.setStyle(Paint.Style.STROKE);
        innerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerPaint.setColor(mInnerColor);
        innerPaint.setStrokeWidth(borderWidth);
        innerPaint.setStrokeCap(Paint.Cap.ROUND);
        innerPaint.setStyle(Paint.Style.STROKE);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(mTextSize);
        textPaint.setColor(mTextColor);
        textPaint.setStrokeWidth(borderWidth);

        textRect = new Rect();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth= w;
        mHeight =  h  ;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);   //    宽度测量模式
        int heigthMode = MeasureSpec.getMode(heightMeasureSpec);    //    高度测量模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);      //  宽度测量值
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);  // 高度测量值
      //  if (widthMode == MeasureSpec.AT_MOST){
      //      widthSize = 800;
      //  }
       // if (heigthMode == MeasureSpec.AT_MOST) {
       //     heightSize = 800 ;
       // }
        setMeasuredDimension(widthSize > heightSize ? heightSize : widthSize,widthSize > heightSize ? heightSize : widthSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);
        rect = new RectF(- mWidth/2+ borderWidth/2,-mHeight/2+ borderWidth/2,mWidth/2- borderWidth/2,mHeight/2- borderWidth/2);
        canvas.drawArc(rect,0,360,false,outerPaint);
        canvas.drawArc(rect,0,360 * progress / 100,false,innerPaint);
        textPaint.getTextBounds(progress+"%",0,(progress+"%").length(),textRect);
        canvas.drawText(progress+"%",- textRect.width()/2 ,textRect.height()/2,textPaint);
       // canvas.save();
      //  textPaint.setStrokeWidth(1);
      //  canvas.drawLine(-2000,0,2000,0,textPaint);
      //  canvas.drawLine(-0,-2220,0,20000,textPaint);
       // canvas.restore();
    }
}
