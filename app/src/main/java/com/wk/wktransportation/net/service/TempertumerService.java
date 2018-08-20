package com.wk.wktransportation.net.service;

import com.wk.wktransportation.entity.Tempertumer;

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
 * @date @2018/8/13 21:13
 * **********************************
 **/
public interface TempertumerService {
    @GET("tempertumer/temperinfo")
    Call<List<String>> getAllBoxType();
    @GET("tempertumer/{number}/{start}/{end}")
    Call<List<Tempertumer>> selectTempertumer(@Path("end") String end, @Path("start") String stert, @Path("number") String number);
}
