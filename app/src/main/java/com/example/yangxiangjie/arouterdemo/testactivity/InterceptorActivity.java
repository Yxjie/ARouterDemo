package com.example.yangxiangjie.arouterdemo.testactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.yangxiangjie.arouterdemo.R;

@Route(path = "/test/interceptor")
public class InterceptorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interceptor);

        if (getIntent() != null) {
            String extra = getIntent().getStringExtra("extra");
            if (!TextUtils.isEmpty(extra)) {
                ((TextView) findViewById(R.id.txt_extra)).setText(extra);
            }
        }
    }
}
