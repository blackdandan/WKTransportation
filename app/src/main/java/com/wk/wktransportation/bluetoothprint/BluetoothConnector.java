package com.wk.wktransportation.bluetoothprint;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * @discription 用于蓝牙连接和断开,发送数据,扫描, 在同包下建立管理类来管理,确保操作安全
 * <p>
 * **********************************
 * @auth @yangzehui
 * @email @784818984@qq.com
 * @date @2018/7/3 22:38
 * **********************************
 **/
public final class BluetoothConnector {
    private static final String ERROR_BLUTOOTH_MANAGER_NULL = "000";
    private static final String ERROR_BLUETOOTH_ADAPTER_NULL = "001";
    private static final String ERROR_BLUTOOTH_ENABLE_FAIL = "002";
    private static final String TAG = "BluetoothConnector";
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothSocket bluetoothSocket;
    private boolean isEnableBeforeInit = false;
    private Application context;
    private static BluetoothConnector instance;
    private boolean isRegisterDeviceDetectorReceiver;
    private ArrayList<ScanResultReceiver> scanResultReceivers = new ArrayList<>();

    BroadcastReceiver deviceDetectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "do==== BluetoothConnector onReceive:action:"+intent.getAction());
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                BluetoothDevice device = (BluetoothDevice) intent.getExtras().get(BluetoothDevice.EXTRA_DEVICE);
                for (int i = 0;i<scanResultReceivers.size();i++){
                    scanResultReceivers.get(i).onDeviceDetection(device);
                }
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())){
                for (int i = 0;i<scanResultReceivers.size();i++){
                    scanResultReceivers.get(i).onScanFinished();
                }
            }
        }
    };

    static BluetoothConnector getInstance(Application application){
        if (instance == null){
            synchronized (BluetoothConnector.class){
                if (instance == null){
                    instance = new BluetoothConnector(application);
                }
            }
        }
        return instance;
    }
    private BluetoothConnector(Application context){
        this.context = context;
    }

    /**
     * 初始化蓝牙,启动蓝牙
     * @return
     */
    synchronized boolean initBluetooth() {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) {
            Log.e(TAG, "do====BluetoothConnector.connect.bluetoothManager == null: ");
            return false;
        }
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            Log.e(TAG, "do====BluetoothConnector.connect.:bluetoothAdapter == null ");
            return false;
        }
        boolean isEnableSuccess;
        isEnableSuccess = bluetoothAdapter.isEnabled();
        if (isEnableSuccess){
            isEnableBeforeInit = true;
        }else {
            isEnableSuccess = bluetoothAdapter.enable();
        }
        if (!isEnableSuccess) {
            Log.e(TAG, "do====BluetoothConnector.connect.: enable fail ");
            return false;
        }
        return true;
    }

    /**
     * 当前蓝牙是否为可用状态
     * @return
     */
    protected boolean isBluetoothInitialized(){
        if (bluetoothAdapter == null){
            return false;
        }
        if (!bluetoothAdapter.isEnabled()){
            return false;
        }
        return true;
    }
    /**
     * @param device
     */
    public boolean connect(BluetoothDevice device){
        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.randomUUID());
            if (bluetoothSocket!=null){
                bluetoothSocket.connect();
                if (bluetoothSocket.isConnected()){
                    return true;
                }else {
                    return true;
                }
            }else {
                Log.d(TAG, "do==== BluetoothConnector connect: connect fail no reason");
                return false;
            }
        } catch (IOException e) {
            Log.e(TAG, "do====BluetoothConnector.connect.: connect fail exception:"+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 是否连接到了某台设备
     * @return
     */
    protected boolean isConnected(){
        if (bluetoothSocket != null && bluetoothSocket.isConnected()){
            return true;
        }
        return false;
    }

    /**
     * 断开连接
     * 如果初始化之前蓝牙是关闭的,就把蓝牙也关掉
     */
    public void disConnect(){
        if (isConnected()){
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isBluetoothInitialized()){
            if (bluetoothAdapter.isEnabled() && !isEnableBeforeInit){
                bluetoothAdapter.disable();
            }
        }
    }

    /**
     * 获得已经绑定的设备
     * @return
     */
    private Set<BluetoothDevice> getBoundDevices(){
        Log.d(TAG, "do==== BluetoothConnector getBoundDevices:");
        if (bluetoothAdapter == null){
            Log.e(TAG, "do====BluetoothConnector.getBoundDevices.: bluetooth not initialized  ");
            return null;
        }
        Set<BluetoothDevice> bluetoothDevices = bluetoothAdapter.getBondedDevices();
        Iterator<BluetoothDevice> iterator = bluetoothDevices.iterator();
        while (iterator.hasNext()){
            Log.e(TAG, "do====BluetoothConnector.getBoundDevices.: device:" +iterator.next().getName());
        }
        return bluetoothDevices;
    }

    /**
     * 使用这个,记得注册{@link #registerDeviceDetectionBroadcast()}
     * @return
     */
    boolean startScan(){
        if (isBluetoothInitialized()){
            boolean isStart = bluetoothAdapter.startDiscovery();
            if (isStart){
                bluetoothAdapter.startDiscovery();
                registerDeviceDetectionBroadcast();
                return true;
            }
        }else {
            Log.e(TAG, "do====BluetoothConnector.startScan.:not initialized  ");
        }
        return false;
    }
    boolean stopScan(){
        if (isBluetoothInitialized()){
            unRegisterDeviceDetectionBroadcast();
           return bluetoothAdapter.cancelDiscovery();
        }
        return false;
    }
    private void registerDeviceDetectionBroadcast(){

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(deviceDetectionReceiver,intentFilter);
        isRegisterDeviceDetectorReceiver = true;
    }

    private void unRegisterDeviceDetectionBroadcast(){
        if (isRegisterDeviceDetectorReceiver){
            context.unregisterReceiver(deviceDetectionReceiver);
        }
    }

    /**
     * 发送数据
     * @param message
     */
    void sendData(byte[] message){
        Log.d(TAG, "do==== BluetoothConnector sendData:data:start");
        if (isConnected()){
            try {
                OutputStream outputStream = bluetoothSocket.getOutputStream();
                outputStream.write(message);
                outputStream.flush();

            } catch (IOException e) {
                Log.d(TAG, "do==== BluetoothConnector sendData:fail:IO Exception:"+e.getMessage());
                e.printStackTrace();
            }
        }else {
            Log.d(TAG, "do==== BluetoothConnector sendData:fail :not connected");
        }
    }
    void addDeviceScanReceiver(ScanResultReceiver resultReceiver){
        if (!scanResultReceivers.contains(resultReceiver)){
            scanResultReceivers.add(resultReceiver);
        }
    }
    void removeDeviceScanReceiver(ScanResultReceiver resultReceiver){
        if (scanResultReceivers.contains(resultReceiver)){
            scanResultReceivers.remove(resultReceiver);
        }
    }
    public interface ScanResultReceiver{
        void onDeviceDetection(BluetoothDevice device);
        void onScanFinished();
    }
}
