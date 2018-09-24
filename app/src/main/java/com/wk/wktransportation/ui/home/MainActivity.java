package com.wk.wktransportation.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wk.wktransportation.R;
import com.wk.wktransportation.entity.CarInfo;
import com.wk.wktransportation.entity.Customer;
import com.wk.wktransportation.gprinter.DeviceConnFactoryManager;
import com.wk.wktransportation.gprinter.GprinterHelper;
import com.wk.wktransportation.net.RestfulClient;
import com.wk.wktransportation.rxbus.Event;
import com.wk.wktransportation.ui.BaseActivity;
import com.wk.wktransportation.ui.bluetooth.BluetoothDeviceList;
import com.wk.wktransportation.ui.selectpage.SelectActivity;
import com.wk.wktransportation.util.Constant;
import com.wk.wktransportation.util.ThreadTool;

import java.util.List;

public class MainActivity extends BaseActivity{
    private static final String TAG = "MainActivity";
    /**
     * 判断打印机所使用指令是否是ESC指令
     */
    private int id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onNotification(Event event) {

    }

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
                    break;
                }
                default:
                    break;
            }
        }
    }

    public void onBluetoothClick(View view) {
        Intent intent = new Intent(this,BluetoothDeviceList.class);
        startActivityForResult(intent,Constant.BLUETOOTH_REQUEST_CODE);
    }

    public void onPrintClick(View view) {
    }

    public void onGetCarClick(View view) {
        ThreadTool.SINGLE_SERVICE.submit(new Runnable() {
            @Override
            public void run() {
                List<CarInfo> list =  RestfulClient.getInstance().getCarInfo("");
                System.out.println("do====1");
                for (CarInfo carInfo:list){
                    Log.d(TAG, "do==== MainActivity run:"+carInfo.getCarnumber());
                }
                List<Customer> customers = RestfulClient.getInstance().getCustomer("");
                Log.d(TAG, "do==== MainActivity run:2");

                for (Customer customer:
                     customers) {
                    Log.d(TAG, "do==== MainActivity run:customer:"+customer.getHospitalname());
                }
            }
        });
    }

    public void onSelectClick(View view) {
        Intent intent = new Intent(this, SelectActivity.class);
        startActivity(intent);
    }
}
