package com.example.synchronousmusicapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PlayMusic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
    }

    public Boolean musicState(){
        return false;
    }
}
