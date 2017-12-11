package com.kx.admin.day20.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;

import com.kx.admin.day20.R;

/**
 * Created by admin on 2017/12/8.
 */

public class LoadingView extends View {
   // private SplashState mSplashState;
    // 大圆里面包含很多小圆的半径 - 整宽度的 1/4
    private float mRotationRadius;
    // 每个小圆的半径 - 大圆半径的 1/10
    private float mCircleRadius;
    // 小圆的颜色列表
    private int[] mCircleColors;
    // 旋转动画执行的时间
    private final long ROTATION_ANIMATION_TIME = 3000;
    // 第二部分动画执行的总时间 (包括三个动画的时间)
    private final long SPLASH_ANIMATION_TIME = 1200;
    // 整体的颜色背景
    private int mSplashColor = Color.WHITE;
    /**
     * 一些变化的参数
     */
    // 空心圆初始半径
    private float mHoleRadius = 0F;
    // 当前大圆旋转的角度（弧度）
    private float mCurrentRotationAngle = 0F;
    // 当前大圆的半径
    private float mCurrentRotationRadius;

    // 绘制圆的画笔
    private Paint mPaint = new Paint();
    // 绘制背景的画笔
    private Paint mPaintBackground = new Paint();

    // 屏幕最中心的位置
    private int mCenterX;
    private int mCenterY;

    // 屏幕对角线的一半
    private float mDiagonalDist;

    private  LoadingState mLoadingState ;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCircleColors=  getContext().getResources().getIntArray(R.array.splash_circle_colors);
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mRotationRadius = w/4;
        mCenterX = w/2;
        mCenterY = h/2 ;
        mCircleRadius = mRotationRadius/10 ;
        mDiagonalDist = (float) Math.sqrt(mCenterX * mCenterX + mCenterY * mCenterY);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //   ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0f,2 * (float) Math.PI);

    }

    @Override
    protected void onDraw(Canvas canvas) {
       if (mLoadingState ==null){
           mLoadingState = new RotateState();
       }
       mLoadingState.onDraw(canvas);
    }

    /**
     * 旋转动画
     */
    class RotateState extends LoadingState{

        private final ValueAnimator mRotateAnimator;

        public RotateState() {
            mRotateAnimator = ObjectAnimator.ofFloat(0f,2f* (float) Math.PI);
            mRotateAnimator.setDuration(ROTATION_ANIMATION_TIME);
            mRotateAnimator.setRepeatCount(-1);
            mRotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotationAngle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            // 线性差值器
            mRotateAnimator.setInterpolator(new LinearInterpolator());
            mRotateAnimator.start();
        }

        @Override
        public void onDraw(Canvas canvas) {
            // 画一个背景 白色
            canvas.drawColor(mSplashColor);
            double percentAngle = Math.PI * 2 / mCircleColors.length;
            for (int i = 0; i < mCircleColors.length; i++) {
                mPaint.setColor(mCircleColors[i]);
                // 当前的角度 = 初始角度 + 旋转的角度
                double currentAngle = percentAngle * i + mCurrentRotationAngle;
                int x = mCenterX+(int) (mRotationRadius * Math.cos(currentAngle));
                int y =  mCenterY+(int) (mRotationRadius * Math.sin(currentAngle));
                canvas.drawCircle(x,y,mCircleRadius,mPaint);
            }
        }

        @Override
        public void change() {
            mRotateAnimator.cancel();
            mLoadingState = new AggregationState();
        }
    }

    /**
     * 聚合动画
     */
    class AggregationState extends LoadingState{
        private final ValueAnimator mAggregationAnimator;
        public AggregationState() {
            // 圆球运行轨迹的半径逐渐减小 ，
            mAggregationAnimator = ObjectAnimator.ofFloat(1f,0f);
            mAggregationAnimator.setDuration(SPLASH_ANIMATION_TIME);
            mAggregationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotationRadius = (float) animation.getAnimatedValue() * mRotationRadius;
                    invalidate();
                }
            });
            // 线性差值器
            mAggregationAnimator.setInterpolator(new AnticipateInterpolator(3));
            mAggregationAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoadingState = new ExpendState();
                }
            });
            mAggregationAnimator.start();
        }
        @Override
        public void onDraw(Canvas canvas) {
            // 画一个背景 白色
            canvas.drawColor(mSplashColor);
            double percentAngle = Math.PI * 2 / mCircleColors.length;
            for (int i = 0; i < mCircleColors.length; i++) {
                mPaint.setColor(mCircleColors[i]);
                // 当前的角度 = 初始角度 + 旋转的角度
                double currentAngle = percentAngle * i + mCurrentRotationAngle;
                int x = mCenterX+(int) (mCurrentRotationRadius * Math.cos(currentAngle));
                int y =  mCenterY+(int) (mCurrentRotationRadius * Math.sin(currentAngle));
                canvas.drawCircle(x,y,mCircleRadius,mPaint);
            }
        }
    }
    /**
     * 展开动画
     */
    class ExpendState extends LoadingState {
        private ValueAnimator mAnimator;
        public ExpendState() {
            mPaint.setStyle(Paint.Style.STROKE);
            mAnimator = ObjectAnimator.ofFloat(0, mDiagonalDist);
            mAnimator.setDuration(ROTATION_ANIMATION_TIME / 2);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mHoleRadius = (float) animation.getAnimatedValue(); // 0 - 对角线的一半
                    invalidate();
                }
            });
            mAnimator.start();
        }
        @Override
        public void onDraw(Canvas canvas) {
            mPaint.setColor(mSplashColor);
            // 不断更改画笔的宽度，
            int strokeWidth = (int) (mDiagonalDist  - mHoleRadius);
            mPaint.setStrokeWidth(strokeWidth);
            //圆的半径为   圆心到内弧的距离 + 宽度的一半
            canvas.drawCircle(mCenterX,mCenterY,mHoleRadius + strokeWidth/2 ,mPaint);

        }
    }

    public  void change(){
        mLoadingState.change();
    }
}
