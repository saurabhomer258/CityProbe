package com.example.saurabhomer.cityprobe;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class welecomeActivity extends AppCompatActivity {
    LinearLayout l1,l2;
    Button btnsub;
    Animation uptodown,downtoup;
    static int SPLASH_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welecome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                //Do any action here. Now we are moving to next page
                Intent mySuperIntent = new Intent(welecomeActivity.this, CardMenu.class);
                startActivity(mySuperIntent);
                /* This 'finish()' is for exiting the app when back button pressed
                *  from Home page which is Activitywelcome
                */

                finish();
            }
        }, SPLASH_TIME);
    }
}