package com.example.saurabhomer.cityprobe;
import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button listen, send, listDevices, upLoad;
    ListView listView;
    TextView status, msg_box;
    EditText writeMsg;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice[] btArray;
    File file;

    static final int STATE_LISTNING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;
    int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final String APP_NAME = "Bluetooth1";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    SendReceive sendReceive;

    LocationManager locationManager;
    LocationListener locationListener;
    double lng = 0;
    double lat = 0;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    // Write a message to the database

    // Write a message to the database


    String upLoadServerUri = null;

    /**********  File Path *************/
    final String uploadFilePath = "/BluetoothApp/";
    final String uploadFileName = "Filename.txt";
    // Write a message to the database
    FirebaseDatabase database;
    DatabaseReference Ref;
    String mydate;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        database=FirebaseDatabase.getInstance();
        Ref=database.getReference("Data");
        Calendar cal = Calendar. getInstance();

        mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
//        Ref.push().setValue(new DataModel(mydate,"s","d","s","d","f","dd","f","d","s","d","d"));

        //updateChildrenAsync(hopperUpdates);

        findViewByIds();
        msg_box.setMovementMethod(new ScrollingMovementMethod());
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        upLoadServerUri = "http://rohitekka.000webhostapp.com/test1/UploadToServer.php";

        if(!bluetoothAdapter.isEnabled()){
            Intent enableIntent=new Intent(bluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BLUETOOTH);
        }




        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                lng = location.getLongitude();
                lat=location.getLatitude();
                Toast.makeText(MainActivity.this, "lat"+lat, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        };



        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                },10);
                return;
            }
        }else{
            configurButton();
        }

        implementListeners();

    }

    private void implementListeners() {
        listDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<BluetoothDevice> bt=bluetoothAdapter.getBondedDevices();
                String[] strings=new String[bt.size()];
                btArray=new BluetoothDevice[bt.size()];
                int index=0;
                if(bt.size()>0){
                    for(BluetoothDevice device:bt){
                        btArray[index]=device;
                        strings[index]=device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,strings);
                    listView.setAdapter(arrayAdapter);
                }

            }
        });
        /*listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerClass serverClass=new ServerClass();
                serverClass.start();
            }
        });
        */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClientClass clientClass=new ClientClass(btArray[position]);
                clientClass.start();

                status.setText("Connecting");
            }
        });
        /*send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string= String.valueOf(writeMsg.getText());
                sendReceive.write(string.getBytes());
            }
        });
        */

        upLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                msg_box.setText("uploading started.....");
                            }
                        });

                        uploadFile(uploadFilePath + "" + uploadFileName);

                    }
                }).start();
            }
        });

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
                    //byte[] readBuff= (byte[]) msg.obj;
                    String readBuffr=(String)msg.obj;
                    //int bytes=msg.arg1;
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
                    Toast.makeText(MainActivity.this, "data"+arr_data[9], Toast.LENGTH_SHORT).show();
                    Ref.push().setValue(new DataModel(mydate,String.valueOf(lat),String.valueOf(lng).trim(),arr_data[0].trim(),arr_data[1].trim(),arr_data[2].trim(),arr_data[3].trim(),arr_data[4].trim(),arr_data[5].trim(),arr_data[6].trim(),arr_data[7].trim(),arr_data[8].trim(),arr_data[9].trim()));
