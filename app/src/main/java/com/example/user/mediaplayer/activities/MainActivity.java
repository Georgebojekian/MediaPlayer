package com.example.user.mediaplayer.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.mediaplayer.R;
import com.example.user.mediaplayer.adapters.MediaAdapter;
import com.example.user.mediaplayer.fragments.MediaFragment;
import com.example.user.mediaplayer.MediaPlayerService;
import com.example.user.mediaplayer.model.Audio;

public class MainActivity extends AppCompatActivity implements MediaAdapter.IOnClickListener {
    MediaPlayerService mediaPlayerService;
    Boolean mBound = false;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().replace(R.id.container_id, new MediaFragment()).commit();
    }

    @Override
    public void onClickItem(Audio audio) {
        Intent intent = new Intent(MainActivity.this, MediaPlayerService.class);
        intent.setAction(MediaPlayerService.ACTION_START);
         path = audio.getData();
        String title = audio.getTitle();
        intent.putExtra("path", path);
        intent.putExtra("title", title);

//            if(path.equals(intent.getStringExtra("path"))) {
                startService(intent);
        if (!mBound) {
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                if (intent.getAction().equals(MediaPlayerService.ACTION_PAUSE)) {
                    mediaPlayerService.pause(intent);
                } else if (intent.getAction().equals(MediaPlayerService.ACTION_RESUME)) {
                    mediaPlayerService.resume(intent);
//                }
            }
        }
//        mBound = false;
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


}