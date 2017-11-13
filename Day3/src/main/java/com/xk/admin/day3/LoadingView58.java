package com.xk.admin.day3;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;



/**
 * Created by admin on 2017/11/1.
 */

/**
 * 简单切换58加载 三种状态的View
 */

public class LoadingView58 extends View {
    private Path path ;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Shape currentShape = Shape.CIRCLE;

    public LoadingView58(Context context) {
        this(context,null);
    }

    public LoadingView58(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView58(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);

        a.recycle();

    }
    private int mWidth,mHeight ;
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
        setMeasuredDimension(widthSize > heightSize ? heightSize : widthSize,widthSize > heightSize ? heightSize : widthSize);
    }
    public void  changeShape(){
        switch (currentShape){
            case CIRCLE:
                currentShape =Shape.RANGTE;
                break;
            case RANGTE:
                currentShape = Shape.SANJIAO;
                break;
            case SANJIAO:
                currentShape = Shape.CIRCLE;
                break;
        }
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);
        switch (currentShape){
            case CIRCLE:
                paint.setColor(Color.RED);
                canvas.drawCircle(0,0,mWidth/2,paint);
                break;
            case RANGTE:
                paint.setColor(Color.BLUE);
                canvas.drawRect(-mWidth/2,-mHeight/2,mWidth/2,mWidth/2,paint);
                break;
            case SANJIAO:
                if (path ==null){
                    path =new Path();
                    path.moveTo(0,-mWidth/2);
                    path.lineTo(-mWidth/2, (float) (0.732* mWidth/2));
                    path.lineTo(mWidth/2, (float) (0.732* mWidth/2));
                    path.close();
                }
                paint.setColor(Color.YELLOW);
                canvas.drawPath(path,paint);
                break;
        }
    }

    private enum Shape {   //三种形状
        CIRCLE,RANGTE,SANJIAO
    }
}
