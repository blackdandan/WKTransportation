package com.wk.wktransportation.ui.temperature;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.wk.wktransportation.R;
import com.wk.wktransportation.database.CarHandler;
import com.wk.wktransportation.entity.Tempertumer;
import com.wk.wktransportation.gprinter.DeviceConnFactoryManager;
import com.wk.wktransportation.gprinter.GprinterHelper;
import com.wk.wktransportation.net.RestfulClient;
import com.wk.wktransportation.rxbus.Event;
import com.wk.wktransportation.ui.BaseActivity;
import com.wk.wktransportation.ui.adapter.TemperatureAdapter;
import com.wk.wktransportation.ui.bluetooth.BluetoothDeviceList;
import com.wk.wktransportation.util.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @discription 描述这个类的作用
 * <p>
 * **********************************
 * @auth @yangzehui
 * @email @784818984@qq.com
 * @date @2018/8/22 19:38
 * **********************************
 **/
public class TemperatureActivity extends BaseActivity{
    private boolean isConnected = false;
    private static final String TAG = "TemperatureActivity";
    /**
     * 探针还是车
     */
    public static final String TYPE = "type";
    /**
     * 车
     */
    public static final int TYPE_CAR = 1;
    /**
     * 探针
     */
    public static final int TYPE_INCUBATOR = 2;
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String CUSTOMER = "customer";
    public static final String NUMBER = "number";

    private int type = 0;
    private String number;
    private String mStartTime;
    private String mEndTime;
    private String customer;

    private ListView temperatureList;
    private Button printButton;
    private String incubatorNumber1;
    private String incubatorNumber2;
    private List<Tempertumer> result = new ArrayList<>();
    private float maxValue = 0f;
    private float minValue = 0f;
    @Override
    public void onNotification(Event event) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        getData();
        initIncubatorNumber();
        initView();
        getTemperature(incubatorNumber1,incubatorNumber2);

    }

    private void initIncubatorNumber() {
        if (type == TYPE_CAR){
            List<String> incubators = CarHandler.getInstance().getIncubatorByCarNumber(number);
            Log.d(TAG, "do==== TemperatureActivity initIncubatorNumber:"+incubators);
            if (incubators.size()>=2){
                incubatorNumber1 = incubators.get(0);
                incubatorNumber2 = incubators.get(1);
            }
        }
        if (type == TYPE_INCUBATOR){
            incubatorNumber1 = number;
        }
    }

    private void initView() {
        temperatureList = findViewById(R.id.list_temperature);
        printButton = findViewById(R.id.btn_print);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected){
                    Intent intent = new Intent(TemperatureActivity.this,BluetoothDeviceList.class);
                    startActivityForResult(intent, Constant.BLUETOOTH_REQUEST_CODE);
                }else {
                    if (result.size()==0){
                        Toast.makeText(TemperatureActivity.this, "NO Data", Toast.LENGTH_SHORT).show();
                    }
                    String iNumber = incubatorNumber1;
                    if (type == TYPE_CAR){
                        iNumber = result.get(0).getIncubatornumber();
                        iNumber = iNumber.substring(0,2);
                    }
                    GprinterHelper.print(id,mStartTime,result,customer,type,number,iNumber,maxValue,minValue);
                }

            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void getTemperature(final String incubatorNumber1, final String incubatorNumber2){
        result.clear();
        new AsyncTask<Void, Void, List<Tempertumer>>() {
            @Override
            protected List<Tempertumer> doInBackground(Void... voids) {
                List<Tempertumer> list = new ArrayList<>();
                Tempertumer tempertumer = new Tempertumer();
                tempertumer.setDatetime(null);
                tempertumer.setIncubatornumber(incubatorNumber1);
                list.add(tempertumer);

                List<Tempertumer> list1 = RestfulClient.getInstance().selectTempertumer(mEndTime,mStartTime,incubatorNumber1);
                list.addAll(list1);
                if (!TextUtils.isEmpty(incubatorNumber2)){
                    Tempertumer tempertumer2 = new Tempertumer();
                    tempertumer2.setDatetime(null);
                    tempertumer2.setIncubatornumber(incubatorNumber1);
                    list.add(tempertumer2);
                    List<Tempertumer> list2 = RestfulClient.getInstance().selectTempertumer(mEndTime,mStartTime,incubatorNumber2);
                    list.addAll(list2);
                }
                getMaxMinValue(list);
                return list;
            }

            @Override
            protected void onPostExecute(List<Tempertumer> tempertumers) {
                super.onPostExecute(tempertumers);
                TemperatureAdapter adapter = new TemperatureAdapter(TemperatureActivity.this);
                adapter.setData(tempertumers);
                temperatureList.setAdapter(adapter);
                result.addAll(tempertumers);
            }
        }.execute();
    }

    private void getMaxMinValue(List<Tempertumer> list) {
        List<Float> floats = new ArrayList<>();
        Log.d(TAG, "do==== TemperatureActivity getMaxMinValue:list.size:"+list.size());
        for (Tempertumer tempertumer :list){
            Float temp = -100f;
            try{
                if (tempertumer.getTempertumer()!=null){
                    temp = Float.parseFloat(tempertumer.getTempertumer());
                }

            }catch (NumberFormatException e){
                e.printStackTrace();
            }
            if (temp!= -100f)
            floats.add(temp);
        }
        if (floats.size()>1){
            maxValue = Collections.max(floats);
            minValue = Collections.min(floats);
        }

    }

    private void getData() {
        type = getIntent().getIntExtra(TYPE,0);
        mStartTime = getIntent().getStringExtra(START_TIME);
        mEndTime = getIntent().getStringExtra(END_TIME);
        customer = getIntent().getStringExtra(CUSTOMER);
        number = getIntent().getStringExtra(NUMBER);
    }
    /**
     * 判断打印机所使用指令是否是ESC指令
     */
    private int id = 0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                /*蓝牙连接*/
                case Constant.BLUETOOTH_REQUEST_CODE: {
                    /*获取蓝牙mac地址*/
                    String macAddress = data.getStringExtra(BluetoothDeviceList.EXTRA_DEVICE_ADDRESS);
                    //初始化话DeviceConnFactoryManager
                    new DeviceConnFactoryManager.Build()
                            .setId(id)
                            //设置连接方式
                            .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH)
                            //设置连接的蓝牙mac地址
                            .setMacAddress(macAddress)
                            .build();
                    //打开端口
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
                    isConnected = true;
                    break;
                }
                default:
                    break;
            }
        }
    }
}
