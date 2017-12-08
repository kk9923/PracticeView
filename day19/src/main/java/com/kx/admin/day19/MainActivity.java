package com.kx.admin.day19;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kx.admin.day19.view.ParallaxViewPager;

public class MainActivity extends AppCompatActivity {
    private int [] layOutIds = {R.layout.fragment_page_first,R.layout.fragment_page_second,R.layout.fragment_page_third};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParallaxViewPager parallax_vp = findViewById(R.id.parallax_vp);
       parallax_vp.setLayoutIds(getSupportFragmentManager(),layOutIds);
    }
}
