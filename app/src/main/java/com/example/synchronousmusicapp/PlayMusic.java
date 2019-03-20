package com.example.synchronousmusicapp;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

/**
 * Loads previous selected song and loads the music UI to interact with the song.
 */
public class PlayMusic extends AppCompatActivity {
    private MediaPlayer mp;
    final static String TAG = "SynchMusic playmusic";
    String play = "play";
    String pause = "pause";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        Log.i(TAG, "we are running play music activity");

        Log.i(TAG, "mp is fine");
        mp = MediaPlayer.create(this, R.raw.kalimba);
        Log.i(TAG, "what happened to the button");

    }

    private void pauseSong() {
        Log.i(TAG, "pause Song");
        if(mp.isPlaying()) {
            mp.pause();
        }
    }

    private void playSong() {
        Log.i(TAG, "play song");
        if(!mp.isPlaying()) {
            mp.start();
        }
    }

    public Boolean musicState(){
        return false;
    }

    /**
     * Toggles play and pause button and calls the appropriate method.
     * @param view default
     */
    public void changeMusicState(View view) {
        Log.d(TAG, "button clicked");

        Button b =  findViewById(R.id.button2);
        String states = b.getText().toString();
        String state = b.getContentDescription().toString();
        Log.i(TAG, "State is in " + state +" 2nd state is " + states.compareTo("play"));
        if(states.compareTo("play") == 0)
        {
            //do something when toggle is on
            playSong();
            b.setText(pause);
        }
        else
        {
            //do something when toggle is off
            pauseSong();
            b.setText(play);
        }
    }
}
