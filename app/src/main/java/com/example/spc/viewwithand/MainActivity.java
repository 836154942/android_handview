package com.example.spc.viewwithand;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    int loacl[] = {0, 0};//初始坐标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewWithHand view = ViewWithHand.getCreatView(this);
        view.setLocation(loacl);
        view.addViewToScreen();
    }
}
