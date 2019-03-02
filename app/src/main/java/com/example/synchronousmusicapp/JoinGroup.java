package com.example.synchronousmusicapp;

import android.content.Intent;
import android.net.nsd.NsdManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class JoinGroup extends AppCompatActivity {
    public String testID = "testID101";
    private RequestService discoveryListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
    }

    public void playMusic(View view) {
        Intent playMusic = new Intent(this,PlayMusic.class);
        startActivity(playMusic);
    }



    public Boolean checkGroupId(String id){
      return id.equals(testID);
    }
}
