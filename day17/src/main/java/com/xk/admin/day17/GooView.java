package com.xk.admin.day17;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;


public class GooView extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //存储静态的两个点
    PointF[] staticPointFs = new PointF[] { new PointF(300f, 300f), new PointF(250f, 350f) };
    //存储移动的两个点
    PointF[] movePointFs = new PointF[] { new PointF(50f, 250f), new PointF(50f, 350f) };
    PointF dragCenter = new PointF(300,300);
    PointF staticCenter = new PointF(300,300);
    //控制点
    PointF controlPointF = new PointF(150f, 300f);
    //移动圆直径
    private float mMoveRadius = dip2px(20);
    //固定圆最大直径
    private float maxStaticRadius = dip2px(15);
    // 两圆之间最大距离
  //  private float maxLines = dip2px(100);
    //固定圆直径
    private float mStaticRadius = maxStaticRadius;
    public GooView(Context context) {
        this(context,null);
    }

    public GooView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GooView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint.setColor(Color.RED);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN :
                break;
            case MotionEvent.ACTION_MOVE:
                float newX = event.getX();
                float newY = event.getY();
                dragCenter.x= newX;
                dragCenter.y= newY;
                break;
            case MotionEvent.ACTION_UP :
                break;
        }
        invalidate();
        return true;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制移动圆
        canvas.drawCircle(dragCenter.x,dragCenter.y,mMoveRadius,paint);
        //  两圆中心的距离
        double lines = Math.sqrt((staticCenter.x - dragCenter.x)* (staticCenter.x - dragCenter.x) +(staticCenter.y - dragCenter.y)* (staticCenter.y - dragCenter.y) );
        mStaticRadius = (float) (maxStaticRadius - lines/14);
        //绘制固定圆
        canvas.drawCircle(staticCenter.x,staticCenter.y,mStaticRadius,paint);
    //    Path bezeierPath = getBezeierPath();
        getBezeierPath();
        canvas.drawPath(bezeierPath,paint);
    }
    Path bezeierPath = new Path();
    /**
     * 获取贝塞尔的路径
     * @return
     */
    public void getBezeierPath() {
        bezeierPath .reset();
        double distance = getDistance();
        mStaticRadius = (int) (maxStaticRadius - distance / 14);
        if (mStaticRadius  > maxStaticRadius) {
            // 超过一定距离 贝塞尔和固定圆都不要画了
            return ;
        }
        // 求角 a
        // 求斜率
        float dy = (dragCenter.y-staticCenter.y);
        float dx = (dragCenter.x-staticCenter.x);
        float tanA = dy/dx;
        // 求角a
        double arcTanA = Math.atan(tanA);
        // p0
        float p0x = (float) (dragCenter.x + mMoveRadius*Math.sin(arcTanA));
        float p0y = (float) (dragCenter.y - mMoveRadius*Math.cos(arcTanA));

        // p1
        float p1x = (float) (staticCenter.x + mStaticRadius*Math.sin(arcTanA));
        float p1y = (float) (staticCenter.y - mStaticRadius*Math.cos(arcTanA));

        // p2
        float p2x = (float) (staticCenter.x - mStaticRadius*Math.sin(arcTanA));
        float p2y = (float) (staticCenter.y + mStaticRadius*Math.cos(arcTanA));

        // p3
        float p3x = (float) (dragCenter.x - mMoveRadius*Math.sin(arcTanA));
        float p3y = (float) (dragCenter.y + mMoveRadius*Math.cos(arcTanA));

        // 拼装 贝塞尔的曲线路径
        bezeierPath.moveTo(p0x,p0y); // 移动
        // 两个点
        PointF controlPoint = getControlPoint();
        // 画了第一条  第一个点（控制点,两个圆心的中心点），终点
        bezeierPath.quadTo(controlPoint.x,controlPoint.y,p1x,p1y);

        // 画第二条
        bezeierPath.lineTo(p2x,p2y); // 链接到
        bezeierPath.quadTo(controlPoint.x,controlPoint.y,p3x,p3y);
        bezeierPath.close();

    }

    public PointF getControlPoint() {
        return new PointF((staticCenter.x+dragCenter.x)/2,(staticCenter.y+dragCenter.y)/2);
    }
    private float  dip2px(float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getResources().getDisplayMetrics());
    }
    private double getDistance(){
        return  Math.sqrt((staticCenter.x - dragCenter.x)* (staticCenter.x - dragCenter.x) +(staticCenter.y - dragCenter.y)* (staticCenter.y - dragCenter.y) );
    }
}
