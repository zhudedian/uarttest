package com.edong.uarttest;

import android.serialport.SerialPort;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.UartDevice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    FileOutputStream mOutputStream;
    FileInputStream mInputStream;
    SerialPort sp;

    int sendTimes = 20000;
    private UartDevice mDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        PeripheralManagerService manager = new PeripheralManagerService();
//        List<String> deviceList = manager.getUartDeviceList();
//        if (deviceList.isEmpty()) {
//            Log.i("edong", "No UART port available on this device.");
//        } else {
//            Log.i("edong", "List of available devices: " + deviceList);
//        }

        try {
            sp=new SerialPort(new File("/dev/ttyS2"),115200);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        mOutputStream=(FileOutputStream) sp.getOutputStream();
        mInputStream=(FileInputStream) sp.getInputStream();

        new Thread(){
            public void run(){
                int i = 0;
                while (i<sendTimes){
                    i++;
                    try {
                        mOutputStream.write(new String("send:"+i).getBytes());
                        mOutputStream.write('\r');
                        mOutputStream.write('\n');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }
    public void configureUartFrame(UartDevice uart) throws IOException {
        // Configure the UART port
        uart.setBaudrate(115200);
        uart.setDataSize(8);
        uart.setParity(UartDevice.PARITY_NONE);
        uart.setStopBits(1);
    }
    public void writeUartData(UartDevice uart) throws IOException {
        byte[] buffer = {'1','2','3','4'};
        int count = uart.write(buffer, buffer.length);
        Log.d("edong", "Wrote " + count + " bytes to peripheral");
    }
    public void readUartBuffer(UartDevice uart) throws IOException {
        // Maximum amount of data to read at one time
        final int maxCount = 2048;
        byte[] buffer = new byte[maxCount];

        int count;
        while ((count = uart.read(buffer, buffer.length)) > 0) {
            Log.d("edong", "Read " + count + " bytes from peripheral");
        }
    }
}
