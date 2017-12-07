package com.kx.admin.day18;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private LoveLayout loveLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loveLayout = findViewById(R.id.loveLayout);
    }

    public void addLove(View view) {
        loveLayout.addLove();
    }
}
