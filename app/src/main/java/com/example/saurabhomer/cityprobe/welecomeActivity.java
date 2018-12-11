package com.example.saurabhomer.cityprobe;

import android.*;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class welecomeActivity extends AppCompatActivity {
    LinearLayout l1,l2;
    Button btnsub;
    Animation uptodown,downtoup;
    static int SPLASH_TIME = 1000;

    static Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welecome);


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET, android.Manifest.permission.BLUETOOTH,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.SEND_SMS
                },10);
                return;
            }
        }
        else {
            ImageView imageView = (ImageView) findViewById(R.id.splash_ican);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.blick);
            imageView.startAnimation(animation);

            ActionBar actionBar = getSupportActionBar();
            // actionBar.hide();
            Thread splash = new Thread() {
                public void run() {
                    try {
                        //set sleep time
                        sleep(3 * 1000);
                        Intent i = new Intent(welecomeActivity.this, CardMenu.class);
                        startActivity(i);
                        finish();
                    } catch (Exception e) {

                    }
                }
            };
            splash.start();
        }


    }


}