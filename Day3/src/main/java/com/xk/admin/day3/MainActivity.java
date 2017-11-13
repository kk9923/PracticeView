package com.xk.admin.day3;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class MainActivity extends AppCompatActivity {

    private ObjectAnimator objectAnimator;
    private Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            loadingView.changeShape();
            handler.postDelayed(runnable,500);
        }
    };
    private LoadingView58 loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QQStepView2 view = (QQStepView2) findViewById(R.id.view);
        loadingView = (LoadingView58) findViewById(R.id.loadingView);
        objectAnimator = ObjectAnimator.ofInt(view, "progress", 0, 100).setDuration(3000);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();

        TabView tabView = (TabView) findViewById(R.id.tabView);



    }
    public void reShow(View view){
        handler.postDelayed(runnable,500);
    }

}
