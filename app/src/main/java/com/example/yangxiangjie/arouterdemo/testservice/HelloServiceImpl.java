package com.example.yangxiangjie.arouterdemo.testservice;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;

/**
 * Created by yangxiangjie on 2017/12/8.
 */
@Route(path = "/service/hello")
public class HelloServiceImpl implements HelloService {

    private Context mContext;

    @Override
    public void sayHello(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}
