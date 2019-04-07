package com.example.synchronousmusicapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * The interface with which the user interacts with.
 * Upon pressing the different buttons their respective
 * functions are called and executed.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //type your code
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
