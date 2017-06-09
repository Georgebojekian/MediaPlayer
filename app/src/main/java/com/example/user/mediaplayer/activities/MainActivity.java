package com.example.user.mediaplayer.activities;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.user.mediaplayer.R;
import com.example.user.mediaplayer.adapters.MediaAdapter;
import com.example.user.mediaplayer.fragments.MediaFragment;
import com.example.user.mediaplayer.MediaPlayerService;
import com.example.user.mediaplayer.model.Audio;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements MediaAdapter.IOnClickListener {
    MediaPlayerService mediaPlayerService;
    Boolean mBound = false;
    String path;
    boolean isServiceStarted = false;
    Intent intent;
    boolean isPlaying = true;
   private static ImageButton imgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().replace(R.id.container_id, new MediaFragment()).commit();

    }

    @Override
    public void onClickItem(Audio audio , final ImageButton playMusic) {
        intent = new Intent(MainActivity.this, MediaPlayerService.class);
        intent.setAction(MediaPlayerService.ACTION_START);
         path = audio.getData();
        String title = audio.getTitle();
         imgButton = playMusic;
        intent.putExtra("path", path);
        intent.putExtra("title", title);


        if (!mBound ){
            if(!isServiceStarted){
            startService(intent);
            isServiceStarted = true;
        }}
          if (isServiceStarted) {
              playMusic.setImageResource(android.R.drawable.ic_media_pause);
              bindService(intent,mConnection,Context.BIND_AUTO_CREATE);
              playMusic.setOnClickListener(new View.OnClickListener(){
                  @Override
                  public void onClick(View v) {
                      if (isPlaying) {
                          intent.setAction(MediaPlayerService.ACTION_PAUSE);
                          mediaPlayerService.pause(intent,playMusic);
                          playMusic.setImageResource(android.R.drawable.ic_media_play);
                          isPlaying = false;
                      }
                      else{
                          intent.setAction(MediaPlayerService.ACTION_RESUME);
                          mediaPlayerService.resume(intent,playMusic);
                          playMusic.setImageResource(android.R.drawable.ic_media_pause);
                          isPlaying = true;
                      }
                  }
              });
              isServiceStarted = false;
          }
          mBound = false;
            }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.LocalBinder localBinder = (MediaPlayerService.LocalBinder) service;
            mediaPlayerService = localBinder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    private boolean isServiceRunning(Class<MediaPlayerService> mediaPlayerServiceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(mediaPlayerServiceClass.getName().equals(service.service.getClassName())) {
                return  true;
            }
        }
        return  false;
    }
    public static ImageButton getBackground(){
        return imgButton;
    }
}