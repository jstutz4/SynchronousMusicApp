package com.example.synchronousmusicapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class JoinGroup extends AppCompatActivity {
    public String testID = "testID101";
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    Activity a = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(),null);
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(a, "Succuss, now start receiving music stream", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(a, "Failuer achived Way to Go!", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void playMusic(View view) {
        Intent playMusic = new Intent(this,PlayMusic.class);
        startActivity(playMusic);
    }



    public Boolean checkGroupId(String id){
      return id.equals(testID);
    }
}
