package com.xk.admin.day15;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * Created by admin on 2017/12/4.
 */

public class LoadingView extends RelativeLayout {

    private CircleView midCircleView;
    private CircleView leftCircleView;
    private CircleView rightCircleView;
    private int mTranslationDistance = 30;
    private final long ANIMATION_TIME = 400;
    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        midCircleView = getCircleView(context);
        midCircleView.setCircleColor(Color.RED);

        leftCircleView = getCircleView(context);
        leftCircleView.setCircleColor(Color.GREEN);

        rightCircleView = getCircleView(context);
        rightCircleView.setCircleColor(Color.BLUE);
        addView(leftCircleView);
        addView(rightCircleView);
        addView(midCircleView);
        post(new Runnable() {
            @Override
            public void run() {
                startOutAnimation();
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    private void startOutAnimation() {
        ObjectAnimator leftTranslationAnimator = ObjectAnimator.ofFloat(leftCircleView,"translationX",0,-dip2px(mTranslationDistance));
        ObjectAnimator rightTranslationAnimator =  ObjectAnimator.ofFloat(rightCircleView,"translationX",0,dip2px(mTranslationDistance));
        AnimatorSet outAnimatorSet = new AnimatorSet();
        outAnimatorSet.playTogether(leftTranslationAnimator,rightTranslationAnimator);
        outAnimatorSet.setDuration(ANIMATION_TIME).setInterpolator(new DecelerateInterpolator());
        outAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startInnerAnimation();
            }
        });
        outAnimatorSet.start();
    }
    private void startInnerAnimation() {
        ObjectAnimator leftTranslationAnimator = ObjectAnimator.ofFloat(leftCircleView, "translationX", -dip2px(mTranslationDistance), 0);
        final ObjectAnimator rightTranslationAnimator = ObjectAnimator.ofFloat(rightCircleView,"translationX",dip2px(mTranslationDistance),0);
        AnimatorSet outAnimatorSet = new AnimatorSet();
        outAnimatorSet.playTogether(leftTranslationAnimator,rightTranslationAnimator);
        outAnimatorSet.setDuration(ANIMATION_TIME).setInterpolator(new DecelerateInterpolator());
        outAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                int midCircleColor = midCircleView.getCircleColor();
                int rightCircleColor = rightCircleView.getCircleColor();
                int leftCircleColor = leftCircleView.getCircleColor();
                midCircleView.setCircleColor(rightCircleColor);
                rightCircleView.setCircleColor(leftCircleColor);
                leftCircleView.setCircleColor(midCircleColor);
                startOutAnimation();
            }
        });
        outAnimatorSet.start();
    }

    public CircleView getCircleView(Context context) {
        CircleView circleView = new CircleView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dip2px(10),dip2px(10));
        params.addRule(CENTER_IN_PARENT);
        circleView.setLayoutParams(params);
        return circleView;
    }
    private int  dip2px(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getResources().getDisplayMetrics());
    }
}
