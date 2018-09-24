package com.wk.wktransportation.net.service;

import com.wk.wktransportation.entity.TempBox;

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
 * @date @2018/8/22 23:25
 * **********************************
 **/
public interface TempBoxService {
    @GET("tempBox/{number}")
    Call<List<TempBox>> getTempBox(@Path("number") String number);
}
