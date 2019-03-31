package com.example.synchronousmusicapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Creates a listener and searches for other devices on the network with the same Synchronous music app.
 */
public class JoinGroup extends AppCompatActivity {
    private ServiceDiscovery serviceDiscovery;
    public static final String TAG = "SynchMusic join";
    private InetAddress clientIp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);


    }

    /**
     * Initialize the service discovery.
     */
    @Override
    protected void onStart() {
        Log.i(TAG, "Started onStart");
        serviceDiscovery = new ServiceDiscovery(this);
        serviceDiscovery.initializeNsd();
        serviceDiscovery.discoverServices();
        super.onStart();
    }

    /**
     * Stops service discovery
     */
    @Override
    protected void onPause() {
        Log.i(TAG, "Started onPause");
        super.onPause();
        if (serviceDiscovery != null) {
            serviceDiscovery.stopDiscovery();
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "Started onResume");
        super.onResume();
    }

    /**
     * Stops all service discovery and listeners and sets them to null.
     */
    @Override
    protected void onDestroy() {
        Log.i(TAG, "Started onDestroy");
        super.onDestroy();
        serviceDiscovery.stopDiscovery();
        serviceDiscovery = null;
    }

    public void sendIp(View view) throws UnknownHostException {




        Thread getClientIp = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                   SharedPreferences pref = getSharedPreferences("hostIP",0);
                    SharedPreferences.Editor editor = pref.edit();
                    TextView hostIP = findViewById(R.id.hostIP);
                    String endDest = "localhost/" + hostIP.getText().toString();
                    editor.putString("hostIP", endDest);
                    editor.apply();



                    Log.i(TAG, "Client IP " +InetAddress.getLocalHost().toString());

                   // InetAddress sendIp = InetAddress.getByName(endDest);
                   // InetAddress.g

                    Log.i(TAG, "host ip: " +endDest);
                    pref = getSharedPreferences("clientIP",0);
                    editor = pref.edit();
                    editor.putString("clientIP", InetAddress.getLocalHost().toString());
                    editor.apply();


                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });
        getClientIp.start();

        //send message to the host with the clients ip via sockets


        //Log.d(TAG, "Client: " + clientIp + " :: "+ "Host " + endDest);
    }
}
