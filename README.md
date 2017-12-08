## ARouter 简单实用

### 详细的API说明
```
// 构建标准的路由请求
ARouter.getInstance().build("/home/main").navigation();

// 构建标准的路由请求，并指定分组
ARouter.getInstance().build("/home/main", "ap").navigation();

// 构建标准的路由请求，通过Uri直接解析
Uri uri;
ARouter.getInstance().build(uri).navigation();

// 构建标准的路由请求，startActivityForResult
// navigation的第一个参数必须是Activity，第二个参数则是RequestCode
ARouter.getInstance().build("/home/main", "ap").navigation(this, 5);

// 直接传递Bundle
Bundle params = new Bundle();
ARouter.getInstance()
	.build("/home/main")
	.with(params)
	.navigation();

// 指定Flag
ARouter.getInstance()
	.build("/home/main")
	.withFlags();
	.navigation();

// 获取Fragment
Fragment fragment = (Fragment) ARouter.getInstance().build("/test/fragment").navigation();
					
// 对象传递
ARouter.getInstance()
	.withObject("key", new TestObj("Jack", "Rose"))
	.navigation();

// 觉得接口不够多，可以直接拿出Bundle赋值
ARouter.getInstance()
	    .build("/home/main")
	    .getExtra();

// 转场动画(常规方式)
ARouter.getInstance()
    .build("/test/activity2")
    .withTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
    .navigation(this);

// 转场动画(API16+)
ActivityOptionsCompat compat = ActivityOptionsCompat.
    makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);

// ps. makeSceneTransitionAnimation 使用共享元素的时候，需要在navigation方法中传入当前Activity

ARouter.getInstance()
	.build("/test/activity2")
	.withOptionsCompat(compat)
	.navigation();
        
// 使用绿色通道(跳过所有的拦截器)
ARouter.getInstance().build("/home/main").greenChannel().navigation();

// 使用自己的日志工具打印日志
ARouter.setLogger();
```

详细介绍请跳转[ARouter GitHub官方源码地址](https://github.com/alibaba/ARouter)

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
3.添加ARouter混淆
```
#ARouter混淆
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider
```
### ARouter页面简单跳转以及传值跳转
a.简单跳转：
```
 @Route(path = "/test/activity1")//路由路径
 public class TestActivity1 extends AppCompatActivity {
            ......
 }
 
 ARouter.getInstance().build("/test/activity1")..navigation();
```

b.传值跳转
[支持基本数据类型，bundle，以及对象]
```
 @Route(path = "/test/activity1")//路由路径
 public class TestActivity1 extends AppCompatActivity {
            @Override
               protected void onCreate(Bundle savedInstanceState) {
                   super.onCreate(savedInstanceState);
                   setContentView(R.layout.activity_test1);
                   if (getIntent() != null) {
                       //带值传递跳转[跳转方法二]
                       TextView textView = findViewById(R.id.txt_test1);
                       String value1 = getIntent().getStringExtra("key1");
                       int value2 = getIntent().getIntExtra("key2", 0);
           
                       if (!TextUtils.isEmpty(value1) || value2 != 0) {
                           textView.setText("传过来的值是：" + "value1 = " + value1 + " ；value2 = " + value2);
                       }
                   }
               }
 }


/ Bundle bundle = new Bundle();
  ARouter.getInstance().build("/test/activity1")
         .withString("key1", "Test")
         .withInt("key2", 27)
//       .with(bundle)
//       .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
         .navigation();
```
>navigation简单介绍
两个参数navigation有两个：<br/>
一是：navigation(Context context, NavigationCallback callback)；<br>
二是：navigation(Activity mContext, int requestCode)；
这两种方式，第一个参数是上下文，第二个参数NavigationCallback或requestCode

NavigationCallback是监听回调方法，我们通过代码理解一下：
```
NavCallback  cal = new NavCallback() {
      @Override
      public void onArrival(Postcard postcard) {
              //界面跳转完毕
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
### ARouter通过URL实现界面跳转以及数据传递
1.创建用于监听Schame事件的SchemeFilterActivity,之后直接把url传递给ARouter即可
```
public class SchemeFilterActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        ARouter.getInstance().build(uri).navigation(this, new NavCallback() {
            @Override
            public void onArrival(Postcard postcard) {
                finish();
            }
        });
    }
}
```
2.在AndroidManifest.xml中注册该Activity,并配置该Activity的host，scheme值
```
 <!-- 通过Url跳转 -->
        <activity android:name=".SchemeFilterActivity">
            <intent-filter>
                <data
                    android:host="test.yxjie.com"
                    android:scheme="arouter"/>

                <action android:name="android.intent.action.VIEW"/>

                <category android:name="android.intent.category.DEFAULT"/>
                <category android:name="android.intent.category.BROWSABLE"/>

                <!--html 里面的App Link-->
                <data
                    android:host="test.yxjie.com"
                    android:scheme="http"/>
            </intent-filter>
        </activity>
