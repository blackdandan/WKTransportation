package com.wk.wktransportation.net.service;

import com.wk.wktransportation.entity.CarInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @discription 描述这个类的作用
 * <p>
 * **********************************
 * @auth @yangzehui
 * @email @784818984@qq.com
 * @date @2018/7/19 22:18
 * **********************************
 **/
public interface CarInfoService {
    @GET("carinfo/{number}")
    Call<List<CarInfo>> getAllCarInfo(@Path("number") String number);
}
