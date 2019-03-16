package com.example.synchronousmusicapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class JoinGroup extends AppCompatActivity {
    private ServiceDiscovery serviceDiscovery;
    public static final String TAG = "SynchMusic join";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        serviceDiscovery = new ServiceDiscovery(this);
        serviceDiscovery.initializeNsd();
        serviceDiscovery.discoverServices();
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "Started onStart");
        super.onStart();
    }

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

    @Override
    protected void onDestroy() {
        Log.i(TAG, "Started onDestroy");
        super.onDestroy();
        serviceDiscovery.stopDiscovery();
        serviceDiscovery = null;
    }

}
