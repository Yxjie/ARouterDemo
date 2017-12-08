package com.example.yangxiangjie.arouterdemo.testservice;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by yangxiangjie on 2017/12/8.
 */

public interface HelloService extends IProvider {

    void sayHello(String msg);

}
