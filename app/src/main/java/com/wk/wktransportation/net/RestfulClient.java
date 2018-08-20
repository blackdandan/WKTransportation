package com.wk.wktransportation.net;

import android.util.Log;

import com.wk.wktransportation.entity.CarInfo;
import com.wk.wktransportation.entity.Customer;
import com.wk.wktransportation.entity.Tempertumer;
import com.wk.wktransportation.entity.Worker;
import com.wk.wktransportation.net.service.CarInfoService;
import com.wk.wktransportation.net.service.CustomerService;
import com.wk.wktransportation.net.service.TempertumerService;
import com.wk.wktransportation.net.service.WorkerService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @discription 描述这个类的作用
 * <p>
 * **********************************
 * @auth @yangzehui
 * @email @784818984@qq.com
 * @date @2018/7/19 22:01
 * **********************************
 **/
public class RestfulClient {
    private static final String TAG = "RestfulClient";
    private static final String BASE_URL = "http://192.168.31.227:8080/";
    private static RestfulClient instance = null;
    private Retrofit retrofit;
    public static RestfulClient getInstance() {
        if (instance == null){
            synchronized (RestfulClient.class){
                if (instance == null){
                    instance = new RestfulClient();
                }
            }
        }
        return instance;
    }
    private RestfulClient(){
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(BASE_URL);
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
        retrofit = retrofitBuilder.build();
    }
    public List<CarInfo> getCarInfo(String carNumber){
        Log.d(TAG, "do==== RestfulClient getCarInfo:carNumber:"+carNumber);
        Log.d(TAG, "do==== RestfulClient getCarInfo:"+retrofit.baseUrl());
        Call<List<CarInfo>> call =  retrofit.create(CarInfoService.class).getAllCarInfo(carNumber);
        try {
            Response<List<CarInfo>> response =  call.execute();
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Customer> getCustomer(String hospitalName){
        Log.d(TAG, "do==== RestfulClient getCustomer:");
        Log.d(TAG, "do==== RestfulClient getCustomer:"+retrofit.baseUrl());
        Call<List<Customer>> call = retrofit.create(CustomerService.class).getAllCustomer(hospitalName);
        try {
            Response<List<Customer>> response = call.execute();
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Worker> getAllWorker(){
        Call<List<Worker>> call = retrofit.create(WorkerService.class).findAllWorker();
        try {
            Response<List<Worker>> response = call.execute();
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<String > getAllBoxType(){
        Call<List<String>> call = retrofit.create(TempertumerService.class).getAllBoxType();
        try {
            Response<List<String>> response = call.execute();
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Tempertumer> selectTempertumer(String end,String start, String number){
        Call<List<Tempertumer>> call = retrofit.create(TempertumerService.class).selectTempertumer(end, start, number);
        try {
            Response<List<Tempertumer>> response = call.execute();
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
