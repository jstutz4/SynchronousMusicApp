package com.example.synchronousmusicapp;

import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class SocketSend extends Socket {
    private static Socket socket;
    private static final String TAG = "SyncMusic socket";

    public void sendToHost(InetAddress hostip, int port){
        Log.i(TAG, "trying to send message to host ?? " + hostip + " --- " + port);
        try {

            socket = new Socket(hostip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
