package com.xk.admin.day6;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * 自定义联系人控件 A-Z
 */
public class IndexView extends View {
    private static final String TAG = "IndexView";
    private Paint mPaint;
    // 定义26个字母
    public String[] mLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private Rect rect;
    private int itemHeight ;
    private int currentPosition = 0 ;

    public IndexView(Context context) {
        this(context,null);
    }
    public IndexView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    public IndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(25);
        rect = new Rect();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        itemHeight = h/mLetters.length;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
       // setMeasuredDimension();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mLetters.length; i++) {
            mPaint.setColor(i==currentPosition ?Color.RED :Color.BLACK);
            String text = mLetters[i];
            mPaint.getTextBounds(text,0,text.length(),rect);
            canvas.drawText(text,getWidth()/2 - rect.width()/2,(itemHeight+ rect.height())/2 + itemHeight*i,mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                int currentP = (int) (y /itemHeight);
                if (currentP!=currentPosition){
                    currentPosition = currentP;
                    invalidate();
                }
                break;

        }
        return true;
    }
}
