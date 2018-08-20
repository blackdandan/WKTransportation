package com.wk.wktransportation.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wk.wktransportation.rxbus.RxBus;

/**
 * @discription 描述这个类的作用
 * <p>
 * **********************************
 * @auth @yangzehui
 * @email @784818984@qq.com
 * @date @2018/7/13 22:50
 * **********************************
 **/
public abstract class BaseActivity extends AppCompatActivity implements RxBus.Subscriber{
    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        RxBus.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.unRegister(this);
    }
}
