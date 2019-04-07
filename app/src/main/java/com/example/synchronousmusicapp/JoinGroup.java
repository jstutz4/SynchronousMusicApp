package com.example.synchronousmusicapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


/**
 * Creates a listener and searches for other devices on the network with the same Synchronous music app.
 */
public class JoinGroup extends AppCompatActivity {
    private ServiceDiscovery serviceDiscovery;
    public static final String TAG = "SynchMusic join";


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
       // serviceDiscovery.initializeNsd();

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


    public void sendIp(View view) {
//        NsdServiceInfo info = serviceDiscovery.getNsdServiceInfo();
//        info.getPort();
//        info.getHost();
//        Log.i(TAG,"AMAZING :: " + info.getPort() +" ** " + info.getHost());
//        SocketSend sendHost = new SocketSend();
//        Log.i(TAG, "trying sockets to send 222");
//        sendHost.sendToHost(info.getHost(), info.getPort());

    }
}
