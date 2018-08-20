package com.wk.wktransportation.net.service;

import com.wk.wktransportation.entity.Customer;

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
 * @date @2018/8/13 20:54
 * **********************************
 **/
public interface CustomerService {
    @GET("customer/{hospitalName}")
    Call<List<Customer>> getAllCustomer(@Path("hospitalName") String hospitalName);
}
