package com.wk.wktransportation;

import android.app.Application;

/**
 * @discription 描述这个类的作用
 * <p>
 * **********************************
 * @auth @yangzehui
 * @email @784818984@qq.com
 * @date @2018/7/4 21:08
 * **********************************
 **/
public class App extends Application{
    private static Application application;

    public static Application getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}
