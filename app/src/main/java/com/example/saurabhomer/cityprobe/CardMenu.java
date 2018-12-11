package com.example.saurabhomer.cityprobe;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import java.util.Map;

public class CardMenu extends AppCompatActivity {
    CardView menu1,menu2,menu3,menu4,menu5;
    BluetoothAdapter bt=null;
    static int flag_datasets=0;
    static BluetoothSocket socket=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_menu);
        menu1=(CardView) findViewById(R.id.menu1);
        menu2=(CardView) findViewById(R.id.menu2);
        menu3=(CardView) findViewById(R.id.menu3);
        menu4=(CardView) findViewById(R.id.menu4);
        menu5=(CardView) findViewById(R.id.menu5);
        bt=BluetoothAdapter.getDefaultAdapter();
        //Toast.makeText(this, ""+socket, Toast.LENGTH_SHORT).show();
        if(!bt.isEnabled())
        {
            /*****first method to enable bluetooth*****/
            //enable bluetooth without pop-up any dialog box

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("An app wants to turn Bluetooth On for the device");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    bt.enable();

                }
            });


            builder.show();
            /*****Second method to enable bluetooth*****/
            //Pop-up dialog box to confirm to enable bluetooth
     /*Intent i=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
     startActivity(i); */
            //Display blutooth device value on Toast
            Toast.makeText(this, "bluetooth found.."+bt, Toast.LENGTH_LONG).show();
        }
        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(socket!=null)
                {
                    Intent i = new Intent(CardMenu.this, Graphset.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(CardMenu.this, "No device found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(CardMenu.this,Mapset.class);
                startActivity(i);

            }
        });
        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(socket!=null) {
                    Intent i = new Intent(CardMenu.this, Datasets.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(CardMenu.this, "No device found", Toast.LENGTH_SHORT).show();
                }

            }
        });
        menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CardMenu.this,Bluetooth_set.class);
                startActivity(i);

            }
        });
        menu5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CardMenu.this,MainActivity.class);
                startActivity(i);

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
       // Toast.makeText(this, ""+socket, Toast.LENGTH_SHORT).show();
        int flag_datasets=0;

    }
}
