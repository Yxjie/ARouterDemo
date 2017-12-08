package com.example.yangxiangjie.arouterdemo.testactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.yangxiangjie.arouterdemo.R;

@Route(path = "/test/activity1")
public class TestActivity1 extends AppCompatActivity {

    @Autowired
    String name;
    @Autowired
    int age;
    @Autowired
    String boyfriend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        ARouter.getInstance().inject(this);
        if (getIntent() != null) {
            //带值传递跳转[跳转方法二]
            TextView textView = findViewById(R.id.txt_test1);
            String value1 = getIntent().getStringExtra("key1");
            int value2 = getIntent().getIntExtra("key2", 0);

            if (!TextUtils.isEmpty(value1) || value2 != 0) {
                textView.setText("传过来的值是：" + "value1 = " + value1 + " ；value2 = " + value2);
            }


            if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(boyfriend)) {
                String format = String.format("name = %s, \n age = %s,\n boyfriend = %s", name, age, boyfriend);
                if (!TextUtils.isEmpty(format)) {
                    Log.d("TestActivity1", format);
                }
            }


        }

    }
}
