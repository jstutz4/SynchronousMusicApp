package com.example.synchronousmusicapp;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Sets up server socket and opens it in the background
 * and remains open in the background for
 * other sockets to connect to.
 */
public class Server {
    private Activity activity;
    private ServerSocket serverSocket;
    private Socket socket1;
    private String  TAG = "Server";
    private String message = "";
    private int port;

    /**
     * Constructor that initiates the server.
     * @param port Port from NSD for consistency
     */
    public Server(int port) {
        this.port = port;

        Log.d(TAG, "Server constructed");
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    /**
     * Thread that will run the socket server and open
     * it in the background so that other sockets can
     * connect to it.
     */
    private class SocketServerThread extends Thread {

        int count = 0;

        @Override
        public void run() {
            try {
                // create ServerSocket using specified port
                serverSocket = new ServerSocket(port);
                Log.d(TAG, "Begin wait for clients");
                while (true) {
                    // block the call until connection is created and return
                    // Socket object
                    Socket socket = serverSocket.accept();
                    count++;
                    message += "#" + count + " from "
                            + socket.getInetAddress() + ":"
                            + socket.getPort() + "\n";

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, message);
                        }
                    });

                    socket1 = socket;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return socket1;
    }

}