package com.pine.mvp.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by tanghongfeng on 2018/9/28
 */

// atlas特性:
// bundle中的application不是最终在android系统上执行的application，
// 之所以保留bundle application的设计是为了尽最大可能保留大家android的开发习惯。
// bundle中的application类，是在bundle第一次安装的时候，
// atlas会依次调用application的attachBaseContext()和onCreate()函数，
// 可以在里面写一些自己bundle需要用到的初始化代码。
// 注意不要bundle application里用this这种代码，因为是不生效的。
public class MvpApplication extends Application {

    @Override
    public void attachBaseContext(Context baseContext) {
        super.attachBaseContext(baseContext);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
