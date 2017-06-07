package com.example.user.mediaplayer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.mediaplayer.R;
import com.example.user.mediaplayer.adapters.MediaAdapter;
import com.example.user.mediaplayer.fragments.MediaFragment;
import com.example.user.mediaplayer.MediaPlayerService;
import com.example.user.mediaplayer.model.Audio;

public class MainActivity extends AppCompatActivity implements MediaAdapter.IOnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().replace(R.id.container_id,new MediaFragment()).commit();
    }

    @Override
    public void onClickItem(Audio audio) {
            Intent playIntent = new Intent(MainActivity.this, MediaPlayerService.class);
            playIntent.setAction(MediaPlayerService.ACTION_START);
            String path = audio.getData();
            String title = audio.getTitle();
            playIntent.putExtra("path", path);
            playIntent.putExtra("title",title);
            startService(playIntent);

    }

}
