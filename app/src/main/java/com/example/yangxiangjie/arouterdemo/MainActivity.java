package com.example.yangxiangjie.arouterdemo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.yangxiangjie.arouterdemo.testservice.HelloService;
import com.example.yangxiangjie.arouterdemo.testservice.HelloServiceImpl;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_simple_jump).setOnClickListener(this);
        findViewById(R.id.btn_jump_with_data).setOnClickListener(this);
        findViewById(R.id.btn_jump_with_data_1).setOnClickListener(this);
        findViewById(R.id.btn_jump_by_uri).setOnClickListener(this);
        findViewById(R.id.btn_jump_webview_by_uri).setOnClickListener(this);
        findViewById(R.id.btn_interceptor).setOnClickListener(this);

        findViewById(R.id.btn_by_name).setOnClickListener(this);
        findViewById(R.id.btn_by_type).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_simple_jump:
                //简单跳转逻辑
                //navigation的第一个参数必须是Activity，第二个参数则是RequestCode
                ARouter.getInstance().build("/test/activity1").navigation(
                        v.getContext(), new NavCallback() {
                            @Override
                            public void onArrival(Postcard postcard) {
                                //界面到达
                                Log.d(TAG, "MainActivity == onArrival  postcard.getPath = " + postcard.getPath());
                            }

                            @Override
                            public void onInterrupt(Postcard postcard) {
                                super.onInterrupt(postcard);
                                //界面被拦截
                                Log.d(TAG, "MainActivity == onInterrupt");

                            }

                            @Override
                            public void onFound(Postcard postcard) {
                                super.onFound(postcard);
                                //界面找到
                                Log.d(TAG, "MainActivity == onFound");

                            }

                            @Override
                            public void onLost(Postcard postcard) {
                                super.onLost(postcard);
                                //界面未找到【开启debug模式会有Toast提示】
                                Log.d(TAG, "MainActivity == onLost");

                            }
                        }
                );
                break;

            case R.id.btn_jump_with_data:
                //带值跳转界面[传统intent方式]
//                Bundle bundle = new Bundle();
                ARouter.getInstance().build("/test/activity1")
                        .withString("key1", "Test")
                        .withInt("key2", 27)
//                        .with(bundle)
//                        .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .navigation();
                break;


            case R.id.btn_jump_with_data_1:
                //带值跳转界面[ARouter依赖注入方式]
                ARouter.getInstance().build("/test/activity1")
                        .withString("name", "LiNa")
                        .withInt("age", 26)
                        .withString("friend", "yxjie").navigation();

                break;

            case R.id.btn_jump_by_uri:
                //通过Uri跳转传值
                Uri uri = Uri.parse("arouter://test.yxjie.com/test/activity1");
                ARouter.getInstance().build(uri)
                        .withString("key1", "我是Uri过来的")
                        .withInt("key2", 2).navigation();
                break;

            case R.id.btn_jump_webview_by_uri:
                //通过Uri加载本地Html
                Uri path = Uri.parse("arouter://test.yxjie.com/test/webview");
                ARouter.getInstance().build(path)
                        .withString("url", "file:///android_asset/schame-test.html")
//                        .withString("url","http://www.baidu.com")
                        .navigation();
                break;

            case R.id.btn_interceptor:
                //测试拦截器
                ARouter.getInstance().build("/test/interceptor")
                        .navigation(v.getContext(), new NavCallback() {
                            @Override
                            public void onArrival(Postcard postcard) {
                                Log.d(TAG, "onArrival running...");
                            }

                            @Override
                            public void onInterrupt(Postcard postcard) {
                                super.onInterrupt(postcard);
                                Log.d("MainActivity", "被拦截器 拦截了");
                            }
                        });
                break;

            case R.id.btn_by_name:
                ((HelloServiceImpl) ARouter.getInstance().build("/service/hello").navigation()).sayHello("ByName调用服务");
                break;

            case R.id.btn_by_type:
                ARouter.getInstance().navigation(HelloService.class).sayHello("ByType调用服务");
                break;

            default:
                break;
        }
    }
}
