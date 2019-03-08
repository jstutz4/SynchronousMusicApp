package com.example.synchronousmusicapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class JoinGroup extends AppCompatActivity {
    //public static interface WifiP2pManager.DnsSdServiceResponseListener;
    public String testID = "testID101";
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    Activity a = this;
    ServiceDiscovery serviceDiscovery;
    // not sure how to figure this line out?
    android.net.wifi.p2p.WifiP2pDevice mphone;
    WifiP2pDnsSdServiceRequest serviceRequest;
    android.net.wifi.p2p.WifiP2pManager.ChannelListener channelListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
       // manager.setDnsSdResponseListeners();
        //serviceDiscovery.onDnsSdServiceAvailable("TPC","Synch Music",mphone);
       //I think the line below needs to go in the hosting side
       // manager.setDnsSdResponseListeners(channel, serviceDiscovery, txtListener);
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        // need to now how to build the channel
        channel = manager.initialize(this, getMainLooper(),channelListener);

        //manager.addServiceRequest();
        //manager.discoverServices();
        this.addServiceRequest();
        this.discoverServices();

    }

    public void playMusic(View view) {
        Intent playMusic = new Intent(this,PlayMusic.class);
        startActivity(playMusic);
    }

    public void addServiceRequest(){
        serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        manager.addServiceRequest(channel,
                serviceRequest,
                new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        // Success!
                        Toast.makeText(a, " add service request Succuss", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(int code) {
                        // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                        if (code == WifiP2pManager.P2P_UNSUPPORTED) {
                            Log.d("Synch Music", "P2P isn't supported on this device.");
                        }
                        else{
                            Toast.makeText(a, "add service request went wrong", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    public void discoverServices(){
        manager.discoverServices(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // Success!
                Toast.makeText(a, "Succuss, now start receiving music stream", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(int code) {
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                if (code == WifiP2pManager.P2P_UNSUPPORTED) {
                    Log.d("Synch Music", "P2P isn't supported on this device.");
                }
                else{
                    Toast.makeText(a, "discover services went wrong", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void discoverPeers(){
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(a, "Succuss, found peers", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(a, " peers Failer achived Way to Go!", Toast.LENGTH_LONG).show();

            }
        });
    }



    public Boolean checkGroupId(String id){
      return id.equals(testID);
    }
}