// String pm1, String pm25, String pm10, String no2, String co, String humidity,String temperature)
//                    String AQI_Category=""
//                    if(Integer.parseInt(arr_data[1]>30))
//                    {
//
//                        if(Integer.parseInt(arr_data[1]>250))
//                        {
//                            AQI_Category="Severe";
//                        }
//                        else if(Integer.parseInt(arr_data[1]>120))
//                        {
//                            AQI_Category="Very poor";
//                        }
//                        else if(Integer.parseInt(arr_data[1]>91))
//                        {
//                            AQI_Category="poor";
//                        }
//                        else if(Integer.parseInt(arr_data[1]>61))
//                        {
//                            AQI_Category="Moderately polluted";
//                        }
//                        else
//                        {
//                            AQI_Category="Satisfactory";
//                        }
//                        AQI_Category+=" pm2.5\n";
//                    }
//                    //pm 10
//
//                    if(Integer.parseInt(arr_data[2]>50))
//                    {
//
//                        if(Integer.parseInt(arr_data[1]>430))
//                        {
//                            AQI_Category="Severe";
//                        }
//                        else if(Integer.parseInt(arr_data[1]>351))
//                        {
//                            AQI_Category="Very poor";
//                        }
//                        else if(Integer.parseInt(arr_data[1]>201))
//                        {
//                            AQI_Category="poor";
//                        }
//                        else if(Integer.parseInt(arr_data[1]>101))
//                        {
//                            AQI_Category="Moderately polluted";
//                        }
//                        else
//                        {
//                            AQI_Category="Satisfactory";
//                        }
//                        AQI_Category+=" pm10\n";
//                    }

                    msg_box.append(tempMsg);
                    break;
            }
            return true;
        }
    });

    private void findViewByIds() {

        listen=(Button)findViewById(R.id.listen);
        listDevices=(Button) findViewById(R.id.listDevices);
        //send=(Button)findViewById(R.id.send);
        listView=(ListView)findViewById(R.id.listView);
        msg_box=(TextView)findViewById(R.id.msg);
        status=(TextView)findViewById(R.id.status);
        //writeMsg=(EditText)findViewById(R.id.writeMsg);
        upLoad=(Button)findViewById(R.id.uploadbtn);



    }

    private void appendTextAndScroll(String text)
    {
        if(msg_box != null){
            msg_box.append(text + "\n");
            final Layout layout = msg_box.getLayout();
            if(layout != null){
                int scrollDelta = layout.getLineBottom(msg_box.getLineCount() - 1)
                        - msg_box.getScrollY() - msg_box.getHeight();
                if(scrollDelta > 0)
                    msg_box.scrollBy(0, scrollDelta);
            }
        }
    }

    private class ServerClass extends Thread{
        private BluetoothServerSocket serverSocket;

        public ServerClass(){
            try {
                serverSocket=bluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME,MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void run(){
            BluetoothSocket socket=null;
            while(socket==null){
                try {
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTING;
                    handler.sendMessage(message);
                    socket=serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                }
                if(socket!=null){
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTED;
                    handler.sendMessage(message);
                    sendReceive=new SendReceive(socket);
                    sendReceive.start();
                    break;
                }
            }
        }
    }

    private class ClientClass extends Thread{
        private BluetoothDevice device;
        private BluetoothSocket socket;
        public ClientClass(BluetoothDevice device1){
            device=device1;
            try {
                socket=device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void run(){
            try {

                socket.connect();
                Message message=Message.obtain();
                message.what=STATE_CONNECTED;
                handler.sendMessage(message);
                sendReceive=new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
                Message message=Message.obtain();
                message.what=STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }
    }
    private class SendReceive extends Thread{
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;
        public SendReceive(BluetoothSocket socket){
            bluetoothSocket=socket;
            InputStream tempIn=null;
            OutputStream tempOut=null;
            try {
                tempIn=bluetoothSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                tempOut=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream=tempIn;
            outputStream=tempOut;

        }
        public void run(){
            byte [] buffer=new byte[1024];
            final byte delimiter = 10;
            int bytes;
            int readBufferPosition = 0;
            while(true){
                try{

                    int bytesAvailable=inputStream.available();

                    if(bytesAvailable>0){
                        byte[] packetByte=new byte[bytesAvailable];
                        inputStream.read(packetByte);
                        for(int i=0;i<bytesAvailable;i++){
                            byte b=packetByte[i];
                            if(b==delimiter){
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(buffer, 0, encodedBytes, 0, encodedBytes.length);
                                final String data = new String(encodedBytes);
                                readBufferPosition = 0;
                                handler.obtainMessage(STATE_MESSAGE_RECEIVED,data).sendToTarget();


                            }
                            else
                            {
                                buffer[readBufferPosition++] = b;
                            }
                        }

                    }

                    //bytes = inputStream.read(buffer);
                    //handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();

                } catch (IOException e){
                    e.printStackTrace();
                }

            }

        }
        public void write(byte[] bytes){
            try{
                outputStream.write(bytes);
            } catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    configurButton();
        }
    }

    private void configurButton() {
        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.requestLocationUpdates("gps", 1000, 0, locationListener);

            }
        });

    }

    public void uploadFile(String sourceFileUri) {

        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;
        String pathToOurFile = sourceFileUri;
        String urlServer = "http://145.14.145.121:21/test1/UploadToServer.php";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1*1024*1024;

        try
        {
            FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );

            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();

            // Allow Inputs &amp; Outputs.
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // Set HTTP method to POST.
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

            outputStream = new DataOutputStream( connection.getOutputStream() );
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0)
            {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = connection.getResponseCode();
            //serverResponseMessage = connection.getResponseMessage();

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception ex)
        {
            //Exception handling
        }

    }

}