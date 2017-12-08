package com.example.yangxiangjie.arouterdemo.testactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.yangxiangjie.arouterdemo.R;

/**
 * 测试WebView
 */
@Route(path = "/test/webview")
public class TestWebViewActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_web_view);
        mWebView = findViewById(R.id.web_view);
        if (getIntent() != null) {
            String url = getIntent().getStringExtra("url");
            mWebView.loadUrl(url);
        }
    }
}
