package com.example.synchronousmusicapp;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;

public class Hosting extends AppCompatActivity {

        private NsdManager.RegistrationListener registrationListener;
        private NsdManager nsdManager;
        private String serviceName;
        private int LocalPort;
    final static String TAG = "SynchMusic Host";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosting);


        try {
            registerService(this);
            Log.i(TAG, "register service try catch pass");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void registerService(Context context) throws IOException {

        int port = findOpenSocket();

        // Create the NsdServiceInfo object, and populate it.
        NsdServiceInfo serviceInfo = new NsdServiceInfo();

        // The name is subject to change based on conflicts
        // with other services advertised on the same network.
        serviceInfo.setServiceName("SynchMusic");
        serviceInfo.setServiceType("_http._tcp");
        serviceInfo.setPort(port);

        nsdManager = (NsdManager)context.getSystemService(context.NSD_SERVICE);
        initializeRegistrationListener();
        nsdManager.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);
        Log.i(TAG, "We made it to the end of register service");

    }

    private int findOpenSocket() throws java.io.IOException {
        // Initialize a server socket on the next available port.
        ServerSocket serverSocket = new ServerSocket(0);

        // Store the chosen port.
        LocalPort =  serverSocket.getLocalPort();
        serverSocket.close();

        return LocalPort;
    }

    public void initializeRegistrationListener() {
        registrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                // Save the service name. Android may have changed it in order to
                // resolve a conflict, so update the name you initially requested
                // with the name Android actually used.
                serviceName = NsdServiceInfo.getServiceName();
                Log.i("TAG", "register listener initialized");
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Registration failed! Put debugging code here to determine why.
                Log.e("TAG", "Service register failed");
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                // Service has been unregistered. This only happens when you call
                // NsdManager.unregisterService() and pass in this listener.
                Log.i("TAG", "Service Unregister");
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Unregistration failed. Put debugging code here to determine why.
                Log.w("TAG", "Service Unregister failed");
            }
        };
    }

}
