## ARouter 简单实用

[ARouter GitHub官方源码地址](https://github.com/alibaba/ARouter)

### 项目引用
1.app下的build.gradle添加依赖和配置:
```
android {
    ...
    defaultConfig {
        ...
        javaCompileOptions {
                    annotationProcessorOptions {
                        arguments = [moduleName: project.getName()]
                    }
                }
    }
    
}

dependencies {
     // 替换成最新版本, 需要注意的是api
     // 要与compiler匹配使用，均使用最新版可以保证兼容
     compile 'com.alibaba:arouter-api:1.2.4'
     annotationProcessor 'com.alibaba:arouter-compiler:1.1.4'

}
```

2.初始化SDK【建议在项目Application类里进行初始化】
```
if (BuildConfig.DEBUG) {
            // 打印日志
            ARouter.openLog();
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
            // 打印日志的时候打印线程堆栈
            ARouter.printStackTrace();
        }
ARouter.init(this);
```

### ARouter页面简单跳转以及传值跳转
a.简单跳转：
```
 ARouter.getInstance().build("/test/activity1")..navigation();
```
b.传值跳转
[支持基本数据类型，bundle，以及对象]
```
/ Bundle bundle = new Bundle();
  ARouter.getInstance().build("/test/activity1")
         .withString("key1", "Test")
         .withInt("key2", 27)
//       .with(bundle)
//       .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
         .navigation();
```
>navigation简单介绍
两个参数navigation有两个：<br/>一是：navigation(Context context, NavigationCallback callback)；<br>二是：navigation(Activity mContext, int requestCode)；
这两种方式，第一个参数是上下文，第二个参数NavigationCallback或requestCode

NavigationCallback是监听回调方法，我们通过代码理解一下：
```
NavCallback  cal = new NavCallback() {
      @Override
      public void onArrival(Postcard postcard) {
              //界面到达
              Log.d(TAG, "MainActivity == onArrival  postcard.getPath = "+postcard.getPath());
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
```









