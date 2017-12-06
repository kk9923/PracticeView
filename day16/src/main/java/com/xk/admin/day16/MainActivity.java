package com.xk.admin.day16;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.xk.admin.day16.view.DropDownMenu;
import com.xk.admin.day16.view.ListScreenMenuAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView  =  new TextView(this);
        textView.setText("我是内容");
        DropDownMenu dropDownView = findViewById(R.id.dropDownView);
        dropDownView.setAdapter(new ListScreenMenuAdapter(this),textView);
    }
}
