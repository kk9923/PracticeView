package com.xk.admin.day15;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by admin on 2017/12/4.
 */

public class CircleView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int circleColor = Color.RED ;
    public CircleView(Context context) {
        this(context,null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int x = getWidth()/2;
        int y = getWidth()/2;
        canvas.drawCircle(x,y,x,paint);
    }
    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
        paint.setColor(circleColor);
        invalidate();
    }

    public int getCircleColor() {
        return circleColor;
    }
}
