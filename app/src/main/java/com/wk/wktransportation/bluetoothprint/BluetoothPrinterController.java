package com.wk.wktransportation.bluetoothprint;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.wk.wktransportation.App;

import java.util.HashSet;
import java.util.Set;

/**
 * @discription 自动管理蓝牙
 * 调用 {@link #printData}保存数据在内存，开始初始化蓝牙，扫描，如果扫描到的包含常用的，就直接打印数据
 *                        如果没有发现常用设备，就展示扫描到的设备给别的类
 *                        此时用户可以选择要使用的设备来打印 刚刚保存的数据
 *                        并询问是否将此设备保存到常用列表
 * <p>
 * **********************************
 * @auth @yangzehui
 * @email @784818984@qq.com
 * @date @2018/7/4 21:04
 * **********************************
 **/
public class BluetoothPrinterController {
    private static final String TAG = "BluetoothController";
    /**
     * 经常使用的打印机
     */
    private static final String BLUETOOTH_SP = "bluetooth";
    private static final String LIST_DEVICES_SP_KEY = "list_printer_device";
    private static final SharedPreferences bluetoothDeviceSharedPreference = App.getApplication().getSharedPreferences(BLUETOOTH_SP, Context.MODE_PRIVATE);
    private static BluetoothConnector bluetoothConnector = BluetoothConnector.getInstance(App.getApplication());
    private static BluetoothConnector.ScanResultReceiver printerReceiver;
    private static Set<BluetoothDevice> scannedDevices = new HashSet<>();
    private static byte[] printData = null;
    private static final BluetoothConnector.ScanResultReceiver privateReceiver = new BluetoothConnector.ScanResultReceiver() {
        @Override
        public void onDeviceDetection(BluetoothDevice device) {
            scannedDevices.add(device);
        }

        @Override
        public void onScanFinished() {
            for (BluetoothDevice device: scannedDevices) {
                printerReceiver.onDeviceDetection(device);
            }
        }
    };
    public static void startScan(){
        if (!bluetoothConnector.isBluetoothInitialized()){
            bluetoothConnector.initBluetooth();
        }
        addDeviceScanResultReceiver();
        bluetoothConnector.startScan();

    }
    public static void stopScan(){
        bluetoothConnector.stopScan();
        removeDeviceScanResultReceiver();
    }

    private static Set<String> getSavedDevices(){
        Set<String> hashSet = null;
        hashSet = bluetoothDeviceSharedPreference.getStringSet(LIST_DEVICES_SP_KEY,new HashSet<String>());
        return hashSet;
    }
    private static void saveDevice(BluetoothDevice device){
        Set<String> deviceSet = getSavedDevices();
        deviceSet.add(device.getAddress());
    }
    public static void printData(byte[] data){
        printData = data;
        startScan();
    }
    public static void connectDefaultDeviceAndPrint(){
        Set<String> savedDevices = getSavedDevices();
        for (BluetoothDevice device : scannedDevices) {
            if (savedDevices.contains(device.getAddress())) {
                if (printData != null) {
                    connectDeviceAndPrint(device);
                } else {
                    Log.e(TAG, "do====BluetoothPrinterController.onScanFinished.: print data == null ");
                    return;
                }
                Log.e(TAG, "do====BluetoothPrinterController.onScanFinished.: no saved device ");
                break;
            }
        }
    }
    public static void connectDeviceAndPrint(BluetoothDevice device){

        if (printData == null){
            Log.e(TAG, "do====BluetoothPrinterController.connectDeviceAndPrint.: printdata == null ");
            return;
        }
        bluetoothConnector.connect(device);
        if (bluetoothConnector.isConnected()){
            bluetoothConnector.sendData(printData);
        }
    }
    public static void setDeviceScanResultReceiver(BluetoothConnector.ScanResultReceiver resultReceiver){
        printerReceiver = resultReceiver;
    }
    private static void addDeviceScanResultReceiver(){
        bluetoothConnector.addDeviceScanReceiver(privateReceiver);
    }
    private static void removeDeviceScanResultReceiver(){
        bluetoothConnector.addDeviceScanReceiver(privateReceiver);
    }


}
