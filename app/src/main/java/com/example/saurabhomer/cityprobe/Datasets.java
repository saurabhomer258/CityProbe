package com.example.saurabhomer.cityprobe;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.example.saurabhomer.cityprobe.MainActivity.connectbt;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class Datasets extends AppCompatActivity {
    BluetoothSocket socket;
    Datasets.SendReceive sendReceive;
    static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    File file;
    int flag = 0;
    int snooze_count = 0;
    static final int STATE_LISTNING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;
    int sms_send_time = 0;
    TextView massage;
    private boolean plotData = true;
    String dgas = null;
    private TextView msg_box,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datasets);
        massage = (TextView) findViewById(R.id.massage);
        msg_box = (TextView) findViewById(R.id.msg);
        status = (TextView) findViewById(R.id.status);
        msg_box.setMovementMethod(new ScrollingMovementMethod());

        Datasets.ClientClass clientClass = new Datasets.ClientClass(MainActivity.connectbt);
        clientClass.start();
    }
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String currentDate = sdf.format(new Date());
            String filename=currentDate;
            switch (msg.what){
                case STATE_LISTNING:
                    status.setText("Listning");
                    break;
                case STATE_CONNECTING:
                    status.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    status.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("Connection_Failed");
                    break;
                case STATE_MESSAGE_RECEIVED:

                    String readBuffr=(String)msg.obj;

                    String tempMsg = readBuffr;

                    String path = Environment.getExternalStorageDirectory()+"/BluetoothApp/";
                    file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    RandomAccessFile raf;
                    try{
                        raf=new RandomAccessFile(file+"/Filename.txt","rw");
                        raf.seek(raf.length());
                        raf.write(tempMsg.getBytes());
                        raf.close();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    String[] arr_data = tempMsg.split(",");
                    //Toast.makeText(MainActivity.this, "data"+arr_data[9], Toast.LENGTH_SHORT).show();
                    // Ref=database.getReference("Data");
                    // Ref.push().setValue(new DataModel(mydate,String.valueOf(lat),String.valueOf(lng).trim(),arr_data[0].trim(),arr_data[1].trim(),arr_data[2].trim(),arr_data[3].trim(),arr_data[4].trim(),arr_data[5].trim(),arr_data[6].trim(),arr_data[7].trim(),arr_data[8].trim(),arr_data[9].trim()));




                    AQI aqi=new AQI();
                    String worning_msg="";
                    // pm10
                    worning_msg+=aqi.aqiTest((float) Double.parseDouble(arr_data[4].trim()),0,50,51,100,101,250,251,350,351,430,"PM10");
                    // pm2.5
                    //   aqi.aqiTest(Float.parseFloat(arr_data[3].trim()),0,30,31,60,61,90,91,120,121,250,"PM2.5")
                    // worning_msg+=aqi.aqiTest(Float.parseFloat(arr_data[5].trim()),0,40,41,80,81,180,181,280,281,400,"NO2");
                    worning_msg+=aqi.aqiTest(Float.parseFloat(arr_data[7].trim()),0.0f,1.0f,1.1f,2.0f,2.1f,10.0f,10.0f,17.0f,17.0f,34.0f,"CO");
                    worning_msg+=aqi.aqiTest(Float.parseFloat(arr_data[6].trim()),0,40,41,80,81,180,181,280,281,400,"CO2");
                    if(worning_msg.trim()!=null && worning_msg.trim()!="" && snooze_count==0) {
                        massage.setText(worning_msg);
                        //Toast.makeText(MainActivity.this, "" + worning_msg, Toast.LENGTH_SHORT).show();
                       // playTone();
                        if(sms_send_time==120)
                        {
                            //SmsManager smsManager = SmsManager.getDefault();
                            //smsManager.sendTextMessage("+919434789009", null, "alert sms:"+worning_msg, null, null);
                          //  sms_send_time=0;

                        }
                        else
                        {
                            sms_send_time++;

                        }

                    }
                    if(worning_msg=="" || worning_msg==null)
                    {
                        massage.setText("");

                    }
                    if(snooze_count!=0)
                    {
                        snooze_count--;
                    }


                    if(flag==1){
                        // Toast.makeText(MainActivity.this, "dj", Toast.LENGTH_SHORT).show();
                        if(dgas=="pm1")
                        {
                            Log.e("A","pm1");

                            //Toast.makeText(MainActivity.this, "pm1", Toast.LENGTH_SHORT).show();
                         //   addEntry(Integer.parseInt(arr_data[2].trim()));
                        }
                        else if(dgas=="pm25")
                        {
                            Log.e("A","pm25");


                           // addEntry(Integer.parseInt(arr_data[3].trim()));
                        }
                        else if(dgas=="pm10")
                        {
                            Log.e("A","pm10");

                          //  addEntry(Integer.parseInt(arr_data[4].trim()));

                        }
                        else if(dgas=="no2")
                        {
                            Log.e("A","no2");

//                            addEntry(Integer.parseInt(arr_data[5].trim()));

                        }
                        else if(dgas=="co")
                        {
                            Log.e("A","co");


                          //  addEntry(Integer.parseInt(arr_data[7].trim()));
                        }
                        else if(dgas=="co2")
                        {
                            Log.e("A","co2");


                          //  addEntry(Integer.parseInt(arr_data[6].trim()));
                        }
                        plotData = false;
                    }

                    msg_box.append(tempMsg);
                    break;
            }
            return true;
        }
    });

//    private static void playTone( ) {
//
//        try {
//
//            if (toneGenerator == null) {
//                toneGenerator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
//            }
//            toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 900);
//
//            Handler handler = new Handler(Looper.getMainLooper());
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (toneGenerator != null) {
//                        //Log.d(TAG, "ToneGenerator released");
//                        toneGenerator.release();
//                        toneGenerator = null;
//                    }
//                }
//
//            }, 900);
//        } catch (Exception e) {
//            Log.d("ex", "Exception while playing sound:" + e);
//        }
//    }

    private class ClientClass extends Thread {
       BluetoothDevice device;


        public ClientClass(BluetoothDevice device1) {
            device = device1;

                socket = Bluetooth_set.socket;

        }

        public void run() {

              // socket.connect();
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                handler.sendMessage(message);
               sendReceive = new Datasets.SendReceive(Bluetooth_set.socket);
                sendReceive.start();

        }
    }

    private class SendReceive extends Thread {
        private final BluetoothSocket bluetoothSocket;
        InputStream inputStream;
        OutputStream outputStream;

        public SendReceive(BluetoothSocket socket) {
            bluetoothSocket = socket;

            InputStream tempIn = null;
            OutputStream tempOut = null;
            try {
                tempIn = bluetoothSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                tempOut = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

           inputStream = tempIn;
            outputStream = tempOut;

        }

        public void run() {
            byte[] buffer = new byte[1024];
            final byte delimiter = 10;
            int bytes;
            int readBufferPosition = 0;
            while (true) {
                try {

                    int bytesAvailable = inputStream.available();

                    if (bytesAvailable > 0) {
                        byte[] packetByte = new byte[bytesAvailable];
                        inputStream.read(packetByte);
                        for (int i = 0; i < bytesAvailable; i++) {
                            byte b = packetByte[i];
                            if (b == delimiter) {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(buffer, 0, encodedBytes, 0, encodedBytes.length);
                                final String data = new String(encodedBytes);
                                readBufferPosition = 0;
                                handler.obtainMessage(STATE_MESSAGE_RECEIVED, data).sendToTarget();


                            } else {
                                buffer[readBufferPosition++] = b;
                            }
                        }

                    }

                    //bytes = inputStream.read(buffer);
                    //handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}