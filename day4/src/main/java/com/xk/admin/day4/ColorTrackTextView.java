package com.xk.admin.day4;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by admin on 2017/10/30.
 * Android自定义view-玩转字体变色
 */
public class ColorTrackTextView extends android.support.v7.widget.AppCompatTextView {
    // 1. 实现一个文字两种颜色 - 绘制不变色字体的画笔
    private Paint mOriginPaint;
    // 1. 实现一个文字两种颜色 - 绘制变色字体的画笔
    private Paint mChangePaint;
    // 1. 实现一个文字两种颜色 - 当前的进度
    private float mCurrentProgress = 0.0f;

    public float getmCurrentProgress() {
        return mCurrentProgress;
    }

    public void setmCurrentProgress(float mCurrentProgress) {
        this.mCurrentProgress = mCurrentProgress;
    }

    // 2.实现不同朝向
    private Direction mDirection = Direction.LEFT_TO_RIGHT;
    private Rect textRect;

    public enum Direction{
        LEFT_TO_RIGHT,RIGHT_TO_LEFT
    }
    public ColorTrackTextView(Context context) {
        this(context,null);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);

        int originColor = array.getColor(R.styleable.ColorTrackTextView_originColor, getTextColors().getDefaultColor());
        int changeColor = array.getColor(R.styleable.ColorTrackTextView_changeColor, getTextColors().getDefaultColor());
        mOriginPaint = getPaintByColor(originColor);
        mChangePaint = getPaintByColor(changeColor);
        textRect = new Rect();
        String text  = getText().toString();
        mOriginPaint.getTextBounds(text,0,text.length(), textRect);
        // 回收
        array.recycle();
    }
    /**
     * 1.根据颜色获取画笔
     */
    private Paint getPaintByColor(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextSize(getTextSize());
        return paint;
    }
    // 1. 一个文字两种颜色
    // 利用clipRect的API 可以裁剪  左边用一个画笔去画  右边用另一个画笔去画  不断的改变中间值
    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        // canvas.clipRect();  裁剪区域
        // 根据进度把中间值算出来
        int middle = (int) (mCurrentProgress * getWidth());

        // 从左变到右
        if(mDirection == Direction.LEFT_TO_RIGHT) {  // 左边是红色右边是黑色
            // 绘制变色
            drawText(canvas, mChangePaint , 0, middle);   //绘制左边
            drawText(canvas, mOriginPaint, middle, getWidth());  //绘制右边
        }else{
            // 右边是红色左边是黑色
            drawText(canvas, mChangePaint, getWidth()-middle, getWidth());    //绘制右边
            // 绘制变色
            drawText(canvas, mOriginPaint, 0, getWidth()-middle);      //绘制左边
        }
    }
    /**
     * 绘制Text
     * @param canvas
     * @param paint
     * @param start
     * @param end
     */
    private void drawText(Canvas canvas, Paint paint, int start, int end) {
        canvas.save();
        // 绘制不变色
        Rect rect = new Rect(start, 0, end, getHeight());
        canvas.clipRect(rect);
        // 我们自己来画
        String text = getText().toString();
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        // 获取字体的宽度
        int x = getWidth() / 2 - bounds.width() / 2;
        // 基线baseLine
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(text, x, baseLine, paint);// 这么画其实还是只有一种颜色
        canvas.restore();
    }

    public void setDirection(Direction direction){
        this.mDirection = direction;
    }

    public void setCurrentProgress(float currentProgress){
        this.mCurrentProgress = currentProgress;
        invalidate();
    }

    public void setChangeColor(int changeColor) {
        this.mChangePaint.setColor(changeColor);
    }

    public void setOriginColor(int originColor) {
        this.mOriginPaint.setColor(originColor);
    }

}
