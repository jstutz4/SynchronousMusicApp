package com.example.synchronousmusicapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This is the class that listens for and then identifies the Network Service Discovery.
 */
class ServiceDiscovery {
    private Context context;
    private Activity activity;
    private NsdManager nsdManager;
    private NsdManager.ResolveListener resolveListener;
    private NsdManager.DiscoveryListener discoveryListener;
    private NsdServiceInfo NsdServiceInfo;
    private static final String TAG = "Synch Music SD";
    private static final String SERVICE_NAME = "SynchMusic";
    private static final String SERVICE_Type = "_http._tcp";
    private static final String SERVICE_Type_DOT = "_http._tcp" + ".";

    ServiceDiscovery(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    /**
     * initialize the service resolve listener.
     */
    public void initializeNsd(){
       this.resolveListener = initializeResolveListener();
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

            /**
             * checks to make sure the service found matches the service name and is not the same
             * ip device. Calls the resolve listener.
             * @param serviceInfo host ip, port number, service name
             */
            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                Log.i(TAG, "Service discovery successs " + serviceInfo);
                String serviceType = serviceInfo.getServiceType();
                Log.i(TAG, "Service discovery success: " + serviceInfo.getServiceName());
                boolean isOurService = (serviceType.equals(SERVICE_Type) || (serviceType.equals(SERVICE_Type_DOT)));
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

            /**
             *
             * @param serviceInfo  the host ip, port, and service name will be set to null
             */
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

            /**
             *
             * @param serviceInfo contains the name, host ip, and port of the service connected to.
             */
            @Override
            public void onServiceResolved(android.net.nsd.NsdServiceInfo serviceInfo) {
                Log.i(TAG, "Resolve Succeeded in SD line 90. " + serviceInfo);
                if(serviceInfo.getServiceName().equals(SERVICE_NAME)){
                    Log.d(TAG, "Same IP");
                }
                NsdServiceInfo = serviceInfo;
                Toast.makeText(context, "Found: " + serviceInfo.getHost().toString() + Integer.toString(serviceInfo.getPort()),
                        Toast.LENGTH_LONG).show();
                //String test = serviceInfo.getHost().getHostAddress();
                // add sockets idea

                //sockets
                SocketSend sendHost = new SocketSend();

                String host = serviceInfo.getHost().toString();
                host = host.substring(1);
                Log.d("Synch", "host ip: " + host);
                Log.i(TAG, "trying sockets to send " + host);
                try {
                    InetAddress address = InetAddress.getByName(host);

                    sendHost.sendToHost(address, serviceInfo.getPort());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }



                try {
                    OutputStream outputStream = sendHost.getOutputStream();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

                    String clientIp = InetAddress.getLocalHost().toString();

                    String sendMessage = clientIp + "\n";
                    bufferedWriter.write(sendMessage);
                    bufferedWriter.flush();
                    Log.i("Synch", "Message sent to the server : " + sendMessage);

                    //Get the return message from the server
                    InputStream is = sendHost.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String message = br.readLine();
                    Log.i("Synch", message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        return resolveListener;
    }


    /**
     * Stops any discover actions and then initialized discovery and resolve listeners and begins
     * searching for the desired service
     *
     */
    void discoverServices(){
        stopDiscovery();
        initializeNsd();
        initializeDiscoveryListener();
        nsdManager.discoverServices(SERVICE_Type, NsdManager.PROTOCOL_DNS_SD, discoveryListener);

    }

    /**
     * stops searching sets discovery listener to null
     */
    void stopDiscovery(){
        if(discoveryListener != null){
            nsdManager.stopServiceDiscovery(discoveryListener);
        }
        discoveryListener = null;
    }

}
