package com.example.synchronousmusicapp;


import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.rtp.AudioCodec;
import android.net.rtp.AudioGroup;
import android.net.rtp.AudioStream;
import android.net.rtp.RtpStream;
import android.util.Log;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import static android.media.AudioAttributes.CONTENT_TYPE_MUSIC;
import static android.media.AudioAttributes.FLAG_AUDIBILITY_ENFORCED;
import static android.media.AudioAttributes.USAGE_MEDIA;

/**
 * This class tries to stream the audio between connected devices.
 */
public class TryAudioStream {

    private AudioStream audioStream;
    private AudioGroup audioGroup;
    private InetAddress destination;
    private String multicastIp;
    private int port;
   //private SharedPreferences pref;
    private final static String TAG = "Audio stream";

    public TryAudioStream(int port,String clientIP, MediaPlayer stream) {
        try {
            InetAddress hoster = InetAddress.getLocalHost();
            audioGroup = new AudioGroup();
            audioGroup.setMode(AudioGroup.MODE_NORMAL);
            audioStream = new AudioStream(hoster);
            this.port = port;
            multicastIp = "224.0.0.10";
           // destination = InetAddress.getByName(clientIP);
            destination = hoster;
            Log.i(TAG, "client ip: " + destination);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public TryAudioStream(int port,InetAddress hostIP){
        this.port = port;
        this.destination = hostIP;
    }

    public void receive() {
        Log.i(TAG, "STArt receiving");
        audioStream.join(null);
        audioStream.setCodec(AudioCodec.AMR);
        audioStream.setMode(RtpStream.MODE_RECEIVE_ONLY);
        audioStream.associate(destination, port);
        audioStream.join(audioGroup);
    }

    public void transmit() {
        Log.i(TAG, "start transmitting");
        audioStream.join(null);
        audioStream.setCodec(AudioCodec.AMR);
        audioStream.setMode(RtpStream.MODE_SEND_ONLY);
        audioStream.associate(destination, port);
        audioStream.join(audioGroup);
    }


}
