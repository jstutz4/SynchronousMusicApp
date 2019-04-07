package com.example.synchronousmusicapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URI;
import java.util.ArrayList;

/**
 * Loads previous selected song and loads the music UI to interact with the song.
 */
public class PlayMusic extends AppCompatActivity {
    private static MediaPlayer mp = new MediaPlayer();
    final static String TAG = "SynchMusic playmusic";
    String play = "play";
    String pause = "pause";

    private Server server;
    private Socket socket;
    private OutputStream outputStream;
    private ArrayList<String> songList;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private Activity activity = this;
    private SharedPreferences pref;
    private TryAudioStream audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int port = sharedPref.getInt("Port", 0);
        server = new Server(this, port);
        socket = server.getSocket();

       Thread music = new Thread(new Runnable() {
           @Override
           public void run() {
               runPermission();
               Log.i(TAG, "we are running play music activity");

               Log.i(TAG, "Look at Try audio stream");
           }
       });
       music.start();



    }

    private void pauseSong() {

        if(mp.isPlaying()) {
            mp.pause();
            Log.i(TAG, "pause Song");
        }
    }

    private void playSong() {

        if(!mp.isPlaying()) {
            mp.start();
            Log.i(TAG, "play song");
        }
        else{
            mp.stop();
        }
    }
    private void stopSong() {
        if(mp.isPlaying()){
            mp.stop();
        }

    }

    public Boolean musicState(){
        return false;
    }

    /**
     * Toggles play and pause button and calls the appropriate method.
     */
    public void changeMusicState(View view) {
        Log.d(TAG, "button clicked");
        Log.d(TAG, view.toString());

        Button b =  findViewById(R.id.button2);
        String states = b.getText().toString();
        String state = b.getContentDescription().toString();
        Log.i(TAG, "State is in " + state +" 2nd state is " + states.compareTo("play"));
//        if(mp.isPlaying() && states.compareTo("play") == 0){
//
//        }
//        else {
            if (states.compareTo("play") == 0 && !mp.isPlaying()) {
                Log.i(TAG, "song normal");
                //do something when toggle is on
                playSong();
                b.setText(pause);
            } else {
                //do something when toggle is off
                pauseSong();
                b.setText(play);
            }
        }
   // }

    private void getMusic() {
        Log.i(TAG, "getmusic started");
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null,"5",null,null);

        if (songCursor != null && (songCursor).moveToFirst()) {
            Log.i(TAG, "songCursoer 1\n " + songCursor.moveToFirst());

            Log.i(TAG, "getmusic started step 1");
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int songLength = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            do {
                Log.i(TAG, "getmusic started step 2");
                String currentTitle = songCursor.getString(songTitle);
                String currentLength = songCursor.getString(songLength);
                String currentLocation = songCursor.getString(songLocation);
                songList.add(currentTitle + "\n" + currentLength + "\n" + currentLocation);
                Log.i(TAG, currentTitle + "\n" + currentLength + "\n" + currentLocation);
            } while (songCursor.moveToNext());
        }
        // }

        songCursor.close();
        Log.d(TAG, "getMusic ened");

    }

    private void runPermission() {
        if(ContextCompat.checkSelfPermission(PlayMusic.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "made it to run permission");
            if(ActivityCompat.shouldShowRequestPermissionRationale(PlayMusic.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(PlayMusic.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            else{
                ActivityCompat.requestPermissions(PlayMusic.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        }
        else{
            Log.d(TAG, "permissions all good");
            dostuff();
        }
    }


      private void dostuff(){
          final String[] idResource2 = new String[1];
         final Context context = this;
          Log.i(TAG, "context: " + context);
        listView = findViewById(R.id.songList);
        songList = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songList);
        //if(adapter != null && listView != null) {
            Log.i(TAG, "Adaptor not null adding something to listview");

          runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  Log.i(TAG, "set Adapter thread working");
                  listView.setAdapter(adapter);
                  Log.i(TAG, "done adding");
                  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                      @Override
                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                          Context context1 = getApplicationContext();
                          String idResource = songList.get(position);
                          int pos =  idResource.indexOf("/");

                          idResource = idResource.substring(pos);
                          sendSong(idResource);
//                try {
                          playMusic(idResource);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                      }
                  });
              }
          });
        //}




      }

      public void sendSong(String idResource) {

          try {
              outputStream = socket.getOutputStream();
          } catch (IOException e) {
              e.printStackTrace();
          }
          PrintStream printStream = new PrintStream(outputStream);
          printStream.print(idResource);
          printStream.close();
      }

    private void playMusic(String idResource) {

        Log.d(TAG, "media player locating song");
        if(mp.isPlaying()){
            mp.stop();
            mp.reset();
        }
        mp = MediaPlayer.create(this, Uri.parse(idResource));


       //mp.setDataSource(idResource);
       //mp.prepare();
        if(mp != null) {
            View view = findViewById(R.id.button2);
//            if(mp.isPlaying()){
//                stopSong();
//                changeMusicState(view);
//                Log.i(TAG, "TRY AGIAN NEW SONG");
//                playMusic(idResource);
//            }

                Log.d(TAG, "media player is not null");

                changeMusicState(view);

       }
       Log.d(TAG, "media player null");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(PlayMusic.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Log.d(TAG,"Permission granted!");
                        dostuff();
                    }
                    else{
                        Log.d(TAG, "NO permission granted");
                        finish();
                    }
                }
            }
        }
    }
}
