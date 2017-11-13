package com.xk.admin.day4;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 可切换的顶部tab
 */
public class TabView extends LinearLayout {
    private Paint circlePaint;

    private float mCurrentProgress = 0.0f;
    private String [] titles = {"企业控制台","监 控","报 表","历史纪录"};
    private  int  mSelectColor ;
    private  int  unSelectColor ;
    private  int  selectCircleBorder  ,mTextSize;
    private int currentSelectPosition = 0  ;
    private int blueHeight,mHeight;
    private int mWidth ;
    public TabView(Context context) {
        this(context,null);
    }

    public TabView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TabView);
        mSelectColor = array.getColor(R.styleable.TabView_selectColor, Color.GREEN);
        unSelectColor = array.getColor(R.styleable.TabView_unSelectColor, Color.WHITE);
        selectCircleBorder =array.getDimensionPixelSize(R.styleable.TabView_selectCircleBorder, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
        mHeight =array.getDimensionPixelSize(R.styleable.TabView_mHeight, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,45, getResources().getDisplayMetrics()));
        mTextSize =array.getDimensionPixelSize(R.styleable.TabView_mTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        System.out.println(mHeight);
        this.setBackgroundColor(Color.WHITE);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStrokeWidth(selectCircleBorder);
        circlePaint.setColor(mSelectColor);
        blueHeight = mHeight- selectCircleBorder;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, mHeight-selectCircleBorder, 1);
       // Point screenSize = DeviceUtils.getScreenSize(context);
        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setText(title);
            textView.setBackgroundColor(Color.BLUE);
            textView.setLayoutParams(layoutParams);
            textView.setTextSize(mTextSize);
            textView.setTextColor(currentSelectPosition==i ? mSelectColor:unSelectColor);
            addView(textView);
            final int finalI = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentSelectPosition = finalI;
                    invalidate();
                    int childCount = getChildCount();
                    for (int j = 0; j < childCount; j++) {
                        TextView textView  = (TextView) getChildAt(j);
                        textView.setTextColor(currentSelectPosition==j ? mSelectColor:unSelectColor);
                    }
                    if (listener!=null ){
                        listener.onTabClick(finalI);
                    }
                }
            });
        }
        array.recycle();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth= w;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(size,mHeight);
    }

    public  interface  onTabClickListener{
        void  onTabClick(int position );
    }
    onTabClickListener listener ;

    public void setonTabClickListener(onTabClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int itemTabWidth = mWidth / 4;
        canvas.drawCircle(itemTabWidth *(2*currentSelectPosition +1)/2, blueHeight,selectCircleBorder,circlePaint);
    }
    /**
     * 1.根据颜色获取画笔
     */
    private Paint getPaintByColor(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setDither(true);
        //  paint.setTextSize(getTextSize());
        return paint;
    }
}
