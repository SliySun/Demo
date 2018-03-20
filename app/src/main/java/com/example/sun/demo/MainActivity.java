package com.example.sun.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List testList;
    int count;
    private ViewFlipper flipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flipper = findViewById(R.id.flipper);
        testList = new ArrayList();
        addData();
        count = testList.size();
        for (int i = 0; i < count; i++) {
            final View ll_content = View.inflate(MainActivity.this, R.layout.item_flipper, null);
            TextView tv_content = (TextView) ll_content.findViewById(R.id.tv_content);
            ImageView iv_closebreak = (ImageView) ll_content.findViewById(R.id.iv_closebreak);
            tv_content.setText(testList.get(i).toString());
            iv_closebreak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //对当前显示的视图进行移除
                    flipper.removeView(ll_content);
                    count--;
                    //当删除后仅剩 一条 新闻时，则取消滚动
                    if (count == 1) {
                        flipper.stopFlipping();
                    }
                }
            });
            flipper.addView(ll_content);
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void addData() {
        testList.add(0,"爸妈爱的“白”娃娃，真是孕期吃出来的吗？");
        testList.add(1,"如果徒步真的需要理由，十四个够不够？");
        testList.add(2, "享受清爽啤酒的同时，这些常识你真的了解吗？");
        testList.add(3, "三星Galaxy S8定型图无悬念");
        testList.add(4, "家猫为何如此高冷？");


    }
}
