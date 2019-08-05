package com.example.waterlevelcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import me.aflak.bluetooth.Bluetooth;

public class Chat extends AppCompatActivity implements Bluetooth.CommunicationCallback {
    private ViewFlipper viewFlipper;
    private String name;
    private Bluetooth b;
    private Button btnOn,btnOff;
    private TextView text;
    private ScrollView scrollView;
    private boolean registered=false;

    //AnimationDrawable chargingAnimation;

    private ImageView levelViewer;
    private  TextView textinfo;

    public int msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        viewFlipper= findViewById(R.id.view_flipper);
        text = findViewById(R.id.text);
        btnOn =  findViewById(R.id.btnOn);
        btnOff = findViewById(R.id.btnOff);
        scrollView =  findViewById(R.id.scrollView);

        text.setMovementMethod(new ScrollingMovementMethod());

        btnOn.setEnabled(false);
        btnOff.setEnabled(false);

        levelViewer = findViewById(R.id.levelViewer);
        textinfo =  findViewById(R.id.textInfo);




       // levelViewer.setBackgroundResource(R.drawable.charging_animation);
       //chargingAnimation= (AnimationDrawable) levelViewer.getBackground();

        b = new Bluetooth(this);
        b.enableBluetooth();

        b.setCommunicationCallback(this);

        int pos = getIntent().getExtras().getInt("pos");
        name = b.getPairedDevices().get(pos).getName();

        Display("Connecting...");
        b.connectToDevice(b.getPairedDevices().get(pos));

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "a";
                b.send(msg);
                Display("You: "+msg);

                //chargingAnimation.start();


                Toast.makeText(Chat.this, "Requested To Turn On Motor!", Toast.LENGTH_SHORT).show();

            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "b";
                b.send(msg);
                Display("You: "+msg);
                Toast.makeText(Chat.this, "Requested To Turn Off Motor!", Toast.LENGTH_SHORT).show();
            }
        });

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        registered=true;
    }


    public void nextView(View v){
        viewFlipper.showNext();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(registered) {
            unregisterReceiver(mReceiver);
            registered=false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.close:
                b.removeCommunicationCallback();
                b.disconnect();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Display(final String s){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.append(s + "\n");
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }



    @Override
    public void onConnect(BluetoothDevice device) {
        Display("Connected to "+device.getName()+" - "+device.getAddress());

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                btnOn.setEnabled(true);
                btnOff.setEnabled(true);


            }
        });
    }


    @Override
    public void onDisconnect(BluetoothDevice device, String message) {
        Display("Disconnected!");
        Display("Connecting again...");
        b.connectToDevice(device);
    }

    @Override
    public void onMessage(String message) {

        Display(name+": "+message);
       levelViewDeciding(message);

    }

    public void levelViewDeciding(String message) {

        msg  = Integer.parseInt(message);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (msg < 10) {
                    levelViewer.setImageResource(R.drawable.level_10);
                    Display("Water level less than 10% ");
                    textinfo.setText("Water level less than 10% ");

                } else if (msg >= 10 && msg <= 20) {
                      levelViewer.setImageResource(R.drawable.level_20);
                    Display("Water level about 20% ");
                     textinfo.setText("Water level about 20% ");
                } else if (msg > 20 && msg <= 30) {
                      levelViewer.setImageResource(R.drawable.level_30);
                    Display("Water level is about 30% ");
                    textinfo.setText("Water level is about 30% ");
                } else if (msg > 30 && msg <= 40) {
                     levelViewer.setImageResource(R.drawable.level_40);
                    Display("Water level is about 40%");
                      textinfo.setText("Water level is about 40%");
                } else if (msg > 40 && msg <= 50) {
                     levelViewer.setImageResource(R.drawable.level_50);
                    Display("Water level is about 50%");
                     textinfo.setText("Water level is about 50%");
                } else if (msg > 50 && msg <= 60) {
                     levelViewer.setImageResource(R.drawable.level_60);
                    Display("Water level is about 60%");
                     textinfo.setText("Water level is about 60%");
                } else if (msg > 60 && msg <= 70) {
                     levelViewer.setImageResource(R.drawable.level_70);
                    Display("Water level is about 70%");
                     textinfo.setText("Water level is about 70%");
                } else if (msg > 70 && msg <= 80) {
                     levelViewer.setImageResource(R.drawable.level_80);
                    Display("Water level is about 80%");
                     textinfo.setText("Water level is about 80%");
                } else if (msg > 80 && msg <= 90) {
                    levelViewer.setImageResource(R.drawable.level_90);
                    Display("Water level is about 90%");
                     textinfo.setText("Water level is about 90%");
                } else if (msg > 90) {
                     levelViewer.setImageResource(R.drawable.level_100);
                    Display("Water level is about 100%");
                     textinfo.setText("Water level is about 100%");
                }

            }
    });
    }



    @Override
    public void onError(String message) {
        Display("Error: "+message);
    }


    @Override
    public void onConnectError(final BluetoothDevice device, String message) {
        Display("Error: "+message);
        Display("Trying again in 3 sec.");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        b.connectToDevice(device);
                    }
                }, 2000);
            }
        });
    }






    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                Intent intent1 = new Intent(Chat.this, MainActivity.class);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        if(registered) {
                            unregisterReceiver(mReceiver);
                            registered=false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        if(registered) {
                            unregisterReceiver(mReceiver);
                            registered=false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                }
            }
        }
    };


}
