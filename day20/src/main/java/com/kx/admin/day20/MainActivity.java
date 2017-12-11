package com.kx.admin.day20;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kx.admin.day20.view.LoadingView;

public class MainActivity extends AppCompatActivity {
   // private LoadingView
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LoadingView loadingView = findViewById(R.id.loadView);
       mHandler.postDelayed(new Runnable() {
           @Override
           public void run() {
               loadingView.change();
           }
       },5000);
    }
}
