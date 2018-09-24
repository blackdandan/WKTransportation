package com.wk.wktransportation.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.wk.wktransportation.App;
import com.wk.wktransportation.R;
import com.wk.wktransportation.gprinter.GprinterHelper;
import com.wk.wktransportation.rxbus.Event;
import com.wk.wktransportation.ui.BaseActivity;
import com.wk.wktransportation.ui.home.MainActivity;
import com.wk.wktransportation.ui.selectpage.SelectActivity;
import com.wk.wktransportation.ui.selecttypepage.SelectTypePage;
import com.wk.wktransportation.util.ThreadTool;

import java.util.concurrent.TimeUnit;

/**
 * @discription 描述这个类的作用
 * <p>
 * **********************************
 * @auth @yangzehui
 * @email @784818984@qq.com
 * @date @2018/7/13 22:52
 * **********************************
 **/
public class SplashActivity extends BaseActivity{
    private static final String TAG = "SplashActivity";
    private boolean isStop = false;

    /**
     * start activity task
     */
    private Runnable startMainActivity = new Runnable() {
        @Override
        public void run() {
            if (isStop)return;
            Intent intent = new Intent(SplashActivity.this, SelectTypePage.class);
            startActivity(intent);
            finish();
        }
    };
    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ThreadTool.SCHEDULED_SERVICE.schedule(startMainActivity,3, TimeUnit.SECONDS);
    }

    @Override
    protected void onStop() {
        isStop = true;
        super.onStop();
    }

    @Override
    public void onNotification(Event event) {

    }
}
