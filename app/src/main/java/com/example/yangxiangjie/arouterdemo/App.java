package com.example.yangxiangjie.arouterdemo;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by yangxiangjie on 2017/12/5.
 */

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        initARouter();
    }

    /**
     * 初始化ARouter
     */
    private void initARouter() {
        if (BuildConfig.DEBUG) {
            // 打印日志
            ARouter.openLog();
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
            // 打印日志的时候打印线程堆栈
            ARouter.printStackTrace();
        }
        ARouter.init(this);
    }

}


