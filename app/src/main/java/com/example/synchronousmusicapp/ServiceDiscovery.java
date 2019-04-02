package com.example.synchronousmusicapp;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

class ServiceDiscovery {
    private Context context;
    private NsdManager nsdManager;
    private NsdManager.ResolveListener resolveListener;
    private NsdManager.DiscoveryListener discoveryListener;



    private NsdServiceInfo NsdServiceInfo;
    private static final String TAG = "Synch Music SD";
    private static final String SERVICE_NAME = "SynchMusic";
    private static final String SERVICE_Type = "_http._tcp.";
    private static final String SERVICE_Type_DOT = "_http._tcp" + ".";

    ServiceDiscovery(Context context){
        this.context = context;
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    void initializeNsd(){
        initializeResolveListener();
    }

    public void initializeDiscoveryListener(){
        discoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);

            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            }

            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.i(TAG, "Discovery started: ");
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "discovery stopped: " + serviceType);
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                Log.i(TAG, "Service discovery successs " + serviceInfo);
                String serviceType = serviceInfo.getServiceType();
                Log.i(TAG, "Service discovery success: " + serviceInfo.getServiceName());
                boolean isOurService = serviceType.equals(SERVICE_Type);
                if(!isOurService){
                    Log.d(TAG, "Unknown service type: " + serviceInfo.getServiceType());
                }
                else if(serviceInfo.getServiceType().equals(SERVICE_NAME)){
                    Log.d(TAG, "Same device: " + SERVICE_NAME);
                }
                else if(serviceInfo.getServiceName().contains(SERVICE_NAME)){
                    Log.d(TAG, "different devices (" +serviceInfo.getServiceName() + "-" + SERVICE_NAME + ")");
                    stopDiscovery();
                    nsdManager.resolveService(serviceInfo,initializeResolveListener());
                    Log.d(TAG, "resolve function called");
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                Log.d(TAG, "Service lost " + serviceInfo);
                if(NsdServiceInfo == serviceInfo){
                    NsdServiceInfo = null;
                }
            }
        };
    }

    private NsdManager.ResolveListener initializeResolveListener(){
        resolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(android.net.nsd.NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Resolve failed" + errorCode);
            }

            @Override
            public void onServiceResolved(android.net.nsd.NsdServiceInfo serviceInfo) {
                Log.i(TAG, "Resolve Succeeded in SD line 90. " + serviceInfo);
                if(serviceInfo.getServiceName().equals(SERVICE_NAME)){
                    Log.d(TAG, "Same IP");
                }
                NsdServiceInfo = serviceInfo;
            }
        };

        return resolveListener;
    }

    void discoverServices(){
        stopDiscovery();
        initializeNsd();
        initializeDiscoveryListener();
        nsdManager.discoverServices(SERVICE_Type, NsdManager.PROTOCOL_DNS_SD, discoveryListener);

    }

    void stopDiscovery(){
        if(discoveryListener != null){
            nsdManager.stopServiceDiscovery(discoveryListener);
        }
        discoveryListener = null;
    }

    public android.net.nsd.NsdServiceInfo getNsdServiceInfo() {
        return NsdServiceInfo;
    }
}
