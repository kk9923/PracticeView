package com.kx.admin.day18;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * Created by admin on 2017/12/6.
 * 贝塞尔曲线 - 花束直播点赞效果
 */

public class LoveLayout extends RelativeLayout {
    private Interpolator[] mInterpolator;
    private int [] images = {R.drawable.pl_blue,R.drawable.pl_red,R.drawable.pl_yellow,};
    //产生随机数
    private Random random ;
    //  控件宽高
    private  int screenWidth ;
    private  int screenHeight ;
    public LoveLayout(Context context) {
        this(context,null);
    }

    public LoveLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        random = new Random();
        mInterpolator = new Interpolator[]{new AccelerateDecelerateInterpolator(),new AccelerateInterpolator(),
                new DecelerateInterpolator(),new LinearInterpolator()};
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        screenWidth = w;
        screenHeight = h;

    }

    /**
     * 添加点赞图片
     */
    public void addLove() {
        final ImageView imageView = new ImageView(getContext());
        int imageIndex = random.nextInt(images.length );
        imageView.setImageResource(images[imageIndex]);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(CENTER_HORIZONTAL);
        imageView.setLayoutParams(layoutParams);
        addView(imageView);

        // 放大和透明度变化动画 （属性动画）
        AnimatorSet firstAnimator = new AnimatorSet();
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(imageView, "alpha", 0.3f, 1.0f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(imageView, "scaleX", 0.3f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(imageView, "scaleY", 0.3f, 1.0f);
        firstAnimator.playTogether(alphaAnimator,scaleXAnimator,scaleYAnimator);
        firstAnimator.setDuration(350);
        firstAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //当底部的缩放和透明度动画执行完后   执行向上的位移动画
                addBezierAnimator(imageView);
            }
        });
        firstAnimator.start();
    }

    /**
     * 向上的位移动画-------贝塞尔曲线
     */
    private void addBezierAnimator(final ImageView imageView) {
        //得到ImageView对应的 drawable对象
        Drawable drawable = imageView.getDrawable();
        //图片实际高度
        int intrinsicHeight = drawable.getIntrinsicHeight();
        //图片实际宽度
        int intrinsicWidth = drawable.getIntrinsicWidth();
        // 三阶贝塞尔曲线的四个点    p1  起点     p2  控制点1     p3   控制点2      p4    终点
        // 起点  为图片左上角定点
        PointF point1 = new PointF(screenWidth/2-intrinsicWidth/2,screenHeight-intrinsicHeight);
        //控制点1     x 坐标随机  y  保证在下半部分
        PointF point2 = new PointF(random.nextInt(screenWidth),random.nextInt(screenHeight/2));
        //控制点2      x 坐标随机  y  保证在上半部分
        PointF point3 = new PointF(random.nextInt(screenWidth),random.nextInt(screenHeight/2)+screenHeight/2);
        //终点    屏幕最顶端，x  随机  但不能超过屏幕外 所以减去一个图片的宽度
        PointF point4 = new PointF(random.nextInt(screenWidth)-intrinsicWidth,0);
        LoveTypeEvaluator loveTypeEvaluator = new LoveTypeEvaluator(point2,point3);
        ValueAnimator valueAnimator = ObjectAnimator.ofObject(loveTypeEvaluator, point1, point4);
        valueAnimator.setInterpolator(mInterpolator[random.nextInt(mInterpolator.length)]);
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 得到每个时间点内 内塞尔曲线上的点的坐标
                PointF pointF = (PointF)  animation.getAnimatedValue();
                //取出x和y值 设置给图片  形成图片运动的效果
                imageView.setX(pointF.x);
                imageView.setY(pointF.y);
                //动画执行百分比
                float animatedFraction = animation.getAnimatedFraction();
                // 给图片设置透明度变化
                imageView.setAlpha(1 - animatedFraction + 0.2f);
            }
        });
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //在动画结束后移除图片对象
                removeView(imageView);
            }
        });

    }


}