```
3.ARouter通过url方式进行界面跳转：<br>
a.原生界面跳转【规则：scheme://host/path】<br>
```
 //通过Uri跳转传值
 Uri uri = Uri.parse("arouter://test.yxjie.com/test/activity1");
 ARouter.getInstance().build(uri)
         .withString("key1", "我是Uri过来的")
         .withInt("key2", 2).navigation();
```

b.H5界面跳转
```
@Route(path = "/test/activity1")
public class TestActivity1 extends AppCompatActivity {

    @Autowired
    String name;
    @Autowired
    int age;
    @Autowired
    String friend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        //此处一定要加，网上有些教程说可不加 经过验证 不加获取不到数据
        ARouter.getInstance().inject(this);
        if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(friend)) {
                String format = String.format("name = %s, \n age = %s,\n friend = %s", name, age, friend);
                if (!TextUtils.isEmpty(format)) {
                    Log.d("TestActivity1", format);
                }
            }
    }
}

//原生方法调用传值
ARouter.getInstance().build("/test/activity1")
                        .withString("name", "LiNa")
                        .withInt("age", 26)
                        .withString("friend", "yxjie").navigation();
<!--H5界面调用传值-->
<p><a href="arouter://m.aliyun.com/test/activity3?name=alex&age=18&friend=jerry">arouter://m.aliyun.com/test/activity3?name=alex&age=18&friend=jerry</a></p>
<p><a href="http://test.yxjie.com/test/activity1">http://test.yxjie.com/test/activity1</a></p>
```

### 声明拦截器(拦截跳转过程，面向切面编程)
```
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

//拦截器测试类
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

//界面调用跳转拦截
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

```

### 服务管理(一) 暴露服务
```
// 声明接口,其他组件通过接口来调用服务
public interface HelloService extends IProvider {

    void sayHello(String msg);

}

// 实现接口
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

```
### 服务管理(二) 发现服务
```
public class Test {
    @Autowired
    HelloService helloService;

    @Autowired(name = "/service/hello")
    HelloService helloService2;

    HelloService helloService3;

    HelloService helloService4;

    public Test() {
	ARouter.getInstance().inject(this);
    }

    public void testService() {
	 // 1. (推荐)使用依赖注入的方式发现服务,通过注解标注字段,即可使用，无需主动获取
	 // Autowired注解中标注name之后，将会使用byName的方式注入对应的字段，不设置name属性，会默认使用byType的方式发现服务(当同一接口有多个实现的时候，必须使用byName的方式发现服务)
	helloService.sayHello("Vergil");
	helloService2.sayHello("Vergil");

	// 2. 使用依赖查找的方式发现服务，主动去发现服务并使用，下面两种方式分别是byName和byType
	helloService3 = ARouter.getInstance().navigation(HelloService.class);
	helloService4 = (HelloService) ARouter.getInstance().build("/service/hello").navigation();
	helloService3.sayHello("Vergil");
	helloService4.sayHello("Vergil");
    }
}

```
### 自定义全局降级策略
```
// 实现DegradeService接口，并加上一个Path内容任意的注解即可
@Route(path = "/xxx/xxx")
public class DegradeServiceImpl implements DegradeService {
  @Override
  public void onLost(Context context, Postcard postcard) {
	// do something.
  }

  @Override
  public void init(Context context) {

  }
}

```
* 注：单独降级的方式优先于全局降级








