package com.example.synchronousmusicapp;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

public class ServiceDiscovery implements WifiP2pManager.DnsSdServiceResponseListener {
    @Override
    public void onDnsSdServiceAvailable(String instanceName, String registrationType, WifiP2pDevice srcDevice) {
        //update the device with a human-friendly version from the DnsTextRecord, assuming one arrived

        srcDevice.deviceName = "Test101";
        // not sure what to add here

        // add to the cumstom adapter defined specifiaally for showing wifi devices

        Log.d("SynchronousMusic", "onBonjourServiceAvailable " + instanceName);


    }
}
