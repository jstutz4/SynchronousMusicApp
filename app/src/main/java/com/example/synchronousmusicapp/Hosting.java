package com.example.synchronousmusicapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Creates an object for a user to register a service with
 * Network Service Discovery so that his clients can find
 * the host information to connect to the host.
 */
public class Hosting extends AppCompatActivity {
        //music service
        private YesMediaBrowser musicService;
        private TryAudioStream tryAudioStream;
        //music service

        private NsdManager.RegistrationListener registrationListener;
        private NsdManager nsdManager;
        private String serviceName;
        private int LocalPort;
    final static String TAG = "SynchMusic Host";
    @Override
    /**
     * Will create the activity, while creating it will begin
     * to register the service.
     */
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

    /**
     * Will Register the service using the NSD API.
     * Will define the service by finding an open port
     * and using it along with the name of the service
     * "SynchMusic. It will be run through tcp with HTTP
     * protocol. After which it will register the service
     * so that it can be found. Uses the RegistrationListener
     * object to handle cases.
     * @param context
     * @throws IOException
     */
    public void registerService(Context context) throws IOException {

        int port = findOpenSocket();

        // Create the NsdServiceInfo object, and populate it.
        NsdServiceInfo serviceInfo = new NsdServiceInfo();

        // The name is subject to change based on conflicts
        // with other services advertised on the same network.
        serviceInfo.setServiceName("SynchMusic");
        serviceInfo.setServiceType("_http._tcp");
        serviceInfo.setPort(port);

        nsdManager = (NsdManager)context.getSystemService(Context.NSD_SERVICE);
        initializeRegistrationListener();
        nsdManager.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);
        Log.i(TAG, "We made it to the end of register service");

    }

    /**
     * When called will find an open socket to avoid any
     * conflict that might popup from using an occupied port.
     * @return Open port that was found.
     * @throws java.io.IOException
     */
    private int findOpenSocket() throws java.io.IOException {
        // Initialize a server socket on the next available port.
        ServerSocket serverSocket = new ServerSocket(0);

        // Store the chosen port.
        LocalPort =  serverSocket.getLocalPort();
        serverSocket.close();

        return LocalPort;
    }

    /**
     * This when called will create the registration listener
     * that will be used to handle cases when the service is registered.
     * OnServiceRegistered will confirm the success of registration as
     * well as change the service name if there is any conflict.
     * OnRegistrationFailed will indicate an error and provide the
     * error code.
     * onServiceUnregistered will confirm the success of the service
     * being unregistered.
     * onUnregistrationFailed will indicate the error when unregistration
     * fails
     */
    public void initializeRegistrationListener() {
        registrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                // Save the service name. Android may have changed it in order to
                // resolve a conflict, so update the name you initially requested
                // with the name Android actually used.
                serviceName = NsdServiceInfo.getServiceName();
                tryAudioStream = new TryAudioStream(LocalPort);
                tryAudioStream.transmit();
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

    /**
     *
     * @param view default
     */
    public void addMusic(View view) {
        Log.i(TAG, "start music service");
        //add music into raw so how

        Intent playmusic = new Intent(this,PlayMusic.class);
        startActivity(playmusic);
        musicService = new YesMediaBrowser();
        Log.i(TAG, "music service good");
    }
}
