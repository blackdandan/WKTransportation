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
import com.wk.wktransportation.util.ThreadPool;
import com.wk.wktransportation.util.Utils;

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

    public static void print(final int id){
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null ||
                !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState()) {
            Utils.toast(App.getApplication(),App.getApplication(). getString(R.string.str_cann_printer));
            return;
        }
        ThreadPool.getInstantiation().addTask(new Runnable() {
            @Override
            public void run() {
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.ESC) {
                    sendReceiptWithResponse(id);
                } else {

                }
            }
        });
    }

    /**
     * 发送数据
     */
    private static void sendReceiptWithResponse(int id) {
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 3);
        // 设置打印居中
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        // 设置为倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        // 打印文字
        esc.addText("Sample\n");
        esc.addPrintAndLineFeed();

        /* 打印文字 */
        // 取消倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        // 打印文字
        esc.addText("Print text\n");
        // 打印文字
        esc.addText("Welcome to use SMARNET printer!\n");

        /* 打印繁体中文 需要打印机支持繁体字库 */
        String message = "佳博智匯票據打印機\n";
        esc.addText(message, "GB2312");
        esc.addPrintAndLineFeed();

        /* 绝对位置 具体详细信息请查看GP58编程手册 */
        esc.addText("智汇");
        esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
        esc.addSetAbsolutePrintPosition((short) 6);
        esc.addText("网络");
        esc.addSetAbsolutePrintPosition((short) 10);
        esc.addText("设备");
        esc.addPrintAndLineFeed();

        /* 打印图片 */
        // 打印文字
        esc.addText("Print bitmap!\n");
        Bitmap b = BitmapFactory.decodeResource(App.getApplication().getResources(),
                R.mipmap.gprinter);
        // 打印图片
        esc.addOriginRastBitImage(b, 384, 0);

        /* 打印一维条码 */
        // 打印文字
        esc.addText("Print code128\n");
        esc.addSelectPrintingPositionForHRICharacters(EscCommand.HRI_POSITION.BELOW);
        // 设置条码可识别字符位置在条码下方
        // 设置条码高度为60点
        esc.addSetBarcodeHeight((byte) 60);
        // 设置条码单元宽度为1
        esc.addSetBarcodeWidth((byte) 1);
        // 打印Code128码
        esc.addCODE128(esc.genCodeB("SMARNET"));
        esc.addPrintAndLineFeed();

        /*
         * QRCode命令打印 此命令只在支持QRCode命令打印的机型才能使用。 在不支持二维码指令打印的机型上，则需要发送二维条码图片
         */
        // 打印文字
        esc.addText("Print QRcode\n");
        // 设置纠错等级
        esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31);
        // 设置qrcode模块大小
        esc.addSelectSizeOfModuleForQRCode((byte) 3);
        // 设置qrcode内容
        esc.addStoreQRCodeData("www.smarnet.cc");
        esc.addPrintQRCode();// 打印QRCode
        esc.addPrintAndLineFeed();

        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        //打印文字
        esc.addText("Completed!\r\n");

        // 开钱箱
        esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
        esc.addPrintAndFeedLines((byte) 8);
        // 加入查询打印机状态，打印完成后，此时会接收到GpCom.ACTION_DEVICE_STATUS广播
        esc.addQueryPrinterStatus();
        Vector<Byte> datas = esc.getCommand();
        // 发送数据
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas);
    }
}
