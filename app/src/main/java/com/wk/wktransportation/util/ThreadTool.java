package com.wk.wktransportation.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import io.reactivex.schedulers.Schedulers;

/**
 * @discription 描述这个类的作用
 * <p>
 * **********************************
 * @auth @yangzehui
 * @email @784818984@qq.com
 * @date @2018/7/13 22:55
 * **********************************
 **/
public class ThreadTool {
    public static final ExecutorService SINGLE_SERVICE = Executors.newSingleThreadExecutor();
    public static final ScheduledExecutorService SCHEDULED_SERVICE = Executors.newScheduledThreadPool(1);
}
