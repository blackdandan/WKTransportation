package com.wk.wktransportation.net.service;


import com.wk.wktransportation.entity.Worker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @discription 描述这个类的作用
 * <p>
 * **********************************
 * @auth @yangzehui
 * @email @784818984@qq.com
 * @date @2018/8/13 21:08
 * **********************************
 **/
public interface WorkerService {
    @GET("worker")
    Call<List<Worker>> findAllWorker();
}
