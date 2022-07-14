package com.waterdelivery.Checkout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import com.sunmi.peripheral.printer.InnerLcdCallback;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.InnerTaxCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;
import com.sunmi.peripheral.printer.TransBean;
import com.waterdelivery.R;

public class Print extends AppCompatActivity {
    boolean result;
    SunmiPrinterService sunmiPrinterService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        try {
            result = InnerPrinterManager.getInstance().bindService(this,
                    innerPrinterCallback);
        }catch (Exception e){

        }


    }

    InnerPrinterCallback innerPrinterCallback=new InnerPrinterCallback() {
        @Override
        protected void onConnected(SunmiPrinterService service) {

  // sunmiPrinterService.printText("Equal",innerPrinterCallback);
            Toast.makeText(Print.this,String.valueOf(result),Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onDisconnected() {

        }
    };

    public void data() {

         sunmiPrinterService = new SunmiPrinterService() {
            @Override
            public void updateFirmware() throws RemoteException {

            }

            @Override
            public int getFirmwareStatus() throws RemoteException {
                return 0;
            }

            @Override
            public String getServiceVersion() throws RemoteException {
                return null;
            }

            @Override
            public void printerInit(InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void printerSelfChecking(InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public String getPrinterSerialNo() throws RemoteException {
                return null;
            }

            @Override
            public String getPrinterVersion() throws RemoteException {
                return null;
            }

            @Override
            public String getPrinterModal() throws RemoteException {
                return null;
            }

            @Override
            public void getPrintedLength(InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void lineWrap(int n, InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void sendRAWData(byte[] data, InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void setAlignment(int alignment, InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void setFontName(String typeface, InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void setFontSize(float fontsize, InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void printText(String text, InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void printTextWithFont(String text, String typeface, float fontsize, InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void printColumnsText(String[] colsTextArr, int[] colsWidthArr, int[] colsAlign, InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void printBitmap(Bitmap bitmap, InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void printBarCode(String data, int symbology, int height, int width, int textposition, InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void printQRCode(String data, int modulesize, int errorlevel, InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void printOriginalText(String text, InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void commitPrint(TransBean[] transbean, InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void commitPrinterBuffer() throws RemoteException {

            }

            @Override
            public void cutPaper(InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public int getCutPaperTimes() throws RemoteException {
                return 0;
            }

            @Override
            public void openDrawer(InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public int getOpenDrawerTimes() throws RemoteException {
                return 0;
            }

            @Override
            public void enterPrinterBuffer(boolean clean) throws RemoteException {

            }

            @Override
            public void exitPrinterBuffer(boolean commit) throws RemoteException {

            }

            @Override
            public void tax(byte[] data, InnerTaxCallback callback) throws RemoteException {

            }

            @Override
            public void getPrinterFactory(InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void clearBuffer() throws RemoteException {

            }

            @Override
            public void commitPrinterBufferWithCallback(InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void exitPrinterBufferWithCallback(boolean commit, InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void printColumnsString(String[] colsTextArr, int[] colsWidthArr, int[] colsAlign, InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public int updatePrinterState() throws RemoteException {
                return 0;
            }

            @Override
            public void sendLCDCommand(int flag) throws RemoteException {

            }

            @Override
            public void sendLCDString(String string, InnerLcdCallback callback) throws RemoteException {

            }

            @Override
            public void sendLCDBitmap(Bitmap bitmap, InnerLcdCallback callback) throws RemoteException {

            }

            @Override
            public int getPrinterMode() throws RemoteException {
                return 0;
            }

            @Override
            public int getPrinterBBMDistance() throws RemoteException {
                return 0;
            }

            @Override
            public void printBitmapCustom(Bitmap bitmap, int type, InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public int getForcedDouble() throws RemoteException {
                return 0;
            }

            @Override
            public boolean isForcedAntiWhite() throws RemoteException {
                return false;
            }

            @Override
            public boolean isForcedBold() throws RemoteException {
                return false;
            }

            @Override
            public boolean isForcedUnderline() throws RemoteException {
                return false;
            }

            @Override
            public int getForcedRowHeight() throws RemoteException {
                return 0;
            }

            @Override
            public int getFontName() throws RemoteException {
                return 0;
            }

            @Override
            public void sendLCDDoubleString(String topText, String bottomText, InnerLcdCallback callback) throws RemoteException {

            }

            @Override
            public int getPrinterPaper() throws RemoteException {
                return 0;
            }

            @Override
            public boolean getDrawerStatus() throws RemoteException {
                return false;
            }

            @Override
            public void sendLCDFillString(String string, int size, boolean fill, InnerLcdCallback callback) throws RemoteException {

            }

            @Override
            public void sendLCDMultiString(String[] text, int[] align, InnerLcdCallback callback) throws RemoteException {

            }

            @Override
            public int getPrinterDensity() throws RemoteException {
                return 0;
            }

            @Override
            public void print2DCode(String data, int symbology, int modulesize, int errorlevel, InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void autoOutPaper(InnerResultCallback callback) throws RemoteException {

            }

            @Override
            public void setPrinterStyle(int key, int value) throws RemoteException {

            }

            @Override
            public void labelLocate() throws RemoteException {

            }

            @Override
            public void labelOutput() throws RemoteException {

            }

            @Override
            public IBinder asBinder() {
                return null;
            }
        };}

 public  void prince()
 {

 }

}