package com.example.yangxiangjie.arouterdemo.interceptor;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

/**
 * Created by yangxiangjie on 2017/12/5.
 * 声明拦截器(拦截跳转过程，面向切面编程)
 */
@Interceptor(priority = 8, name = "测试用拦截器")
public class TestInterceptor implements IInterceptor {

    private Context mContext;

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        if (TextUtils.equals("/test/interceptor", postcard.getPath())) {
            //此方法不在UI线程
            Log.d("TestInterceptor", "路径/test/interceptor 被拦截了");
            postcard.withString("extra", "我是 测试用拦截器 添加的拦截数据");
            callback.onContinue(postcard);
        } else {
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}
