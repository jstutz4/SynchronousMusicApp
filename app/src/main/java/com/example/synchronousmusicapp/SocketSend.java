package com.example.synchronousmusicapp;

import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * takes the host ip and post number and opens up a socket connection
 */
public class SocketSend extends Socket {
    private static Socket socket;
    private static final String TAG = "SyncMusic socket";

    /**
     * connects to the host device
     * @param hostip device ip to connect to
     * @param port port number to connect to
     */
    public void sendToHost(InetAddress hostip, int port){
        Log.i(TAG, "trying to send message to host ?? " + hostip + " --- " + port);
        try {

            socket = new Socket(hostip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
