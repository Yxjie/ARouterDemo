package com.example.yangxiangjie.arouterdemo.testservice;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.DegradeService;

/**
 * Created by yangxiangjie on 2017/12/8.
 * 自定义全局降级策略
 */

@Route(path = "/xxx/xxx")
public class DegradeServiceImpl implements DegradeService {


    @Override
    public void onLost(Context context, Postcard postcard) {
        //do something

        Log.d("yxjie", "DegradeServiceImpl = = = onLost");

//        if (postcard != null) {
//            Toast.makeText(context, "Path = " + postcard.getPath() + " Group = " + postcard.getGroup(), Toast.LENGTH_SHORT).show();
//            Log.d("DegradeServiceImpl", "Path = " + postcard.getPath() + " Group = " + postcard.getGroup());
//        }else {
//            Toast.makeText(context, "Postcard 为空", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void init(Context context) {
    }
}
