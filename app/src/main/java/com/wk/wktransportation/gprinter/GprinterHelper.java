package com.wk.wktransportation.gprinter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.wk.wktransportation.App;
import com.wk.wktransportation.R;
import com.wk.wktransportation.entity.Tempertumer;
import com.wk.wktransportation.ui.selecttypepage.SelectTypePage;
import com.wk.wktransportation.ui.temperature.TemperatureActivity;
import com.wk.wktransportation.util.ThreadPool;
import com.wk.wktransportation.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * @discription 佳博SDK使用
 * <p>
 * **********************************
 * @auth @yangzehui
 * @email @784818984@qq.com
 * @date @2018/7/13 23:17
 * **********************************
 **/
public class GprinterHelper {
    private static final String TAG = "GprinterHelper";

    public static void print(final int id, final String startTime,
                             final List<Tempertumer> tempertumers, final String customer,
                             final int type, final String carnumber, final String number
                            , final float maxValue, final float minValue){
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null ||
                !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState()) {
            Utils.toast(App.getApplication(),App.getApplication(). getString(R.string.str_cann_printer));
            return;
        }
        ThreadPool.getInstantiation().addTask(new Runnable() {
            @Override
            public void run() {
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.ESC) {
                    sendReceiptWithResponse(id,startTime,tempertumers,customer,type,carnumber,number,maxValue,minValue);
                } else {

                }
            }
        });
    }

    /**
     * 发送数据
     */
    private static void sendReceiptWithResponse(int id,String startTime,
                                                final List<Tempertumer> tempertumers,
                                                final String customer, final int type,
                                                final String carnumber, final String number,float maxValue,float minValue) {
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 3);
        // 设置打印居中
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        // 设置为倍高倍宽
//        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        // 打印文字
        esc.addText("太原维康鸿业科技有限公司\n");
        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);

        if (type == TemperatureActivity.TYPE_CAR){
            esc.addText("运输方式:冷藏车\n");
        }else {
            esc.addText("运输方式:保温箱\n");
        }

        esc.addText("收货方:"+customer+"\n");
        esc.addText("发货方:太原维康鸿业科技有限公司\n");
        esc.addText("承运方:太原维康鸿业科技有限公司\n");
        if (type == TemperatureActivity.TYPE_CAR){
            esc.addText("车牌号:"+carnumber+"\n");
        }
        esc.addText("设备编号:"+number+"\n");
        esc.addPrintAndLineFeed();
        esc.addText("--------------------------------\n");
        if (type == TemperatureActivity.TYPE_CAR){
            esc.addText("时间: 编号: 温度℃: 编号: 温度℃  \n");
        }else {
            esc.addText("时间:  温度℃:  时间:  温度℃\n");
        }
        esc.addText("--------------------------------\n");
        esc.addText(startTime+"\n");

        if (type == TemperatureActivity.TYPE_CAR){
            List<Tempertumer> tempertumerList1 = tempertumers.subList(0,tempertumers.size()/2-1);
            List<Tempertumer> tempertumerList2 = tempertumers.subList(tempertumers.size()/2,tempertumers.size()-1);
            for (int i = 0;i<tempertumerList1.size();i++){
                Tempertumer tempertumer1 = tempertumerList1.get(i);
                String time = tempertumer1.getDatetime();
                String num1 = tempertumer1.getIncubatornumber();
                if (time!=null){
                    esc.addText(time.substring(time.length()-8,time.length()-3));
                    esc.addText("  "+num1.substring(num1.length()-2,num1.length())+":");
                    esc.addText(" "+tempertumer1.getTempertumer()+"  ");
                    Tempertumer tempertumer2 = tempertumerList2.get(i);

                    String num2 = tempertumer2.getIncubatornumber();
                    esc.addText(time.substring(time.length()-8,time.length()-3));
                    esc.addText("  "+num2.substring(num2.length()-2,num2.length())+":");
                    esc.addText(" "+tempertumer2.getTempertumer());
                    esc.addText("\n");

                }
            }
        }else{
            for (int i = 0; i< tempertumers.size();i++){
                Tempertumer tempertumer = tempertumers.get(i);
                String time = tempertumer.getDatetime();
                if (time!=null){
                    esc.addText(time.substring(time.length()-8,time.length()-3));
                    esc.addText("  "+tempertumer.getTempertumer());
                    if (i%2 == 0){
                        esc.addText("\n");
                    }
                }
            }

        }
        esc.addText("最大温度值:"+maxValue+"\n");
        esc.addText("最小温度值:"+minValue+"\n");
        esc.addText("收货人:\n");
        esc.addText("交货人:\n");
        esc.addText("\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.RIGHT);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
        String now = simpleDateFormat.format(new Date());
        esc.addText(now+"\n");
        esc.addText("\n");
        esc.addText("\n");
        esc.addText("\n");
        esc.addText("\n");
        // 加入查询打印机状态，打印完成后，此时会接收到GpCom.ACTION_DEVICE_STATUS广播
        esc.addQueryPrinterStatus();
        Vector<Byte> datas = esc.getCommand();
        // 发送数据
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas);
    }
}
