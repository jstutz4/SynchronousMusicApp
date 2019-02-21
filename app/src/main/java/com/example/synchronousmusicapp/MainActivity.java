package com.example.synchronousmusicapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void GetHost(View view) {
        Intent hosting = new Intent(this,Hosting.class);
        startActivity(hosting);
    }

    public void JoinGroup(View view) {
        Intent join = new Intent(this,JoinGroup.class);
        startActivity(join);
    }
}
