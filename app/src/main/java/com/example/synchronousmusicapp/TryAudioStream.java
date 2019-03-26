package com.example.synchronousmusicapp;


import android.net.rtp.AudioCodec;
import android.net.rtp.AudioGroup;
import android.net.rtp.AudioStream;
import android.net.rtp.RtpStream;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class TryAudioStream {

    private AudioStream audioStream;
    private AudioGroup audioGroup;
    private InetAddress destination;
    private int port;

    public TryAudioStream(InetAddress destination, int port) {
        try {
            audioGroup = new AudioGroup();
            audioGroup.setMode(AudioGroup.MODE_NORMAL);
            audioStream = new AudioStream(InetAddress.getLocalHost());
            this.destination = destination;
            this.port = port;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void recieve() {
        audioStream.join(null);
        audioStream.setCodec(AudioCodec.AMR);
        audioStream.setMode(RtpStream.MODE_RECEIVE_ONLY);
        audioStream.associate(destination, port);
        audioStream.join(audioGroup);
    }

    public void transmit() {
        audioStream.join(null);
        audioStream.setCodec(AudioCodec.AMR);
        audioStream.setMode(RtpStream.MODE_SEND_ONLY);
        audioStream.associate(destination, port);
        audioStream.join(audioGroup);
    }


}
