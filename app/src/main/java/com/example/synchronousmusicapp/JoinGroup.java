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
    //public static interface WifiP2pManager.DnsSdServiceResponseListener;
    public String testID = "testID101";
//    WifiP2pManager manager;
//    WifiP2pManager.Channel channel;
    Activity a = this;
//    ServiceDiscovery serviceDiscovery;
//    // not sure how to figure this line out?
//    android.net.wifi.p2p.WifiP2pDevice mphone;
//    WifiP2pDnsSdServiceRequest serviceRequest;
//    android.net.wifi.p2p.WifiP2pManager.ChannelListener channelListener;


    //nsd manager
    final static String TAG = "SynchMusic";
    private NsdManager manager2;
    private NsdManager.DiscoveryListener discoveryListener;
    private NsdManager.ResolveListener resolveListener;
    private JoinGroup nsdHelper;
    private JoinGroup connection;
    private int LocalPort;

    // Create the NsdServiceInfo object, and populate it.
    NsdServiceInfo serviceInfo = new NsdServiceInfo();



    //nsd manager
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
       // manager.setDnsSdResponseListeners();
        //serviceDiscovery.onDnsSdServiceAvailable("TPC","Synch Music",mphone);
       //I think the line below needs to go in the hosting side
       // manager.setDnsSdResponseListeners(channel, serviceDiscovery, txtListener);
        //manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        // need to now how to build the channel
        //channel = manager.initialize(this, getMainLooper(),channelListener);

        //manager.addServiceRequest();
        //manager.discoverServices();
//        this.addServiceRequest();
//        this.discoverServices();

        //Nsd attempt
        try {
            this.initializeServiceInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }

        initializeDiscoveryListener();
        initializeResolveListener();
        discoverServices("_http._tcp",NsdManager.PROTOCOL_DNS_SD,discoveryListener);
        //manager2.resolveService(serviceInfo,resolveListener);

    }

    @Override
    protected void onPause() {
        if (discoveryListener != null) {
            this.tearDown();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (discoveryListener != null) {
            this.discoverServices("_http._tcp",NsdManager.PROTOCOL_DNS_SD,discoveryListener);
        }
    }

    @Override
    protected void onDestroy() {
        this.tearDown();
        connection.tearDown();
        super.onDestroy();
    }

    // NsdHelper's tearDown method
    public void tearDown() {
        manager2.stopServiceDiscovery(discoveryListener);
    }
    
    
    //NsdManager

    public void initializeServiceInfo() throws IOException {
        int port = findOpenSocket();
        // The name is subject to change based on conflicts
        // with other services advertised on the same network.
        serviceInfo.setServiceName("SynchMusic");
        serviceInfo.setServiceType("_http._tcp");
        serviceInfo.setPort(port);
    }

    public int findOpenSocket() throws java.io.IOException {
        // Initialize a server socket on the next available port.
        ServerSocket serverSocket = new ServerSocket(0);

        // Store the chosen port.
        LocalPort =  serverSocket.getLocalPort();
        serverSocket.close();

        return LocalPort;
    }
        public void initializeDiscoveryListener(){
         discoveryListener = new NsdManager.DiscoveryListener() {
             @Override
             public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                 Log.e(TAG, "start Discovery failed: " + errorCode);
                 manager2.stopServiceDiscovery(this);
             }

             @Override
             public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, " stop Discovery failed: " + errorCode);
                manager2.stopServiceDiscovery(this);
             }

             @Override
             public void onDiscoveryStarted(String serviceType) {
                 Toast.makeText(a, " Discovery started", Toast.LENGTH_LONG).show();
             }

             @Override
             public void onDiscoveryStopped(String serviceType) {
                Log.d(TAG, "discovery STopped");
             }

             @Override
             public void onServiceFound(NsdServiceInfo serviceInfo) {
                 Toast.makeText(a, " Synch Music service found", Toast.LENGTH_LONG).show();

                 if(!serviceInfo.getServiceType().equals("_http._tcp")){
                     Log.d(TAG, "Unknow Servic Type " + serviceInfo.getServiceType());
                 }
                 else if(serviceInfo.getServiceName().equals("SynchMusic")){
                     Log.d(TAG, "Same device: ");
                 }
                 else if(serviceInfo.getServiceName().contains("SynchMusic")){
                     manager2.resolveService(serviceInfo,resolveListener);
                 }

             }

             @Override
             public void onServiceLost(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "Service lost");
             }
         };
        }


        public void initializeResolveListener(){
            resolveListener = new NsdManager.ResolveListener() {
                @Override
                public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                    Log.e(TAG, "Resolve failed " +errorCode);
                }

                @Override
                public void onServiceResolved(NsdServiceInfo serviceInfo) {
                    Log.d(TAG, "resolve Succeeded " + serviceInfo);
                    if(serviceInfo.getServiceName().equals("SynchMusic")){
                        Log.d(TAG, "Same IP");

                    }
                    //Not sure what this does or if we need to duplicate code from the host.jave
//                    mService = serviceInfo;
//                    int port = mService.getPort();
//                    InetAddress host = mService.getHost();
                }
            };
        }
    ///NsdManager

    public void playMusic(View view) {
        Intent playMusic = new Intent(this,PlayMusic.class);
        startActivity(playMusic);
    }

//    public void addServiceRequest(){
//        serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
//        manager.addServiceRequest(channel,
//                serviceRequest,
//                new WifiP2pManager.ActionListener() {
//                    @Override
//                    public void onSuccess() {
//                        // Success!
//                        Toast.makeText(a, " add service request Succuss", Toast.LENGTH_LONG).show();
//
//                    }
//
//                    @Override
//                    public void onFailure(int code) {
//                        // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
//                        if (code == WifiP2pManager.P2P_UNSUPPORTED) {
//                            Log.d("Synch Music", "P2P isn't supported on this device.");
//                        }
//                        else{
//                            Toast.makeText(a, "add service request went wrong", Toast.LENGTH_LONG).show();
//
//                        }
//                    }
//                });
//    }

    public void discoverServices(String serviceType, int protocol, NsdManager.DiscoveryListener listener){
            stopDiscovery();
            initializeDiscoveryListener();
            discoverServices("_http._tcp",NsdManager.PROTOCOL_DNS_SD,discoveryListener);
    }


    public void stopDiscovery() {
        if (discoveryListener != null) {
            try {
                stopServiceDiscovery(discoveryListener);
            } finally {
                discoveryListener = null;
            }
        }
    }

    public void stopServiceDiscovery(NsdManager.DiscoveryListener listener){
            Log.e(TAG,"Impliment this function i guess");
    }

//    public void discoverPeers(){
//        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(a, "Succuss, found peers", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onFailure(int reason) {
//                Toast.makeText(a, " peers Failer achived Way to Go!", Toast.LENGTH_LONG).show();
//
//            }
//        });
//    }



    public Boolean checkGroupId(String id){
      return id.equals(testID);
    }
}
