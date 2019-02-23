package com.example.synchronousmusicapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class JoinGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
    }

    public void playMusic(View view) {
        Intent playMusic = new Intent(this,PlayMusic.class);
        startActivity(playMusic);
    }
}
