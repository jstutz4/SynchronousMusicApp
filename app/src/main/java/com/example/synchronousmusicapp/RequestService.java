package com.example.synchronousmusicapp;

import android.bluetooth.BluetoothAdapter;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pServiceRequest;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class RequestService {

    private WifiP2pServiceInfo serviceInfo;
    private Map record = new HashMap();

   // private WifiP2pServiceRequest serviceRequest = WifiP2pServiceRequest.newInstance();
    private BluetoothAdapter blueToothName;
    private NsdManager.DiscoveryListener discoveryListener;
    private static final String TAG = "synchronousmusic.local";

    public void regServic(){
//        record.put("listenport", String.valueOf(0));
//        record.put("buddyname", getLocalBluetoothName() + (int) (Math.random() * 1000));
//        record.put("available", "visible");
//       // serviceInfo = WifiP2pServiceInfo.newInstance("_test", "_presence._tcp", record);
//    }
//
//    public String getLocalBluetoothName(){
//        if(blueToothName == null){
//            blueToothName = BluetoothAdapter.getDefaultAdapter();
//        }
//        String name = blueToothName.getName();
//        if(name == null){
//            System.out.println("Name is null!");
//            name = blueToothName.getAddress();
//        }
//        return name;
   }

    public void discoveryListener(){
        discoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                //stop service
                //NsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                //stop service
                //NsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.d(TAG, "Service discoovery started");
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.d(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                Log.d(TAG, "Service discovery success" + serviceInfo);

            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "service lost: " + serviceInfo);
            }
        };
        //error here need help
       // NsdManager.discoverServices(TAG, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }
}
